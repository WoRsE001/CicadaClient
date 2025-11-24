package net.cicada.utility.Player;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import net.cicada.utility.Access;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import org.lwjgl.util.vector.Vector2f;

@Getter @UtilityClass
public class RotateUtil implements Access {
    public Vector2f rotation = new Vector2f(0, 0), lastRotation = rotation;

    public void setRotation(float yaw, float pitch) {
        lastRotation = rotation;
        rotation.set(yaw, pitch);
    }

    public void setRotation(Vector2f rotate) {
        lastRotation = rotation;
        rotation = rotate;
    }

    public Vector2f calcDeltaRotate(Vec3 point, float yawSpeed, float pitchSpeed) {
        Vec3 diff = point.subtract(mc.thePlayer.getPositionEyes(1));
        float deltaYaw = (float) MathHelper.wrapAngleTo180_double(MathHelper.wrapAngleTo180_double(Math.toDegrees(Math.atan2(diff.zCoord, diff.xCoord)) - 90) - rotation.getX());
        float deltaPitch = (float) ((-Math.toDegrees(Math.atan2(diff.yCoord, Math.hypot(diff.xCoord, diff.zCoord)))) - rotation.getY());
        deltaYaw = MathHelper.clamp_float(deltaYaw, -yawSpeed, yawSpeed);
        deltaPitch = MathHelper.clamp_float(deltaPitch, -pitchSpeed, pitchSpeed);
        float gcdFix = (float) ((Math.pow(mc.gameSettings.mouseSensitivity * 0.6 + 0.2, 3.0)) * 1.2);
        deltaYaw = MathHelper.round(deltaYaw, gcdFix);
        deltaPitch = MathHelper.round(deltaPitch, gcdFix);
        return new Vector2f(deltaYaw, deltaPitch);
    }

    public void rotateTo(Vec3 point, float yawSpeed, float pitchSpeed) {
        lastRotation = rotation;
        Vector2f deltaRotation = calcDeltaRotate(point,  yawSpeed, pitchSpeed);
        rotation.translate(deltaRotation.getX(), deltaRotation.getY());
        rotation.setY(MathHelper.clamp_float(rotation.getY(), -90, 90));
    }

    public Vec3 bestHitVec(AxisAlignedBB box) {
        Vec3 playerPositionEyes = mc.thePlayer.getPositionEyes(1);
        return new Vec3(
                MathHelper.clamp_double(playerPositionEyes.xCoord, box.minX, box.maxX),
                MathHelper.clamp_double(playerPositionEyes.yCoord, box.minY, box.maxY),
                MathHelper.clamp_double(playerPositionEyes.zCoord, box.minZ, box.maxZ)
        );
    }

    public static boolean isLookingAtEntity(Entity target, Vector2f rotations, final double range) {
        Vec3 src = mc.thePlayer.getPositionEyes(1.0f);
        Vec3 rotationVec = PlayerUtil.getLook(rotations);
        Vec3 dest = src.addVector(rotationVec.xCoord * range, rotationVec.yCoord * range, rotationVec.zCoord * range);
        MovingObjectPosition obj = mc.theWorld.rayTraceBlocks(src, dest, false, false, true);
        if (obj == null) {
            return false;
        }
        return target.getEntityBoundingBox().expand(target.getCollisionBorderSize(), target.getCollisionBorderSize(), target.getCollisionBorderSize()).calculateIntercept(src, dest) != null;
    }
}
