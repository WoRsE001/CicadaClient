package net.cicada.utility;

import lombok.experimental.UtilityClass;
import net.cicada.event.impl.MovementEvent;
import net.minecraft.util.MathHelper;

@UtilityClass
public class MovementUtil implements Access {
    public void strafe(double speed, double strength) {
        if (!isMoving()) {
            mc.thePlayer.motionX = 0;
            mc.thePlayer.motionZ = 0;
            return;
        }

        double prevX = mc.thePlayer.motionX * (1.0 - strength);
        double prevZ = mc.thePlayer.motionZ * (1.0 - strength);
        double useSpeed = speed * strength;

        double angle = Math.toRadians(getDirs());
        mc.thePlayer.motionX = (-Math.sin(angle) * useSpeed) + prevX;
        mc.thePlayer.motionZ = (Math.cos(angle) * useSpeed) + prevZ;
    }

    public static float getDirs() {
        float yaw = mc.thePlayer.rotationYaw;
        int x  = (mc.gameSettings.keyBindRight.isKeyDown() ? 1 : 0) - (mc.gameSettings.keyBindLeft.isKeyDown()  ? 1 : 0);
        int z  = (mc.gameSettings.keyBindForward.isKeyDown() ? 1 : 0) - (mc.gameSettings.keyBindBack.isKeyDown()   ? 1 : 0);
        if (x != 0 || z != 0) yaw += (float) Math.toDegrees(Math.atan2(x, z));
        return MathHelper.wrapAngleTo180_float(yaw);
    }


    public void fixMovement(final MovementEvent event, final float yaw) {
        float delta_yaw = mc.thePlayer.rotationYaw - yaw;
        float x = event.getMoveStrafe(), z = event.getMoveForward();
        float newX = (float) (x * Math.cos(Math.toRadians(delta_yaw)) - z * Math.sin(Math.toRadians(delta_yaw)));
        float newZ = (float) (z * Math.cos(Math.toRadians(delta_yaw)) + x * Math.sin(Math.toRadians(delta_yaw)));
        event.setMoveForward(Math.round(newZ));
        event.setMoveStrafe(Math.round(newX));
    }

    public boolean isMoving() {
        return  mc.thePlayer.movementInput != null && (mc.thePlayer.movementInput.moveForward != 0 || mc.thePlayer.movementInput.moveStrafe != 0);
    }
}
