package fr.bakaaless.sksocket.addon.event;

import fr.bakaaless.sksocket.addon.type.AdaptClient;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class EventClientDisconnect extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final AdaptClient client;

    public EventClientDisconnect(final AdaptClient client, final boolean async) {
        super(async);
        this.client = client;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public AdaptClient getClient() {
        return this.client;
    }

}
