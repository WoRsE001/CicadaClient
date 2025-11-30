package net.cicada.utility.Player;

import lombok.experimental.UtilityClass;
import net.cicada.utility.Access;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;

@UtilityClass
public class InvUtil implements Access {
    public void moveItem(int from, int in) {
        mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, from, in, 2, mc.thePlayer);
    }

    public void stealItem(int slot) {
        mc.playerController.windowClick(mc.thePlayer.openContainer.windowId, slot, 0, 1, mc.thePlayer);
    }

    public void dropItem(int slot) {
        mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot, 1, 4, mc.thePlayer);
    }

    public float getSwordDamage(ItemStack stack) {
        int level = EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack);
        return (float) (((ItemSword) stack.getItem()).getDamageVsEntity() + level * 1.25);
    }

    public double getDamageReduceAmount(final ItemStack itemStack) {
        double damageReduceAmount = 0.0;
        if (itemStack.getItem() instanceof ItemArmor) {
            damageReduceAmount += ((ItemArmor)itemStack.getItem()).damageReduceAmount + (6 + EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, itemStack) * EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, itemStack)) / 3.0f;
            damageReduceAmount += EnchantmentHelper.getEnchantmentLevel(Enchantment.blastProtection.effectId, itemStack) / 11.0;
            damageReduceAmount += EnchantmentHelper.getEnchantmentLevel(Enchantment.projectileProtection.effectId, itemStack) / 11.0;
            damageReduceAmount += EnchantmentHelper.getEnchantmentLevel(Enchantment.fireProtection.effectId, itemStack) / 11.0;
            damageReduceAmount += EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, itemStack) / 11.0;
            damageReduceAmount += EnchantmentHelper.getEnchantmentLevel(Enchantment.thorns.effectId, itemStack) / 11.0;
            damageReduceAmount += EnchantmentHelper.getEnchantmentLevel(Enchantment.featherFalling.effectId, itemStack) / 11.0;
            if (((ItemArmor)itemStack.getItem()).armorType == 0 && ((ItemArmor)itemStack.getItem()).getArmorMaterial() == ItemArmor.ArmorMaterial.GOLD) {
                damageReduceAmount -= 0.01;
            }
        }
        return damageReduceAmount;
    }
}
