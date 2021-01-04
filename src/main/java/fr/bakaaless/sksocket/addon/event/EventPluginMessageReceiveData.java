package fr.bakaaless.sksocket.addon.event;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import fr.bakaaless.sksocket.addon.type.AdaptClient;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EventPluginMessageReceiveData extends Event {

    static {
        Skript.registerEvent("PluginMessage Receive Data", SimpleEvent.class, EventPluginMessageReceiveData.class, "receive plugin message");
        EventValues.registerEventValue(EventPluginMessageReceiveData.class, String.class, new Getter<String, EventPluginMessageReceiveData>() {
            @Nullable
            @Override
            public String get(final @NotNull EventPluginMessageReceiveData event) {
                return event.getChannel();
            }
        }, 0);
        EventValues.registerEventValue(EventPluginMessageReceiveData.class, String[].class, new Getter<String[], EventPluginMessageReceiveData>() {
            @Nullable
            @Override
            public String[] get(final @NotNull EventPluginMessageReceiveData event) {
                return event.getAnswer().toArray(new String[0]);
            }
        }, 0);
    }

    private static final HandlerList HANDLERS = new HandlerList();

    private final String channel;
    private final Player player;
    private final List<String> answer;

    public EventPluginMessageReceiveData(final String channel, final Player player, final List<String> answer) {
        this.channel = channel;
        this.player = player;
        this.answer = answer;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public String getChannel() {
        return this.channel;
    }

    public Player getPlayer() {
        return this.player;
    }

    public List<String> getAnswer() {
        return this.answer;
    }
}
