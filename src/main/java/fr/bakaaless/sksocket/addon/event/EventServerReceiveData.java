package fr.bakaaless.sksocket.addon.event;

import fr.bakaaless.sksocket.addon.type.AdaptClient;
import fr.bakaaless.sksocket.addon.type.AdaptServerSocket;
import fr.bakaaless.sksocket.addon.type.AdaptSocket;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class EventServerReceiveData extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final AdaptServerSocket server;
    private final AdaptClient client;
    private final String data;

    public EventServerReceiveData(final AdaptServerSocket server, final AdaptClient client, final String data) {
        super(true);
        this.server = server;
        this.client = client;
        this.data = data;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public AdaptServerSocket getServer() {
        return this.server;
    }

    public AdaptClient getClient() {
        return this.client;
    }

    public String getData() {
        return this.data;
    }

}
