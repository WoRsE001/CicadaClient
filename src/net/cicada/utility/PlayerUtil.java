package net.cicada.utility;

import lombok.experimental.UtilityClass;
import net.cicada.module.api.ModuleManager;
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

@UtilityClass
public class PlayerUtil implements Access {
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
        if (entity.getEntityId() >= 1000000000 || entity.getEntityId() <= 0) return true;
        if (mc.getNetHandler().getPlayerInfo(entity.getUniqueID()) == null) return true;
        return mc.getNetHandler().getPlayerInfo(entity.getUniqueID()).getResponseTime() <= 0;
    }

    public boolean isValid(Entity entity) {
        if ((ModuleManager.TARGETS.teams.isValue() && isTeam(entity))) {
            return false;
        }
        if (entity instanceof EntityLivingBase && entity.isEntityAlive() && entity != mc.thePlayer) {
            if (ModuleManager.TARGETS.typeTargets.is("Players") && entity instanceof EntityPlayer) {
                return !ModuleManager.TARGETS.antiBot.isValue() || !isBot(entity);
            }
            return (ModuleManager.TARGETS.typeTargets.is("Mobs") && isMob(entity)) || (ModuleManager.TARGETS.typeTargets.is("Animals")  && isAnimal(entity));
        }
        return false;
    }
}
