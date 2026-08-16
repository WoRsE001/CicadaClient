package cicada.client.feature.module.modules.player

import cicada.client.event.Event
import cicada.client.event.events.EventTick
import cicada.client.feature.module.ClientModule
import cicada.client.feature.module.ModuleCategory
import cicada.client.feature.module.modules.player.ModuleInventoryCleaner.ItemForSort
import cicada.client.utils.client.gameMode
import cicada.client.utils.client.mc
import cicada.client.utils.client.player
import net.minecraft.client.gui.screens.inventory.ContainerScreen
import net.minecraft.world.inventory.ContainerInput

object ModuleChestStealer : ClientModule("ChestStealer", ModuleCategory.PLAYER) {
    private val startDelay by int("StartDelay", 0, 0..10)
    private val delay by int("Delay", 5, 0..10)
    private val close by boolean("Close", true)
    private val closeDelay by int("CloseDelay", 0, 0..10).visible { close }
    private val invManager by boolean("InvManager", true)

    private var tick = 0

    override fun onDisable() {
        tick = 0
    }

    override fun onEvent(event: Event) {
        if (event !is EventTick.Pre) return

        val screen = mc.screen
        if (screen is ContainerScreen) {
            if (tick > 0) {
                tick--
            } else {
                val menu = screen.menu
                for (slot in menu.slots) {
                    val itemStack = slot.item
                    if (itemStack.isEmpty || slot.container === player.inventory) continue
                    if (invManager) {
                        var switched = false
                        for ((index, sortedSlot) in ModuleInventoryCleaner.sortedSlots.withIndex()) {
                            val itemForSort = sortedSlot.inner as ItemForSort
                            if (itemForSort.`is`(itemStack)) {
                                if (itemForSort.compare(itemStack, player.inventory.getItem(index))) {
                                    switched = true
                                    gameMode.handleContainerInput(
                                        menu.containerId, slot.index, index, ContainerInput.SWAP, player
                                    )
                                    break
                                }
                            }
                        }
                        if (!switched) {
                            gameMode.handleContainerInput(
                                menu.containerId, slot.index, 0, ContainerInput.QUICK_MOVE, player
                            )
                        }
                    } else {
                        gameMode.handleContainerInput(
                            menu.containerId, slot.index, 0, ContainerInput.QUICK_MOVE, player
                        )
                    }
                    tick = delay
                    if (tick > 0) break
                }
            }
        } else {
            tick = startDelay
        }
    }
}