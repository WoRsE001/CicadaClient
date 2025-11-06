package net.cicada.event.impl;

import lombok.Getter;
import lombok.Setter;
import net.cicada.event.api.Event;

@Getter @Setter
public class MotionEvent extends Event {
    private double posX, posY, posZ;
    private float rotationYaw, rotationPitch;
    private boolean onGround;

    public MotionEvent(double posX, double posY, double posZ, float rotationYaw, float rotationPitch, boolean onGround, Priority priority) {
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        this.rotationYaw = rotationYaw;
        this.rotationPitch = rotationPitch;
        this.onGround = onGround;
        this.priority = priority;
    }
}
