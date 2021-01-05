package fr.bakaaless.skonnection.addon.event.sockets;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import fr.bakaaless.skonnection.addon.type.sockets.AdaptClient;
import fr.bakaaless.skonnection.addon.type.sockets.AdaptServerSocket;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Client Attempt To Connect")
@Description("This event allows you to know when a client is connecting to a server socket.")
@Examples({"on client attempt to connect on server:",
        "\tif ip of event-clientsocket is not \"127.0.0.1\":",
        "\t\tcancel event"})
@Since("1.0.0")
public class EventServerReceiveConnection extends Event implements Cancellable {

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
