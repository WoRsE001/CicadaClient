package net.cicada.module.impl.misc;

import net.minecraft.network.play.server.S01PacketJoinGame;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.cicada.event.api.Event;
import net.cicada.event.impl.PacketEvent;
import net.cicada.module.api.Category;
import net.cicada.module.api.Module;
import net.cicada.module.api.ModuleInfo;
import net.cicada.utility.LoggerUtil;

@ModuleInfo(name = "FlagDetector", category = Category.Misc, state = true)
public class FlagDetector extends Module {
    int count = 0;

    @Override
    public void listen(Event event) {
        if (event instanceof PacketEvent e) {
            if (e.getPacket() instanceof S08PacketPlayerPosLook) {
                count++;
                LoggerUtil.display("§4Flag detected [" + count + "]!");
            } else if (e.getPacket() instanceof S01PacketJoinGame) {
                count = 0;
            }
        }
    }
}
