package net.cicada.event.impl;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.cicada.event.api.Event;

@Getter @Setter
public class AttackEvent extends Event {
    private EntityPlayer playerIn;
    private Entity targetEntity;

    public AttackEvent(EntityPlayer playerIn, Entity targetEntity, Priority priority) {
        this.playerIn = playerIn;
        this.targetEntity = targetEntity;
        this.priority = priority;
    }
}
