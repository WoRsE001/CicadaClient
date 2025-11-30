package net.cicada.command.impl;

import net.cicada.command.api.Command;
import net.cicada.command.api.CommandInfo;
import net.cicada.utility.LoggerUtil;
import net.minecraft.network.play.client.C03PacketPlayer;

@CommandInfo(name = "Teleport", aliases = {"tp"}, usage = ".tp <x> <y> <z> <count>")
public class Teleport extends Command {
    @Override
    public void execute(String[] args) {
        if (args.length == 5) {
            for (int i = 0; i < Integer.parseInt(args[4]); i++) {
                mc.thePlayer.setPosition(Double.parseDouble(args[1]), Double.parseDouble(args[2]), Double.parseDouble(args[3]));
                LoggerUtil.display("f");
                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
            }
            //mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Double.parseDouble(args[1]), Double.parseDouble(args[2]), Double.parseDouble(args[3]), true));
        }
    }
}
