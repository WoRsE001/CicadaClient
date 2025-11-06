package net.cicada.module.impl.player;

import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.potion.PotionEffect;
import net.cicada.event.api.Event;
import net.cicada.event.impl.MotionEvent;
import net.cicada.module.api.Category;
import net.cicada.module.api.Module;
import net.cicada.module.api.ModuleInfo;

import java.util.ArrayList;
import java.util.List;

@ModuleInfo(name = "AutoPotion", category = Category.Player)
public class AutoPotion extends Module {
    private int prevSlot;

    @Override
    public void listen(Event event) {
        if (event instanceof MotionEvent e) {
            int slot = getNeedPotion().isEmpty() ? -1 : getPotionSlot(getNeedPotion().get(0));
            if (slot != -1) {
                if (e.getPriority() == Event.Priority.Low) {
                    prevSlot = mc.thePlayer.inventory.currentItem;
                    e.setRotationPitch(90);
                    if (prevSlot != slot)
                        mc.getNetHandler().getNetworkManager().sendPacket(new C09PacketHeldItemChange(slot));
                }

                if (e.getPriority() == Event.Priority.High && prevSlot != -1) {
                    mc.thePlayer.inventory.currentItem = slot;
                    mc.rightClickMouse();
                    mc.thePlayer.inventory.currentItem = prevSlot;
                    if (prevSlot != slot)
                        mc.getNetHandler().getNetworkManager().sendPacket(new C09PacketHeldItemChange(prevSlot));
                    prevSlot = -1;
                }
            }
        }
    }

    private List<String> getNeedPotion() {
        List<String> needPotion = new ArrayList<>();
        needPotion.add("damageBoost");
        needPotion.add("moveSpeed");
        needPotion.add("fireResistance");
        for (PotionEffect activePotionEffect : mc.thePlayer.getActivePotionEffects()) {
            if (activePotionEffect.getEffectName().contains("damageBoost")) needPotion.remove("damageBoost");
            if (activePotionEffect.getEffectName().contains("moveSpeed")) needPotion.remove("moveSpeed");
            if (activePotionEffect.getEffectName().contains("fireResistance")) needPotion.remove("fireResistance");
        }
        return needPotion;
    }

    private int getPotionSlot(String effectName) {
        for (int i = 0; i < 9; i++) {
            ItemStack itemStack = mc.thePlayer.inventory.getStackInSlot(i);
            if (itemStack == null || !(itemStack.getItem() instanceof ItemPotion p)) continue;
            for (PotionEffect effect : p.getEffects(itemStack)) {
                if (!effect.getEffectName().contains(effectName)) continue;
                return i;
            }
        }
        return -1;
    }
}
