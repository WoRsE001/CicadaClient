package cicada.client.feature.module.modules.player

import cicada.client.event.Event
import cicada.client.event.impl.TickEvent
import cicada.client.feature.module.ModuleCategory
import cicada.client.feature.module.ClientModule
import cicada.client.utils.client.gameMode
import cicada.client.utils.client.player
import cicada.utility.player.inventory.isTotemOfUndying
import cicada.utility.player.inventory.slotBy
import net.minecraft.world.inventory.ContainerInput
import net.minecraft.world.item.ItemStack

// SCWGxD regrets everything he did. 20.04.2026 8:08.
object ModuleAutoSwap : ClientModule("AutoSwap", ModuleCategory.PLAYER) {
    private var daynItem: ItemStack? = null
    private var swaped = false

    override fun onEvent(event: Event) {
        if (event is TickEvent.Pre) {
            val inventoryMenu = player.inventoryMenu
            
            val totemSlot = inventoryMenu.slotBy { it.isTotemOfUndying }

            if (player.health <= 3 && totemSlot != null && !swaped) {
                val offhandItem = player.offhandItem
                if (offhandItem != null) {
                    if (offhandItem.isTotemOfUndying)
                        return
                    else 
                        daynItem = offhandItem
                }
                gameMode.handleContainerInput(player.inventoryMenu.containerId, totemSlot.index, 40, ContainerInput.SWAP, player)
                swaped = true
            }
            
            if (player.health > 3 && swaped) {
                val idk = inventoryMenu.slotBy { it == daynItem }

                if (daynItem != null && idk != null) {
                    gameMode.handleContainerInput(player.inventoryMenu.containerId, idk.index, 40, ContainerInput.SWAP, player)
                }
                swaped = false
            }
        }
    }
}