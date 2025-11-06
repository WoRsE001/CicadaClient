package net.cicada.event.api;

import java.util.ArrayList;
import java.util.List;

public class EventCaller {
    private static final List<EventListener> listeners = new ArrayList<>();

    public static void register(EventListener... listeners) {
        EventCaller.listeners.addAll(List.of(listeners));
    }

    public static void register(List<EventListener> listeners) {
        EventCaller.listeners.addAll(listeners);
    }

    public static void call(Event e) {
        for (EventListener listener : listeners) {
            if (!listener.listen()) continue;
            listener.listen(e);
        }
    }
}
