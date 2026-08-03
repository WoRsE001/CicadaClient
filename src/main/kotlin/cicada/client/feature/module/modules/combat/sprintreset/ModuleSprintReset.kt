package cicada.client.feature.module.modules.combat.sprintreset

import cicada.client.event.Event
import cicada.client.event.impl.EventTick
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

    private val delayTicks by int("Delay ticks", 0, 0..10)
    private val resetTicks by int("Reset ticks", 1, 1..10)

    private var delayTick = 0
    private var resetTick = 0

    private var shouldReset = false
    private var resetting = false

    override fun onEvent(event: Event) {
        val _mode = mode.inner as SprintResetMode

        if (event is EventTick.Pre) {
            val target = ModuleAttackAura.target

            if (target == null) {
                delayTick = 0
                resetTick = 0
                return
            }

            if (target.hurtTime == 10 && delayTick == 0 && resetTick == 0) {
                delayTick = delayTicks
                resetTick = resetTicks
                shouldReset = true
            }

            if (delayTick > 0) {
                delayTick--
            }
        }

        if (delayTick > 0 || resetTick == 0) {
            if (resetting && _mode.stopReset(event)) {
                resetting = false
            }

            return
        }

        if (shouldReset && _mode.startReset(event)) {
            shouldReset = false
        }

        if (_mode.reset(event)) {
            resetTick--
            resetting = true
        }
    }
}
