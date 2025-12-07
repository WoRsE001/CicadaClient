package net.cicada.utility.Player;

import lombok.experimental.UtilityClass;
import net.cicada.event.impl.MovementEvent;
import net.cicada.utility.Access;
import net.minecraft.util.MathHelper;

@UtilityClass
public class MoveUtil implements Access {
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

    public double direction(float rotationYaw, final double moveForward, final double moveStrafing) {
        if (moveForward < 0F) rotationYaw += 180F;
        float forward = 1F;
        if (moveForward < 0F) forward = -0.5F;
        else if (moveForward > 0F) forward = 0.5F;
        if (moveStrafing > 0F) rotationYaw -= 90F * forward;
        if (moveStrafing < 0F) rotationYaw += 90F * forward;
        return Math.toRadians(rotationYaw);
    }

    public void moveFix(MovementEvent e, float srcYaw, float targetYaw) {
        if (e.getMoveForward() == 0f && e.getMoveStrafe() == 0f)
            return;
        float closestDiff = Float.MAX_VALUE;
        for (float forward = -1; forward <= 1f; forward++) {
            for (float strafe = -1; strafe <= 1f; strafe++) {
                if (forward == 0f && strafe == 0)
                    continue;

                float diff = Math.abs(MathHelper.wrapAngleTo180_float(targetYaw - getDirection(srcYaw, forward, strafe)));
                if (diff < closestDiff) {
                    closestDiff = diff;
                    e.setMoveForward(forward);
                    e.setMoveStrafe(strafe);
                }
            }
        }
    }

    public float getDirection(float yaw, float forward, float strafe) {
        if (forward < 0) {
            yaw += 180;
        }

        float forwardMult = 1f;

        if (forward < 0) forwardMult = -0.5f;
        else if (forward > 0) forwardMult = 0.5f;

        if (strafe > 0) yaw -= 90 * forwardMult;
        if (strafe < 0) yaw += 90 * forwardMult;

        return MathHelper.wrapAngleTo180_float(yaw);
    }

    public boolean isMoving() {
        return  mc.thePlayer.movementInput != null && (mc.thePlayer.movementInput.moveForward != 0 || mc.thePlayer.movementInput.moveStrafe != 0);
    }
}
