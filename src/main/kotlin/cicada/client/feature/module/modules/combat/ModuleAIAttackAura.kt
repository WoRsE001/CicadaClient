package cicada.client.feature.module.modules.combat

import cicada.client.event.Event
import cicada.client.event.impl.EventTick
import cicada.client.feature.module.ClientModule
import cicada.client.feature.module.ModuleCategory
import cicada.client.rotation.Rotation
import cicada.client.rotation.ai.RotationNN
import cicada.client.setting.preset.TargetFinder
import cicada.client.utils.client.player
import cicada.client.utils.math.minus
import cicada.client.utils.player.canCrit
import cicada.client.utils.rotation.rotate
import cicada.client.utils.rotation.rotation

// SCWGxD regrets everything he did. 29.06.2026 11:58.
object ModuleAIAttackAura : ClientModule("AIAttackAura", ModuleCategory.COMBAT) {
    // Number of features captured per game tick. MUST match RotationNN.SAMPLE_SIZE and the
    // order produced by Sample.parse() / consumed by RotationNN.parseDataSet().
    private const val SAMPLE_SIZE = 15

    private val targetFinder = tree(TargetFinder())
    private val attackRange by float("Attack range", 3f, 0f..6f)

    // Rolling buffer of the most recent frames (one 15-feature DoubleArray per tick),
    // trimmed to the model's `history` length each tick. The model expects the history of
    // our previous positions/rotations as input and emits a single rotation delta per tick.
    val data = ArrayList<DoubleArray>()
    var model: RotationNN? = null
    private var lastRotation = Rotation(0f, 0f)

    override fun onEvent(event: Event) {
        // Capture into a local so the null check sticks (model is a mutable var set from a
        // command thread and would otherwise not smart-cast).
        val model = model ?: return

        if (event is EventTick.Pre) {
            targetFinder.updateTarget()

            val target = targetFinder.target
            if (target == null) {
                // No target: drop the history so a future engagement starts from a clean window.
                data.clear()
                lastRotation = player.rotation()
                return
            }

            val diff = target.position() - player.position()
            val delta = (player.rotation() - lastRotation).wrapped()

            // Append this tick's frame in the canonical feature order.
            data += doubleArrayOf(
                diff.x / 6, diff.y / 6, diff.z / 6,
                delta.x.toDouble() / 90, delta.y.toDouble() / 180,
                if (player.canCrit()) 1.0 else 0.0,
                player.hurtTime / 10.0, target.hurtTime / 10.0,
                if (player.input.keyPresses.forward) 1.0 else -1.0,
                if (player.input.keyPresses.backward) 1.0 else -1.0,
                if (player.input.keyPresses.left) 1.0 else -1.0,
                if (player.input.keyPresses.right) 1.0 else -1.0,
                if (player.input.keyPresses.jump) 1.0 else -1.0,
                if (player.input.keyPresses.shift) 1.0 else -1.0,
                if (player.input.keyPresses.sprint) 1.0 else -1.0
            )

            // Keep only the last `history` frames.
            while (data.size > model.history) data.removeAt(0)

            // Need a full window before we can predict.
            if (data.size == model.history) {
                player.rotate(buildPredictedDelta(model))
            }

            lastRotation = player.rotation()
        }
    }

    // Builds the model input exactly as RotationNN.parseDataSet does: every frame contributes
    // its full feature vector, except the LAST (predicted) frame which omits its own delta
    // (2 values) to avoid target leakage. Returns the de-normalized rotation delta to apply.
    private fun buildPredictedDelta(model: RotationNN): Rotation {
        val history = model.history
        val input = DoubleArray(model.inputCount) // history * SAMPLE_SIZE - 2
        var idx = 0

        for (j in 0 until history) {
            val frame = data[j]
            // diff (x, y, z)
            input[idx++] = frame[0]
            input[idx++] = frame[1]
            input[idx++] = frame[2]
            // delta (x, y) — omitted for the last frame (that's what we're predicting)
            if (j != history - 1) {
                input[idx++] = frame[3]
                input[idx++] = frame[4]
            }
            // canCrit
            input[idx++] = frame[5]
            // hurt times (player, target)
            input[idx++] = frame[6]
            input[idx++] = frame[7]
            // movement keys (forward..sprint)
            for (k in 8 until SAMPLE_SIZE) input[idx++] = frame[k]
        }

        val out = model.predict(input)

        // Output is TANH-normalized the same way the targets were (pitch/90, yaw/180): undo it.
        return Rotation((out[0] * 90).toFloat(), (out[1] * 180).toFloat())
    }
}