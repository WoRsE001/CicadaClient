package net.cicada.module.impl.misc;

import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.cicada.event.api.Event;
import net.cicada.event.impl.AttackEvent;
import net.cicada.module.api.Category;
import net.cicada.module.api.Module;
import net.cicada.module.api.ModuleInfo;

@ModuleInfo(name = "SilentMurder", category = Category.Misc)
public class SilentMurder extends Module {
    int swordSlot, lastSlot;

    @Override
    public void listen(Event event) {
        if (event instanceof AttackEvent e) {
            swordSlot = findSword();
            if (swordSlot != -1) {
                if (e.getPriority() == Event.Priority.Low) {
                    lastSlot = mc.thePlayer.inventory.currentItem;
                    mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(swordSlot));
                } else if (e.getPriority() == Event.Priority.High) {
                    mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(lastSlot));
                }
            }
        }
    }

    private int findSword() {
        for (int i = 0; i < 9; i++) {
            ItemStack itemStack = mc.thePlayer.inventory.getStackInSlot(i);
            if (itemStack != null && itemStack.getItem() instanceof ItemSword) return i;
        }
        return -1;
    }
}
