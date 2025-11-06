package net.cicada.module.impl.movement;

import net.minecraft.block.BlockAir;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.cicada.event.api.Event;
import net.cicada.event.impl.JumpEvent;
import net.cicada.event.impl.UpdateEvent;
import net.cicada.module.api.Category;
import net.cicada.module.api.Module;
import net.cicada.module.api.ModuleInfo;
import org.lwjgl.input.Keyboard;

@ModuleInfo(name = "HighJump", category = Category.Movement, key = Keyboard.KEY_H)
public class HighJump extends Module {
    boolean sneak = false;

    @Override
    protected void onDisable() {
        if (this.sneak) {
            this.sneak = false;
            mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
        }
        super.onDisable();
    }

    @Override
    public void listen(Event event) {
        if (event instanceof UpdateEvent) {
            if (mc.theWorld.getBlockState(mc.thePlayer.getPosition().add(0, -1, 0)).getBlock() instanceof BlockAir) {
                mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
                sneak = true;
            } else if (sneak) {
                mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
                sneak = false;
            }
        }

        if (event instanceof JumpEvent e) {
            e.setMotion(0.47F);
        }
    }
}
