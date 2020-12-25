package fr.bakaaless.sksocket.addon.event;

import fr.bakaaless.sksocket.addon.type.AdaptSocket;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class EventSocketDisconnect extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final AdaptSocket socket;

    public EventSocketDisconnect(final AdaptSocket socket) {
        super(true);
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
