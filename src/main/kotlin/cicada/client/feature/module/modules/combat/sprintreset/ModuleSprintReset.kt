package cicada.client.feature.module.modules.combat.sprintreset

import cicada.client.event.Event
import cicada.client.event.events.EventAttack
import cicada.client.event.events.EventTick
import cicada.client.feature.module.ClientModule
import cicada.client.feature.module.ModuleCategory
import cicada.client.feature.module.modules.combat.attackaura.ModuleAttackAura
import cicada.client.feature.module.modules.combat.sprintreset.mode.*
import cicada.client.utils.client.player

// испорченно SCWGxD в 28.12.2025:20:29
object ModuleSprintReset : ClientModule("SprintReset", ModuleCategory.COMBAT) {
    private val mode = choice("Mode").apply {
        choice(SprintResetSprintTap).select()
        choice(SprintResetWTap)
        choice(SprintResetPacket)
        choice(SprintResetOnePacket)
    }

    private val delayTicks by int("DelayTicks", 0, 0..10)
    private val resetTicks by int("ResetTicks", 1, 1..10)

    private var delayTick = 0
    private var resetTick = 0

    private var resettingPhase = Phase.NONE

    override fun onEvent(event: Event) {
        val _mode = mode.inner as SprintResetMode

        if (event is EventAttack.Post && resettingPhase == Phase.NONE) {
            resettingPhase = Phase.SHOULD
        }

        if (event is EventTick.Pre) {
            if (delayTick > 0) {
                delayTick--
            }

            if (resettingPhase == Phase.SHOULD) {
                if (player.isSprinting) {
                    delayTick = delayTicks
                    resetTick = resetTicks
                    resettingPhase = Phase.START
                } else {
                    resettingPhase = Phase.NONE
                }
            }
        }

        if (delayTick > 0 || resetTick == 0) {
            if (resettingPhase == Phase.PROCESS && _mode.stopReset(event)) {
                resettingPhase = Phase.NONE
            }

            return
        }

        if (resettingPhase == Phase.START && _mode.startReset(event)) {
            resettingPhase = Phase.PROCESS
        }

        if (resettingPhase == Phase.PROCESS && _mode.reset(event)) {
            resetTick--
        }
    }

    private enum class Phase {
        NONE,
        SHOULD,
        START,
        PROCESS
    }
}
