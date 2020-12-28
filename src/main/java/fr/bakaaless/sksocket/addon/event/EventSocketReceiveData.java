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

public class EventSocketReceiveData extends Event {

    static {
        Skript.registerEvent("Socket Receive Data Event", SimpleEvent.class, EventSocketReceiveData.class, "socket receive data[ async]");
        EventValues.registerEventValue(EventSocketReceiveData.class, AdaptSocket.class, new Getter<AdaptSocket, EventSocketReceiveData>() {
            @Nullable
            @Override
            public AdaptSocket get(final @NotNull EventSocketReceiveData event) {
                return event.getSocket();
            }
        }, 0);
        EventValues.registerEventValue(EventSocketReceiveData.class, String.class, new Getter<String, EventSocketReceiveData>() {
            @Nullable
            @Override
            public String get(final @NotNull EventSocketReceiveData event) {
                return event.getData();
            }
        }, 0);
    }

    private static final HandlerList HANDLERS = new HandlerList();

    private final AdaptSocket socket;
    private final String data;

    public EventSocketReceiveData(final AdaptSocket socket, final String data) {
        super(true);
        this.socket = socket;
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

    public AdaptSocket getSocket() {
        return this.socket;
    }

    public String getData() {
        return this.data;
    }

}
