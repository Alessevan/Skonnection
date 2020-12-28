package fr.bakaaless.sksocket.addon.event;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import fr.bakaaless.sksocket.addon.type.AdaptClient;
import fr.bakaaless.sksocket.addon.type.AdaptServerSocket;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EventServerReceiveConnection extends Event implements Cancellable {

    static {
        Skript.registerEvent("ServerSocket Receive Data", SimpleEvent.class, EventServerReceiveData.class, "server[ ][socket] receive data[ async]");
        EventValues.registerEventValue(EventServerReceiveData.class, AdaptServerSocket.class, new Getter<AdaptServerSocket, EventServerReceiveData>() {
            @Nullable
            @Override
            public AdaptServerSocket get(final @NotNull EventServerReceiveData event) {
                return event.getServer();
            }
        }, 0);
        EventValues.registerEventValue(EventServerReceiveData.class, AdaptClient.class, new Getter<AdaptClient, EventServerReceiveData>() {
            @Nullable
            @Override
            public AdaptClient get(final @NotNull EventServerReceiveData event) {
                return event.getClient();
            }
        }, 0);
        EventValues.registerEventValue(EventServerReceiveData.class, String.class, new Getter<String, EventServerReceiveData>() {
            @Nullable
            @Override
            public String get(final @NotNull EventServerReceiveData event) {
                return event.getData();
            }
        }, 0);
    }

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
