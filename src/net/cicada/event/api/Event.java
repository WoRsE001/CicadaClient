package net.cicada.event.api;

import lombok.Getter;
import lombok.Setter;
@Getter @Setter
public class Event {
    protected Priority priority;
    private boolean canceled;

    public void cancel() {
        canceled = true;
    }

    public <T extends Event> T call() {
        EventCaller.call(this);
        return (T) this;
    }

    public enum Priority {
        Low,
        Middle,
        High,
    }
}
