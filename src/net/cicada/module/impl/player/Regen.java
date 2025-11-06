package net.cicada.module.impl.player;

import net.minecraft.network.play.client.C03PacketPlayer;
import net.cicada.event.api.Event;
import net.cicada.event.impl.TickEvent;
import net.cicada.module.api.Category;
import net.cicada.module.api.Module;
import net.cicada.module.api.ModuleInfo;
import net.cicada.module.setting.impl.NumberSetting;

@ModuleInfo(name = "Regen", category = Category.Player)
public class Regen extends Module {
    NumberSetting PPS = new NumberSetting("Packet per second", 5, 0, 100, 1, () -> true, this);
    NumberSetting minHealth = new NumberSetting("Min health", 20, 0, 20, 1, () -> true, this);

    @Override
    public void listen(Event event) {
        if (event instanceof TickEvent && mc.thePlayer.getHealth() < minHealth.getValue() && mc.thePlayer.getFoodStats().getFoodLevel() > 17) {
            for (int i = 0; i < PPS.getValue(); i++) {
                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer());
            }
        }
    }
}
