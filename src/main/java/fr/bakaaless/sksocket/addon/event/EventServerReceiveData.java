package fr.bakaaless.sksocket.addon.event;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import fr.bakaaless.sksocket.addon.type.AdaptClient;
import fr.bakaaless.sksocket.addon.type.AdaptServerSocket;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EventServerReceiveData extends Event {

    static {
        Skript.registerEvent("Client Attempt To Connect", SimpleEvent.class, EventServerReceiveConnection.class, "[client ]attempt to connect[ on server][ async]");
        EventValues.registerEventValue(EventServerReceiveConnection.class, AdaptServerSocket.class, new Getter<AdaptServerSocket, EventServerReceiveConnection>() {
            @Nullable
            @Override
            public AdaptServerSocket get(final @NotNull EventServerReceiveConnection event) {
                return event.getServer();
            }
        }, 0);
        EventValues.registerEventValue(EventServerReceiveConnection.class, AdaptClient.class, new Getter<AdaptClient, EventServerReceiveConnection>() {
            @Nullable
            @Override
            public AdaptClient get(final @NotNull EventServerReceiveConnection event) {
                return event.getClient();
            }
        }, 0);
    }

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
