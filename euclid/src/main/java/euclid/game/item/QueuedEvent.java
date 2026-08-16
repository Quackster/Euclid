package euclid.game.item;

import java.util.Map;
import java.util.function.Consumer;

public class QueuedEvent {

    private String eventName;
    private long ticksTimer;
    private Consumer<QueuedEvent> action;
    private Map<Object, Object> attributes;

    public QueuedEvent(String eventName, Consumer<QueuedEvent> action, long ticksTimer, Map<Object, Object> attributes) {
        this.eventName = eventName;
        this.action = action;
        this.ticksTimer = ticksTimer;
        this.attributes = attributes;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public long getTicksTimer() {
        return ticksTimer;
    }

    public void setTicksTimer(long ticksTimer) {
        this.ticksTimer = ticksTimer;
    }

    public Consumer<QueuedEvent> getAction() {
        return action;
    }

    public void setAction(Consumer<QueuedEvent> action) {
        this.action = action;
    }

    public Map<Object, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<Object, Object> attributes) {
        this.attributes = attributes;
    }

    public boolean hasAttribute(Object key) {
        return attributes.containsKey(key);
    }

    @SuppressWarnings("unchecked")
    public <T> T getAttribute(Object key) {
        if (attributes.containsKey(key)) {
            return (T) attributes.get(key);
        }
        return null;
    }
}
