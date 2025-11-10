package net.cicada.module.impl.movement;

import net.minecraft.block.BlockAir;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.cicada.event.api.Event;
import net.cicada.event.impl.MovementInputEvent;
import net.cicada.event.impl.TickEvent;
import net.cicada.event.impl.UpdateEvent;
import net.cicada.module.api.Category;
import net.cicada.module.api.Module;
import net.cicada.module.api.ModuleInfo;
import net.cicada.module.setting.impl.BooleanSetting;
import net.cicada.module.setting.impl.ListSetting;
import net.cicada.module.setting.impl.NumberSetting;
import net.cicada.utility.MovementUtil;
import org.lwjgl.input.Keyboard;

import java.util.List;

@ModuleInfo(name = "Speed", category = Category.Movement)
public class Speed extends Module {
    ListSetting mode = new ListSetting("Mode", "Motion", List.of("Motion", "Intave 1.13+", "Polar", "PolarTest"), () -> true, this);
    NumberSetting speed = new NumberSetting("Speed", 1, 0, 5, 0.01, () -> mode.getValue().equals("Motion"), this);
    BooleanSetting autoJump = new BooleanSetting("AutoJump", true, () -> true, this);

    boolean sneak = false;

    @Override
    protected void onDisable() {
        mc.timer.setTimer(1);
        if (this.sneak) {
            this.sneak = false;
            mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
        }
        super.onDisable();
    }

    @Override
    public void listen(Event event) {
        if (event instanceof TickEvent) {
            if (mode.getValue().equals("Motion")) {
                MovementUtil.strafe(speed.getValue(), 1);
            }
        }

        if (event instanceof UpdateEvent) {
            if (mode.getValue().equals("Intave 1.13+")) {
                if (!MovementUtil.isMoving() || mc.thePlayer.onGround || mc.thePlayer.hurtTime != 0) return;
                if (mc.theWorld.getBlockState(mc.thePlayer.getPosition().add(0, -1, 0)).getBlock() instanceof BlockAir) {
                    mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
                    mc.thePlayer.motionX *= 1.043;
                    mc.thePlayer.motionZ *= 1.043;
                    sneak = true;
                } else if (sneak) {
                    mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
                    sneak = false;
                }
            }

            if (mode.getValue().equals("Polar")) {
                if (mc.thePlayer.onGround) {
                    mc.thePlayer.motionX *= Math.random() * 0.02 + 1;
                    mc.thePlayer.motionZ *= Math.random() * 0.02 + 1;
                    mc.timer.setTimer((float) Math.random() * 0.05F + 1);
                } else {
                    mc.timer.setTimer(1);
                }
            }

            if (mode.getValue().equals("PolarTest")) {
                mc.thePlayer.motionX *= Math.random() * 0.028 + 1;
                mc.thePlayer.motionZ *= Math.random() * 0.028 + 1;
                if (mc.thePlayer.onGround) {
                    mc.timer.setTimer((float) Math.random() * 0.09F + 1)
                } else {
                    mc.timer.setTimer(1)
                }
            }
        }

        if (event instanceof MovementInputEvent e) {
            if (MovementUtil.isMoving() && autoJump.isValue()) e.setJump(true);
        }
    }
}
