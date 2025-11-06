package net.cicada.event.impl;

import net.cicada.event.api.Event;

public class Render3DEvent extends Event {
    public Render3DEvent(Priority priority) {
        this.priority = priority;
    }
}
