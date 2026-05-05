package cicada.client.module.impl.combat

import cicada.client.event.Event
import cicada.client.event.impl.TickEvent
import cicada.client.module.Category
import cicada.client.module.Module
import cicada.client.module.impl.combat.sprintreset.*
import cicada.client.utils.math.minus
import cicada.client.utils.player
import kotlin.math.cos

// испорченно SCWGxD в 28.12.2025:20:29
object ModuleSprintReset : Module(
    "SprintReset",
    Category.COMBAT
) {
    private val mode = choice("Mode").apply {
        choice(SprintTap)
        choice(WTap).select()
        choice(Packet)
        choice(OnePacket)
    }
    
    private val delay by int("Delay", 3, 0..10)
    private val reset by int("Reset", 2, 1..10)
    private val conditions = multiChoice("Conditions")
    private val notInLiquid = conditions.choice("Not in liquid")
    private val notWhileKB = conditions.choice("Not while knockback")
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
                    ModuleAttackAura.targetFinder.target
                else if (false)
                    ModuleAttackAura.targetFinder.target // other module
                else
                    null // lastAttackedTarget

            if (target != null) {
                val knockbackDot = player.deltaMovement.dot(target.position() - player.position())

                if (
                    target.hurtTime == 0 &&
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