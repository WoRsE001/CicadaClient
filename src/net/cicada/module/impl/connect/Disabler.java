package net.cicada.module.impl.connect;

import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.cicada.event.api.Event;
import net.cicada.event.impl.PacketEvent;
import net.cicada.module.api.Category;
import net.cicada.module.api.Module;
import net.cicada.module.api.ModuleInfo;
import net.cicada.module.setting.impl.BooleanSetting;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;

@ModuleInfo(name = "Disabler", category = Category.Connect)
public class Disabler extends Module {
    BooleanSetting startDestroyBlock = new BooleanSetting("StartDestroyBlock", false, () -> true, this);
    BooleanSetting abortDestroyBlock = new BooleanSetting("AbortDestroyBlock", false, () -> true, this);
    BooleanSetting stopDestroyBlock = new BooleanSetting("StopDestroyBlock", false, () -> true, this);
    BooleanSetting matrixTimer = new BooleanSetting("MatrixTimer", false, () -> true, this);

    @Override
    public void listen(Event event) {
        if (event instanceof PacketEvent p) {
            if (startDestroyBlock.isValue() && p.getPacket() instanceof C07PacketPlayerDigging stDig && stDig.getStatus() == C07PacketPlayerDigging.Action.START_DESTROY_BLOCK) event.cancel();
            if (abortDestroyBlock.isValue() && p.getPacket() instanceof C07PacketPlayerDigging stDig && stDig.getStatus() == C07PacketPlayerDigging.Action.ABORT_DESTROY_BLOCK) event.cancel();
            if (stopDestroyBlock.isValue() && p.getPacket() instanceof C07PacketPlayerDigging stDig && stDig.getStatus() == C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK) event.cancel();

            if (this.matrixTimer.isValue()) {
                if (p.getPacket() instanceof C0FPacketConfirmTransaction) event.cancel();
            }
        }
    }
}
