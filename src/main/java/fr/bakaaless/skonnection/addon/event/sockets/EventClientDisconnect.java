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
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Client Disconnect")
@Description("This event allows you to know when a client is disconnecting.")
@Examples({"on client disconnected:",
            "\tbroadcast ip of event-clientsocket"})
@Since("1.0.0")
public class EventClientDisconnect extends Event {

    static {
        Skript.registerEvent("Client Disconnect", SimpleEvent.class, EventClientDisconnect.class, "client disconnected");
        EventValues.registerEventValue(EventClientDisconnect.class, AdaptClient.class, new Getter<AdaptClient, EventClientDisconnect>() {
            @Nullable
            @Override
            public AdaptClient get(final @NotNull EventClientDisconnect event) {
                return event.getClient();
            }
        }, 0);
    }

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
