package net.cicada.utility.Player;

import com.google.common.base.Predicates;
import lombok.experimental.UtilityClass;
import net.cicada.module.api.ModuleManager;
import net.cicada.utility.Access;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import org.lwjgl.util.vector.Vector2f;

import java.util.List;

@UtilityClass
public class PlayerUtil implements Access {
    public Vec3 getLook(Vector2f rotation) {
        float f = MathHelper.cos(-rotation.getX() * 0.017453292F - (float) Math.PI);
        float f1 = MathHelper.sin(-rotation.getX() * 0.017453292F - (float) Math.PI);
        float f2 = -MathHelper.cos(-rotation.getY() * 0.017453292F);
        float f3 = MathHelper.sin(-rotation.getY() * 0.017453292F);
        return new Vec3(f1 * f2, f3,f * f2);
    }

    public MovingObjectPosition rayTrace(Vector2f rotation, float blockReachDistance, float partialTicks) {
        Vec3 vec3 = mc.thePlayer.getPositionEyes(partialTicks);
        Vec3 vec31 = getLook(rotation);
        Vec3 vec32 = vec3.addVector(vec31.xCoord * blockReachDistance, vec31.yCoord * blockReachDistance, vec31.zCoord * blockReachDistance);
        return mc.thePlayer.worldObj.rayTraceBlocks(vec3, vec32, false, false, true);
    }

    public MovingObjectPosition rayTrace(Vec3 vec32, float partialTicks) {
        Vec3 vec3 = mc.thePlayer.getPositionEyes(partialTicks);
        return mc.thePlayer.worldObj.rayTraceBlocks(vec3, vec32, false, false, true);
    }

    public static MovingObjectPosition rayCast(Vector2f rotation, float range, Entity entity) {
        final float partialTicks = mc.timer.renderPartialTicks;
        MovingObjectPosition objectMouseOver;

        if (entity != null && mc.theWorld != null) {
            objectMouseOver = PlayerUtil.rayTrace(rotation, range, 1);
            double d1 = range;
            final Vec3 vec3 = entity.getPositionEyes(partialTicks);

            if (objectMouseOver != null) {
                d1 = objectMouseOver.hitVec.distanceTo(vec3);
            }

            final Vec3 vec31 = PlayerUtil.getLook(rotation);
            final Vec3 vec32 = vec3.addVector(vec31.xCoord * range, vec31.yCoord * range, vec31.zCoord * range);
            Entity pointedEntity = null;
            Vec3 vec33 = null;
            final float f = 1.0F;
            final List<Entity> list = mc.theWorld.getEntitiesInAABBexcluding(entity, entity.getEntityBoundingBox().addCoord(vec31.xCoord * range, vec31.yCoord * range, vec31.zCoord * range).expand(f, f, f), Predicates.and(EntitySelectors.NOT_SPECTATING, Entity::canBeCollidedWith));
            double d2 = d1;

            for (final Entity entity1 : list) {
                final float f1 = entity1.getCollisionBorderSize();
                final AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().expand(f1, f1, f1);
                final MovingObjectPosition movingobjectposition = axisalignedbb.calculateIntercept(vec3, vec32);

                if (axisalignedbb.isVecInside(vec3)) {
                    if (d2 >= 0.0D) {
                        pointedEntity = entity1;
                        vec33 = movingobjectposition == null ? vec3 : movingobjectposition.hitVec;
                        d2 = 0.0D;
                    }
                } else if (movingobjectposition != null) {
                    final double d3 = vec3.distanceTo(movingobjectposition.hitVec);

                    if (d3 < d2 || d2 == 0.0D) {
                        pointedEntity = entity1;
                        vec33 = movingobjectposition.hitVec;
                        d2 = d3;
                    }
                }
            }

            if (pointedEntity != null && (d2 < d1 || objectMouseOver == null)) {
                objectMouseOver = new MovingObjectPosition(pointedEntity, vec33);
            }

            return objectMouseOver;
        }

        return null;
    }
    
    public Vec3 getSmoothPos(Entity entity) {
        double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * mc.timer.renderPartialTicks;
        double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * mc.timer.renderPartialTicks;
        double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * mc.timer.renderPartialTicks;
        return new Vec3(x, y, z);
    }

    public boolean isMob(Entity entity) {
        return entity instanceof EntityMob
                || entity instanceof EntityVillager
                || entity instanceof EntitySlime
                || entity instanceof EntityGhast
                || entity instanceof EntityDragon;
    }

    public boolean isAnimal(Entity entity) {
        return entity instanceof EntityAnimal
                || entity instanceof EntitySquid
                || entity instanceof EntityGolem
                || entity instanceof EntityBat;
    }

    public boolean isTeam(Entity entity) {
        if (mc.thePlayer.getDisplayName() != null && entity.getDisplayName() != null) {
            String targetName = entity.getDisplayName().getFormattedText().replace("§r", "");
            String clientName = mc.thePlayer.getDisplayName().getFormattedText().replace("§r", "");
            return targetName.startsWith("§" + clientName.charAt(1));
        }
        return false;
    }

    public boolean isBot(Entity entity) {
        if (entity.getEntityId() >= 1000000000 || entity.getEntityId() <= 0 && ModuleManager.TARGETS.antiBot.is("EntityID")) return true;
        if (mc.getNetHandler().getPlayerInfo(entity.getUniqueID()) == null) return ModuleManager.TARGETS.antiBot.is("PlayerInfo");
        else return mc.getNetHandler().getPlayerInfo(entity.getUniqueID()).getResponseTime() <= 0 && ModuleManager.TARGETS.antiBot.is("ResponseTime");
    }

    public boolean isValid(Entity entity) {
        if ((ModuleManager.TARGETS.teams.isValue() && isTeam(entity))) {
            return false;
        }
        if (entity instanceof EntityLivingBase && entity.isEntityAlive() && entity != mc.thePlayer) {
            if (ModuleManager.TARGETS.typeTargets.is("Players") && entity instanceof EntityPlayer) {
                return !isBot(entity);
            }
            return (ModuleManager.TARGETS.typeTargets.is("Mobs") && isMob(entity)) || (ModuleManager.TARGETS.typeTargets.is("Animals")  && isAnimal(entity));
        }
        return false;
    }
}
