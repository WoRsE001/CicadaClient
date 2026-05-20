package cicada.client.feature.module.modules.player

import cicada.client.event.Event
import cicada.client.event.impl.GameLoopEvent
import cicada.client.feature.module.ModuleCategory
import cicada.client.feature.module.ClientModule
import cicada.client.config.types.ChoiceValue
import cicada.client.utils.client.gameMode
import cicada.client.utils.client.mc
import cicada.client.utils.client.player
import cicada.utility.player.inventory.attackDamage
import cicada.utility.player.inventory.getEnchantment
import cicada.utility.player.inventory.isFood
import cicada.utility.player.inventory.isSword
import net.minecraft.client.gui.screens.inventory.InventoryScreen
import net.minecraft.world.inventory.ContainerInput
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.ProjectileItem
import net.minecraft.world.item.enchantment.Enchantments

object ModuleInventoryCleaner : ClientModule("InventoryManager", ModuleCategory.PLAYER) {
    private val sortedSlots = List(9) { i ->
        choice("Slot $i").apply {
            choice(ItemForSort("Sword", { it.isSword }, { a, b -> a.attackDamage > b.attackDamage }))
            choice(ItemForSort("Fishing rod", { it.`is`(Items.FISHING_ROD) }, { a, b -> false }))
            choice(ItemForSort("Bow", { it.`is`(Items.BOW) }, { a, b -> a.getEnchantment(Enchantments.PUNCH) >  b.getEnchantment(Enchantments.PUNCH) }))
            choice(ItemForSort("Water bucket", { it.`is`(Items.WATER_BUCKET) }, { a, b -> false }))
            choice(ItemForSort("Golden apples", { it.`is`(Items.GOLDEN_APPLE) }, { a, b -> a.count > b.count }))
            choice(ItemForSort("Food", { it.isFood && !it.`is`(Items.GOLDEN_APPLE)}, { a, b -> a.count > b.count }))
            choice(ItemForSort("Ender pearls", { it.`is`(Items.ENDER_PEARL) }, { a, b -> a.count > b.count }))
            choice(ItemForSort("Projectiles", { it.item is ProjectileItem }, { a, b -> a.count > b.count }))
            choice(ItemForSort("Blocks", { it.item is BlockItem }, { a, b -> a.count > b.count }))
        }
    }

    override fun onEvent(event: Event) {
        if (mc.screen !is InventoryScreen) return
        if (event !is GameLoopEvent.Pre) return

        sort()
    }

    private fun sort() {
        for ((index, sortedSlot) in sortedSlots.withIndex()) {
            val sortedItem = sortedSlot.inner as? ItemForSort ?: continue
            val yaHZkakNAZVAT = player.inventoryMenu.slots[index + 36]
            var slotForSort: Slot? = if (sortedItem.`is`(yaHZkakNAZVAT.item)) yaHZkakNAZVAT else null
            for (slot in player.inventoryMenu.slots) {
                val itemStack = slot.item
                if (sortedItem.`is`(itemStack)) {
                    if (slotForSort == null || sortedItem.compare(itemStack, slotForSort.item))
                        slotForSort = slot
                }
            }

            if (slotForSort != null) {
                gameMode.handleContainerInput(
                    player.inventoryMenu.containerId,
                    slotForSort.index,
                    index,
                    ContainerInput.SWAP,
                    player
                )
            }
        }
    }

    private fun clear() {
        val itemsToClear = listOf(
            ItemForSort("Sword", { it.isSword }, { a, b -> a.attackDamage > b.attackDamage }),
            ItemForSort("Fishing rod", { it.`is`(Items.FISHING_ROD) }, { a, b -> false }),
            ItemForSort("Bow", { it.`is`(Items.BOW) }, { a, b -> a.getEnchantment(Enchantments.PUNCH) >  b.getEnchantment(Enchantments.PUNCH) }),
            ItemForSort("Water bucket", { it.`is`(Items.WATER_BUCKET) }, { a, b -> false }),
        )

        for (itemToClear in itemsToClear) {

        }
    }

    private class ItemForSort(
        name: String,
        val `is`: (ItemStack) -> Boolean,
        val compare: (ItemStack, ItemStack) -> Boolean
    ) : ChoiceValue.Choice(name)
}