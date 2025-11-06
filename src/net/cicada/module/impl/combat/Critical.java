package net.cicada.module.impl.combat;

import net.cicada.event.api.Event;
import net.cicada.event.impl.AttackEvent;
import net.cicada.module.api.Category;
import net.cicada.module.api.Module;
import net.cicada.module.api.ModuleInfo;
import net.cicada.module.setting.impl.ListSetting;
import net.minecraft.network.play.client.C03PacketPlayer;

import java.util.List;

@ModuleInfo(name = "Critical", category = Category.Combat)
public class Critical extends Module {
    ListSetting mode = new ListSetting("Mode", "Packet", List.of("Packet", "NCP"), () -> true, this);

    @Override
    public void listen(Event event) {
        if (event instanceof AttackEvent e) {
            if (mode.is("Packet")) {
                if (e.getPriority() == Event.Priority.Low) {
                    mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.0001, mc.thePlayer.posZ, false));
                    mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(false));
                }
            }

            if (mode.is("NCP")) {
                if (e.getPriority() == Event.Priority.Low) {
                    mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.11, mc.thePlayer.posZ, false));
                    mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.1100013579, mc.thePlayer.posZ, false));
                    mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.0000013579, mc.thePlayer.posZ, false));
                }
            }
        }
    }
}
