package net.cicada.module.impl.fun;

import net.cicada.event.api.Event;
import net.cicada.event.impl.*;
import net.cicada.module.api.Category;
import net.cicada.module.api.Module;
import net.cicada.module.api.ModuleInfo;
import net.cicada.module.setting.impl.BooleanSetting;
import net.cicada.module.setting.impl.NumberSetting;
import net.cicada.utility.Player.MovementUtil;
import net.cicada.utility.Player.RotateUtil;

@ModuleInfo(name = "Spin", category = Category.Fun)
public class Spin extends Module {
    NumberSetting yawSpeed = new NumberSetting("YawSpeed", 90, -180, 180, 1, () -> true, this);
    NumberSetting pitchAngle = new NumberSetting("PitchAngle", 90, -90, 90, 1, () -> true, this);
    BooleanSetting serverSide = new BooleanSetting("ServerSide", false, () -> true, this);
    BooleanSetting jumpFix = new BooleanSetting("JumpFix", true, () -> this.serverSide.isValue(), this);
    BooleanSetting moveFix = new BooleanSetting("MoveFix", true, () -> this.serverSide.isValue(), this);
    BooleanSetting silentMoveFix = new BooleanSetting("SilentMoveFix", true, () -> this.serverSide.isValue() && moveFix.isValue(), this);

    int count = 0;

    @Override
    public void listen(Event event) {
        if (event instanceof MotionEvent e && e.getPriority() == Event.Priority.Low) {
            if (this.serverSide.isValue()) {
                e.setRotationYaw(count);
                e.setRotationPitch((float) pitchAngle.getValue());
            }
            mc.thePlayer.rotationYawHead = count;
            mc.thePlayer.rotationPitchHead = (int) pitchAngle.getValue();
            mc.thePlayer.renderYawOffset = count;
            count += (int) yawSpeed.getValue();
        }

        if (event instanceof JumpEvent e && this.jumpFix.isValue()) {
            e.setRotationYaw(count);
        }

        if (this.moveFix.isValue()) {
            if (event instanceof StrafeEvent e) {
                e.setRotationYaw(count);
            }

            if (event instanceof MovementEvent e && this.silentMoveFix.isValue()) {
                MovementUtil.moveFix(e, count, MovementUtil.getDirection(mc.thePlayer.rotationYaw, e.getMoveForward(), e.getMoveStrafe()));
            }
        }
    }
}
