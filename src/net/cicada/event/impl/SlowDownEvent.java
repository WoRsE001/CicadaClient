package net.cicada.event.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.cicada.event.api.Event;

@Getter @Setter @AllArgsConstructor
public class SlowDownEvent extends Event {
    private float forwardSlowDownFactor;
    private float strafeSlowDownFactor;
}
