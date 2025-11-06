package net.cicada.command.api;

import net.cicada.command.impl.Bind;
import net.cicada.command.impl.Config;
import net.cicada.command.impl.Toggle;
import net.cicada.event.api.Event;
import net.cicada.event.api.EventListener;
import net.cicada.event.impl.PacketEvent;
import net.minecraft.network.play.client.C01PacketChatMessage;

import java.lang.reflect.Field;
import java.util.*;

public class CommandManager implements EventListener {
    public static final List<Command> COMMANDS = new ArrayList<>();
    public static String prefix = ".";

    public static Bind BIND = new Bind();
    public static Config CONFIG = new Config();
    public static Toggle TOGGLE = new Toggle();

    public CommandManager() {
        for (Field field : getClass().getDeclaredFields()) {
            if (Command.class.isAssignableFrom(field.getType())) {
                field.setAccessible(true);
                try {
                    COMMANDS.add((Command) field.get(this));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void listen(Event event) {
        if (event instanceof PacketEvent e && e.getPacket() instanceof C01PacketChatMessage message) {
            if (!message.getMessage().startsWith(prefix)) return;
            event.cancel();
            String[] args = message.getMessage().toLowerCase().substring(1).split(" ");
            String alias = args[0];
            for (Command command : COMMANDS) {
                for (String aliases : command.getAliases()) {
                    if(!aliases.equals(alias)) continue;
                    command.execute(args);
                    return;
                }
            }
        }
    }

    @Override
    public boolean listen() {
        return true;
    }
}
