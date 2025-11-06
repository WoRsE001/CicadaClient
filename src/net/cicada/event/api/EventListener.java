package net.cicada.event.api;

public interface EventListener
{
    void listen(Event event);
    boolean listen();
}