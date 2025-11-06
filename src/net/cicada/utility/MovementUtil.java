package net.cicada.utility;

import lombok.experimental.UtilityClass;
import net.cicada.event.impl.MovementEvent;

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

        double angle = Math.toRadians(direction(mc.thePlayer.rotationYaw));
        mc.thePlayer.motionX = (-Math.sin(angle) * useSpeed) + prevX;
        mc.thePlayer.motionZ = (Math.cos(angle) * useSpeed) + prevZ;
    }

    public double direction(float rotationYaw) {
        if (mc.thePlayer.movementInput.moveForward < 0F) rotationYaw += 180F;

        float forward = 1F;

        if (mc.thePlayer.movementInput.moveForward < 0F) forward = -0.5F;
        else if (mc.thePlayer.movementInput.moveForward > 0F) forward = 0.5F;

        if (mc.thePlayer.movementInput.moveStrafe > 0F) rotationYaw -= 90F * forward;
        if (mc.thePlayer.movementInput.moveStrafe < 0F) rotationYaw += 90F * forward;

        return rotationYaw;
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
