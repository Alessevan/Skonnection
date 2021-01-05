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
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("ServerSocket Receive Data")
@Description("This event allows you to know when a server socket receive data from a client.")
@Examples({"on server socket receive data:",
        "\tif ip of event-clientsocket is not \"127.0.0.1\":",
        "\t\tstop",
        "\tbroadcast \"%uuid of event-serversocket% » %event-text%\""})
@Since("1.0.0")
public class EventServerReceiveData extends Event {

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
