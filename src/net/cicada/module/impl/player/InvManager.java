package net.cicada.module.impl.player;

import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.cicada.event.api.Event;
import net.cicada.event.impl.TickEvent;
import net.cicada.module.api.Category;
import net.cicada.module.api.Module;
import net.cicada.module.api.ModuleInfo;
import net.cicada.utility.InvUtil;

@ModuleInfo(name = "InvManager", category = Category.Player)
public class InvManager extends Module {
    int[] sortHotbar = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8};

    @Override
    public void listen(Event event) {
        if (event instanceof TickEvent && mc.currentScreen instanceof GuiInventory) {
            if (this.searchAndSort()) return;
            if (drop()) return;
        }
    }

    private boolean searchAndSort() {
        int[] slotCandidates = new int[]{-1, -1, -1, -1, -1, -1, -1, -1, -1};

        for (int i = 0; i < 36; i++) {
            ItemStack itemStack = getItemStack(i);
            if (itemStack == null) continue;
            Item item = itemStack.getItem();

            if (item instanceof ItemSword) {
                if (slotCandidates[0] == -1) slotCandidates[0] = i;
                else if (InvUtil.getDamage(itemStack) > InvUtil.getDamage(getItemStack(slotCandidates[0]))) slotCandidates[0] = i;
            } if (item instanceof ItemFishingRod) {
                if (slotCandidates[1] == -1) slotCandidates[1] = i;
            } if (item instanceof ItemBow) {
                if (slotCandidates[2] == -1) slotCandidates[2] = i;
            } if (item == Items.water_bucket) {
                if (slotCandidates[3] == -1) slotCandidates[3] = i;
            } if (item instanceof ItemFood && !(item instanceof ItemAppleGold)) {
                if (slotCandidates[4] == -1) slotCandidates[4] = i;
            } if (item instanceof ItemAppleGold) {
                if (slotCandidates[5] == -1) slotCandidates[5] = i;
            } if (item instanceof ItemEnderPearl) {
                if (slotCandidates[6] == -1) slotCandidates[6] = i;
            } if (item instanceof ItemEgg) {
                if (slotCandidates[7] == -1) slotCandidates[7] = i;
            } if (item instanceof ItemBlock) {
                if (slotCandidates[8] == -1) slotCandidates[8] = i;
            }
        }

        if (slotCandidates[0] != -1 && sortHotbar[0] != -1 && slotCandidates[0] != sortHotbar[0]) {
            InvUtil.moveItem(slotCandidates[0], sortHotbar[0] - 36);
            return true;
        } if (slotCandidates[1] != -1 && sortHotbar[1] != -1 && slotCandidates[1] != sortHotbar[1]) {
            InvUtil.moveItem(slotCandidates[1], sortHotbar[1] - 36);
            return true;
        } if (slotCandidates[2] != -1 && sortHotbar[2] != -1 && slotCandidates[2] != sortHotbar[2]) {
            InvUtil.moveItem(slotCandidates[2], sortHotbar[2] - 36);
            return true;
        } if (slotCandidates[3] != -1 && sortHotbar[3] != -1 && slotCandidates[3] != sortHotbar[3]) {
            InvUtil.moveItem(slotCandidates[3], sortHotbar[3] - 36);
            return true;
        } if (slotCandidates[4] != -1 && sortHotbar[4] != -1 && slotCandidates[4] != sortHotbar[4]) {
            InvUtil.moveItem(slotCandidates[4], sortHotbar[4] - 36);
            return true;
        } if (slotCandidates[5] != -1 && sortHotbar[5] != -1 && slotCandidates[5] != sortHotbar[5]) {
            InvUtil.moveItem(slotCandidates[5], sortHotbar[5] - 36);
            return true;
        } if (slotCandidates[6] != -1 && sortHotbar[6] != -1 && slotCandidates[6] != sortHotbar[6]) {
            InvUtil.moveItem(slotCandidates[6], sortHotbar[6] - 36);
            return true;
        } if (slotCandidates[7] != -1 && sortHotbar[7] != -1 && slotCandidates[7] != sortHotbar[7]) {
            InvUtil.moveItem(slotCandidates[7], sortHotbar[7] - 36);
            return true;
        } if (slotCandidates[8] != -1 && sortHotbar[8] != -1 && slotCandidates[8] != sortHotbar[8]) {
            InvUtil.moveItem(slotCandidates[8], sortHotbar[8] - 36);
            return true;
        }
        return false;
    }

    private boolean drop() {
        for (int i = 9; i < 36; i++) {
            ItemStack itemStack = getItemStack(i);
            if (itemStack == null) continue;
            Item item = itemStack.getItem();

            if (item instanceof ItemSword || item instanceof ItemFishingRod || item instanceof ItemBow || item == Items.water_bucket) {
                InvUtil.dropItem(i);
                return true;
            }
        }
        return false;
    }

    private ItemStack getItemStack(int slot) {
        return mc.thePlayer.inventory.getStackInSlot(slot);
    }
}
