package net.cicada.utility;

import lombok.experimental.UtilityClass;
import net.cicada.module.api.ModuleManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import org.lwjgl.util.vector.Vector2f;

@UtilityClass
public class CombatManager implements Access {
    public EntityLivingBase target;

    public void updateTarget(String sortType) {
        EntityLivingBase candidate = null;
        for (Entity entity : mc.theWorld.getLoadedEntityList()) {
            if (!(entity instanceof EntityLivingBase) || entity.isDead || entity == mc.thePlayer || !PlayerUtil.isValid(entity)) continue;
            if (candidate == null) {
                candidate = (EntityLivingBase) entity;
                continue;
            }
            if (sortType.equals("FOV")) {
                if (FOVToTarget(entity, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch).length() < FOVToTarget(candidate, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch).length()) candidate = (EntityLivingBase) entity;
            } else if (sortType.equals("Health")) {
                if (((EntityLivingBase) entity).getHealth() < candidate.getHealth()) candidate = (EntityLivingBase) entity;
            } else if (sortType.equals("Distance")) {
                if (mc.thePlayer.getDistanceToEntity(entity) < mc.thePlayer.getDistanceToEntity(candidate)) candidate = (EntityLivingBase) entity;
            }
        }

        target = candidate;
    }

    public Vector2f FOVToTarget(Entity entity, float rotationYaw, float rotationPitch) {
        Vec3 diff = entity.getPositionEyes(1).subtract(mc.thePlayer.getPositionEyes(1));
        float deltaYaw = (float) MathHelper.wrapAngleTo180_double(MathHelper.wrapAngleTo180_double(Math.toDegrees(Math.atan2(diff.zCoord, diff.xCoord)) - 90) - rotationYaw);
        float deltaPitch = (float) ((-Math.toDegrees(Math.atan2(diff.yCoord, Math.hypot(diff.xCoord, diff.zCoord)))) - rotationPitch);
        return new Vector2f(Math.abs(deltaYaw), Math.abs(deltaPitch));
    }
}
