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

        float rotationYaw = mc.thePlayer.rotationYaw;

        boolean forward = mc.gameSettings.keyBindForward.isKeyDown();
        boolean backward = mc.gameSettings.keyBindBack.isKeyDown();
        boolean left = mc.gameSettings.keyBindLeft.isKeyDown();
        boolean right = mc.gameSettings.keyBindRight.isKeyDown();

        if (forward && !backward && !left && !right) {
            rotationYaw += 0.0f;
        } else if (backward && !forward && !left && !right) {
            rotationYaw += 180.0f;
        } else if (left && !right && !forward && !backward) {
            rotationYaw -= 90.0f;
        } else if (right && !left && !forward && !backward) {
            rotationYaw += 90.0f;
        } else if (forward && left && !backward && !right) {
            rotationYaw -= 45.0f;
        } else if (forward && right && !backward && !left) {
            rotationYaw += 45.0f;
        } else if (backward && left && !forward && !right) {
            rotationYaw -= 135.0f;
        } else if (backward && right && !forward && !left) {
            rotationYaw += 135.0f;
        }

        rotationYaw = rotationYaw % 360.0f;
        if (rotationYaw < 0) {
            rotationYaw += 360.0f;
        }

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
