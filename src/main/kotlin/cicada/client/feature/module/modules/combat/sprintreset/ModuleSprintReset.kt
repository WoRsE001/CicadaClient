package cicada.client.feature.module.modules.combat.sprintreset

import cicada.client.event.Event
import cicada.client.event.impl.TickEvent
import cicada.client.feature.module.ClientModule
import cicada.client.feature.module.ModuleCategory
import cicada.client.feature.module.modules.combat.attackaura.ModuleAttackAura
import cicada.client.feature.module.modules.combat.sprintreset.mode.*
import cicada.client.utils.client.player
import cicada.client.utils.math.minus
import cicada.client.utils.player.LocalPlayerStateTracker
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

    private var shouldReset = false
    private var stage = Stage.START

    override fun onEvent(event: Event) {
        val currentSubMode = mode.inner

        if (currentSubMode !is SprintResetMode)
            return

        if (!LocalPlayerStateTracker.serverSprint && player.isSprinting) {
            shouldReset = true
        }

        if (shouldReset) {
            if (stage == Stage.START) if (currentSubMode.startReset(event)) stage = Stage.RESET
            if (stage == Stage.RESET) if (currentSubMode.reset(event)) stage = Stage.STOP
            if (stage == Stage.STOP) if (currentSubMode.stopReset(event)) {
                stage = Stage.START
                shouldReset = false
            }
        }
    }

    private enum class Stage { START, RESET, STOP }
}