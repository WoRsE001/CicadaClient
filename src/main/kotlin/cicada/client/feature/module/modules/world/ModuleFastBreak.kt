package cicada.client.feature.module.modules.world

import cicada.client.event.Event
import cicada.client.event.events.EventTick
import cicada.client.feature.module.ClientModule
import cicada.client.feature.module.ModuleCategory
import cicada.client.utils.client.gameMode
import cicada.client.utils.client.player
import cicada.utility.player.inventory.isMiningTool

object ModuleFastBreak : ClientModule("FastBreak", ModuleCategory.WORLD) {
    private val onlyTool by boolean("OnlyTool", true)
    val breakDamage by float("BreakDamage", 0.5f, 0f..1f)
    val breakDelay by int("BreakDelay", 5, 0..5)

    fun shouldFastBreak() = toggled && (!onlyTool || player.mainHandItem.isMiningTool)

    override fun onEvent(event: Event) {
        if (event is EventTick.Pre) {
            if (shouldFastBreak()) {
                if (gameMode.destroyProgress > breakDamage) {
                    gameMode.destroyProgress = 1f
                }
            }
        }
    }
}