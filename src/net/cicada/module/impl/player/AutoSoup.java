package net.cicada.module.impl.player;

import net.cicada.event.api.Event;
import net.cicada.event.impl.UpdateEvent;
import net.cicada.module.api.Category;
import net.cicada.module.api.Module;
import net.cicada.module.api.ModuleInfo;
import net.cicada.module.setting.impl.NumberSetting;
import net.minecraft.item.ItemSoup;
import net.minecraft.item.ItemStack;

@ModuleInfo(name = "AutoSoup", category = Category.Player)
public class AutoSoup extends Module {
    NumberSetting thresholdHealth = new NumberSetting("ThresholdHealth", 19, 1, 20, 1, () -> true, this);

    int lastSlot;

    @Override
    public void listen(Event event) {
        if (event instanceof UpdateEvent) {
            if (mc.thePlayer.getHealth() <= thresholdHealth.getValue()) {
                int bestSlot = findSoup();
                if (bestSlot != -1) {
                    if (lastSlot == -1) lastSlot = mc.thePlayer.inventory.currentItem;
                    mc.thePlayer.inventory.currentItem = bestSlot;
                    mc.rightClickMouse();
                    mc.playerController.sendPacketDropItem(mc.thePlayer.inventory.getStackInSlot(bestSlot));
                }
            } else if (lastSlot != -1) {
                mc.thePlayer.inventory.currentItem = lastSlot;
                lastSlot = -1;
            }
        }
    }

    private int findSoup() {
        for (int i = 0; i < 9; i++) {
            ItemStack itemStack = mc.thePlayer.inventory.getStackInSlot(i);
            if (itemStack == null) continue;
            if (itemStack.getItem() instanceof ItemSoup) return i;
        }

        return -1;
    }
}
