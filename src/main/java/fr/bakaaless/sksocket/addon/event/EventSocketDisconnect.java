package fr.bakaaless.sksocket.addon.event;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import fr.bakaaless.sksocket.addon.type.AdaptSocket;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EventSocketDisconnect extends Event {

    static {
        Skript.registerEvent("Socket Disconnect", SimpleEvent.class, EventSocketDisconnect.class, "socket disconnected");
        EventValues.registerEventValue(EventSocketDisconnect.class, AdaptSocket.class, new Getter<AdaptSocket, EventSocketDisconnect>() {
            @Nullable
            @Override
            public AdaptSocket get(final @NotNull EventSocketDisconnect event) {
                return event.getSocket();
            }
        }, 0);
    }

    private static final HandlerList HANDLERS = new HandlerList();

    private final AdaptSocket socket;

    public EventSocketDisconnect(final AdaptSocket socket, final boolean async) {
        super(async);
        this.socket = socket;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public AdaptSocket getSocket() {
        return this.socket;
    }

}
