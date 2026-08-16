package euclid.messages;

import java.util.ArrayList;
import java.util.List;

public abstract class IMessageComposer {

    private final List<Object> data = new ArrayList<>();
    private boolean composed;

    public List<Object> getData() {
        return data;
    }

    public boolean isComposed() {
        return composed;
    }

    public void setComposed(boolean composed) {
        this.composed = composed;
    }

    public abstract void write();
}
