package net.cicada.utility.Player;

import lombok.experimental.UtilityClass;
import net.cicada.utility.Access;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.*;

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

    public boolean isFull() {
        for (int i = 0; i < mc.thePlayer.inventory.getSizeInventory() - 4; i++) {
            if (mc.thePlayer.inventory.getStackInSlot(i) == null) return false;
        }
        return true;
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

    public float getMineSpeed(ItemStack stack) {
        Item item = stack.getItem();
        int level = EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, stack);
        switch (level) {
            case 1: level = 30; break;
            case 2: level = 69; break;
            case 3: level = 120; break;
            case 4: level = 186; break;
            case 5: level = 271; break;
            default: level = 0; break;
        }

        if (item instanceof ItemTool) {
            switch (item) {
                case ItemPickaxe itemPickaxe -> {
                    return itemPickaxe.getToolMaterial().getEfficiencyOnProperMaterial() + level;
                }
                case ItemSpade itemSpade -> {
                    return itemSpade.getToolMaterial().getEfficiencyOnProperMaterial() + level;
                }
                case ItemAxe itemAxe -> {
                    return itemAxe.getToolMaterial().getEfficiencyOnProperMaterial() + level;
                }
                default -> {
                }
            }
        }

        return 0.0F;
    }
}
