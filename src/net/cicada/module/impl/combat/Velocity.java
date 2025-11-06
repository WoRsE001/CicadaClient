package net.cicada.module.impl.combat;

import net.cicada.event.impl.MovementInputEvent;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.cicada.event.api.Event;
import net.cicada.event.impl.AttackEvent;
import net.cicada.event.impl.MovementEvent;
import net.cicada.event.impl.PacketEvent;
import net.cicada.module.api.Category;
import net.cicada.module.api.Module;
import net.cicada.module.api.ModuleInfo;
import net.cicada.module.setting.impl.ListSetting;
import net.cicada.module.setting.impl.NumberSetting;

import java.util.List;

@ModuleInfo(name = "Velocity", category = Category.Combat, state = true)
public class Velocity extends Module {
    ListSetting mode = new ListSetting("Mode", "Reduce", List.of("Motion", "JumpReset", "Reduce"), () -> true, this);
    NumberSetting motionXZ = new NumberSetting("MotionXZ", 0, 0, 1, 0.01, () -> mode.getValue().equals("Motion"), this);
    NumberSetting motionY = new NumberSetting("MotionY", 0, 0, 1, 0.01, () -> mode.getValue().equals("Motion"), this);
    NumberSetting chance = new NumberSetting("Chance", 50, 0, 100, 1, () -> mode.getValue().equals("JumpReset"), this);
    NumberSetting reduceOnHit = new NumberSetting("ReduceOnHit", 0.6, 0, 1, 0.01, () -> mode.getValue().equals("Reduce"), this);

    @Override
    public void listen(Event event) {
        if (event instanceof MovementInputEvent e) {
            if (mode.getValue().equals("JumpReset")) {
                if (mc.thePlayer.hurtTime > 8 && mc.thePlayer.onGround && chance.getValue() < Math.random() * 100) {
                    e.setJump(true);
                }
            }
        }

        if (event instanceof PacketEvent e) {
            if (mode.getValue().equals("Motion")) {
                if (e.getPacket() instanceof S12PacketEntityVelocity v && mc.thePlayer.getEntityId() == v.getEntityID()) {
                    v.setMotionX((int) (v.getMotionX() * motionXZ.getValue()));
                    v.setMotionY((int) (v.getMotionY() * motionY.getValue()));
                    v.setMotionZ((int) (v.getMotionZ() * motionXZ.getValue()));
                }
            }
        }

        if (event instanceof AttackEvent e && e.getPriority() == Event.Priority.Low) {
            if (mode.getValue().equals("Reduce") && mc.thePlayer.hurtTime != 0) {
                mc.thePlayer.motionX *= reduceOnHit.getValue();
                mc.thePlayer.motionZ *= reduceOnHit.getValue();
            }
        }
    }
}
