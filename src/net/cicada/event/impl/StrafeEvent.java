package net.cicada.event.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.cicada.event.api.Event;

@Getter @Setter @AllArgsConstructor
public class StrafeEvent extends Event {
    private float moveForward, moveStrafe, friction, rotationYaw;
}
