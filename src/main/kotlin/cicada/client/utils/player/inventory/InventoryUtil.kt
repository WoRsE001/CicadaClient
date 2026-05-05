package cicada.utility.player.inventory

import net.minecraft.world.inventory.InventoryMenu
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack

// Blood! It's everywhere. SCWxD killed you on 17.03.2026 at 14:39.
fun InventoryMenu.slotBy(condition: (ItemStack) -> Boolean): Slot? {
    for (i in slots) { if (condition(i.item)) return i }
    return null
}