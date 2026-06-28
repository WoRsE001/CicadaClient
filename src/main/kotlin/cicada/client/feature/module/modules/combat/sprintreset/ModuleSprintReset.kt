package cicada.client.feature.module.modules.combat.sprintreset

import cicada.client.event.Event
import cicada.client.event.impl.TickEvent
import cicada.client.feature.module.ClientModule
import cicada.client.feature.module.ModuleCategory
import cicada.client.feature.module.modules.combat.attackaura.ModuleAttackAura
import cicada.client.feature.module.modules.combat.sprintreset.mode.*
import cicada.client.utils.client.player
import cicada.client.utils.math.minus
import kotlin.math.cos

// испорченно SCWGxD в 28.12.2025:20:29
object ModuleSprintReset : ClientModule(
    "SprintReset",
    ModuleCategory.COMBAT
) {
    private val mode = choice("Mode").apply {
        choice(SprintResetSprintTap)
        choice(SprintResetWTap)
        choice(SprintResetPacket)
        choice(SprintResetOnePacket)
    }

    private val delay by int("Delay", 1, 0..10)
    private val reset by int("Reset", 1, 1..10)
    private val conditions = multiChoice("Conditions")
    private val notInLiquid = conditions.choice("Not in liquid")
    private val notWhileKB = conditions.choice("Not while knockback", true)
    private val notWhileKBFOV by int("Permissible FOV for knockback", 45, 0..180, "deg")

    private var delayTimer = 0
    private var resetTimer = 0
    private var isResetting = false

    override fun onDisable() {
        delayTimer = 0
        resetTimer = 0
        isResetting = false
    }

    override fun onEvent(event: Event) {
        val currentSubMode = mode.inner

        if (currentSubMode !is SprintResetMode)
            return

        if (event is TickEvent.Pre) {
            val target =
                if (ModuleAttackAura.toggled)
                    ModuleAttackAura.target
                else if (false)
                    ModuleAttackAura.target // other module
                else
                    null // lastAttackedTarget

            if (target != null) {
                val knockbackDot = player.deltaMovement.dot(target.position() - player.position())

                if (
                    target.hurtTime == 10 &&
                    (!notInLiquid.toggled || !player.isInLiquid) &&
                    (!notWhileKB.toggled || knockbackDot >= cos(Math.toRadians(notWhileKBFOV.toDouble())))
                ) {
                    delayTimer = delay
                    resetTimer = reset
                }
            }

            if (delayTimer > 0) delayTimer--
        }

        if (resetTimer == 0 && isResetting && currentSubMode.stopReset(event))
            isResetting = false

        if (resetTimer == 0 || delayTimer > 0) return

        if (!isResetting && currentSubMode.startReset(event))
            isResetting = true

        if (currentSubMode.reset(event)) {
            resetTimer--
        }
    }
}