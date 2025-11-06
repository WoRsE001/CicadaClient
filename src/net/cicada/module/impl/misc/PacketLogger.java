package net.cicada.module.impl.misc;

import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.cicada.event.api.Event;
import net.cicada.event.impl.PacketEvent;
import net.cicada.module.api.Category;
import net.cicada.module.api.Module;
import net.cicada.module.api.ModuleInfo;
import net.cicada.utility.LoggerUtil;

@ModuleInfo(name = "PacketLogger", category = Category.Misc)
public class PacketLogger extends Module {
    @Override
    public void listen(Event event) {
        if (event instanceof PacketEvent e) {
            if (!(e.getPacket() instanceof S08PacketPlayerPosLook || e.getPacket() instanceof C03PacketPlayer || e.getPacket() instanceof C07PacketPlayerDigging)) return;
            LoggerUtil.display(e.getPacket().toString());
        }
    }
}
