package net.cicada.event.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.cicada.event.api.Event;

@Getter @Setter @AllArgsConstructor
public class MovementInputEvent extends Event {
    private boolean forward, backward, right, left, jump, sneak;
}
