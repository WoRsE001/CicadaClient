package net.cicada.event.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.network.Packet;
import net.cicada.event.api.Event;

@Getter @Setter @AllArgsConstructor
public class PacketEvent extends Event {
    private Packet packet;
    private Type type;

    public enum Type {
        Send,
        Receive,
    }
}
