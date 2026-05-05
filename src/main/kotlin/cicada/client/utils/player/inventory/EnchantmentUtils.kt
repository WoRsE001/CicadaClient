package cicada.utility.player.inventory

import net.minecraft.core.Holder
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.EnchantmentHelper
import net.minecraft.world.item.enchantment.ItemEnchantments

// Blood! It's everywhere. SCWxD killed you on 17.03.2026 at 5:45.
private val ItemStack.componentTypeForEnchantment
    inline get() = EnchantmentHelper.getComponentType(this)

fun ItemStack.removeEnchantment(enchantment: Holder<Enchantment>) {
    EnchantmentHelper.updateEnchantments(this) { it.set(enchantment, 0) }
}

fun ItemStack.clearEnchantments() =
    EnchantmentHelper.setEnchantments(this, ItemEnchantments.EMPTY)

fun ItemStack?.getEnchantmentCount(): Int =
    this?.get(componentTypeForEnchantment)?.size() ?: 0

fun ItemStack?.getEnchantment(enchantment: ResourceKey<Enchantment>): Int {
    if (this == null) return 0
    val enchantmentEntry = enchantment.toRegistryEntryOrNull() ?: return 0
    return EnchantmentHelper.getItemEnchantmentLevel(enchantmentEntry, this)
}