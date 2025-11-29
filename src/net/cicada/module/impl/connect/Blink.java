package net.cicada.module.impl.connect;

import net.minecraft.network.Packet;
import net.cicada.event.api.Event;
import net.cicada.event.impl.PacketEvent;
import net.cicada.module.api.Category;
import net.cicada.module.api.Module;
import net.cicada.module.api.ModuleInfo;

import java.util.ArrayList;
import java.util.List;

@ModuleInfo(name = "Blink", category = Category.Connect)
public class Blink extends Module {
    List<Packet<?>> sendQueue = new ArrayList<>();

    @Override
    protected void onDisable() {
        if (!sendQueue.isEmpty()) {
            sendQueue.forEach(packet -> mc.thePlayer.sendQueue.addToSendQueue(packet));
            sendQueue.clear();
        }
        super.onDisable();
    }

    @Override
    public void listen(Event event) {
        if (event instanceof PacketEvent e && e.getType() == PacketEvent.Type.Send) {
            sendQueue.add(e.getPacket());
            e.cancel();
        }
    }
}
