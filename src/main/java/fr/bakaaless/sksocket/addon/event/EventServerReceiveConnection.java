package fr.bakaaless.sksocket.addon.event;

import fr.bakaaless.sksocket.addon.type.AdaptClient;
import fr.bakaaless.sksocket.addon.type.AdaptServerSocket;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class EventServerReceiveConnection extends Event implements Cancellable {

    private static HandlerList HANDLERS = new HandlerList();

    private final AdaptServerSocket server;
    private final AdaptClient client;
    private boolean cancelled;

    public EventServerReceiveConnection(AdaptServerSocket server, AdaptClient client) {
        super(true);
        this.server = server;
        this.client = client;
        this.cancelled = false;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }

    public AdaptServerSocket getServer() {
        return this.server;
    }

    public AdaptClient getClient() {
        return this.client;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
}
