package net.cicada.utility;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;

public class InvUtil implements Access {
    public static void moveItem(int from, int in) {
        mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot(from), slot(in), 2, mc.thePlayer);
    }

    public static void stealItem(int slot) {
        mc.playerController.windowClick(mc.thePlayer.openContainer.windowId, slot, 0, 1, mc.thePlayer);
    }

    public static void dropItem(int slot) {
        mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot(slot), 1, 4, mc.thePlayer);
    }

    public static float getDamage(ItemStack stack) {
        ItemSword sword = (ItemSword) stack.getItem();
        int level = EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack);
        return (float) (sword.getDamageVsEntity() + level * 1.25);
    }

    private static int slot(final int slot) {
        if (slot >= 36) {
            return 8 - (slot - 36);
        }

        if (slot < 9) {
            return slot + 36;
        }

        return slot;
    }
}
