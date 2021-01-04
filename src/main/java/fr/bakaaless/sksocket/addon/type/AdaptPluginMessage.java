package fr.bakaaless.sksocket.addon.type;

import java.util.ArrayList;
import java.util.List;

public class AdaptPluginMessage {

    private final List<Object> content;

    public AdaptPluginMessage() {
        this.content = new ArrayList<>();
    }

    public void addContent(final Object value) {
        this.content.add(value);
    }

    public void removeContent(final Object value) {
        this.content.remove(value);
    }

    public List<?> getContent() {
        return this.content;
    }

    public byte[] toByteArray() {
        return new byte[0];
    }
}
