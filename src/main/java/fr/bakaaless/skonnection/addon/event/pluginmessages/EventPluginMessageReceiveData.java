package fr.bakaaless.skonnection.addon.event.pluginmessages;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import fr.bakaaless.skonnection.addon.type.pluginmessages.AdaptData;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Name("PluginMessage Receive Data")
@Description("This event allows you to retrieve an incoming plugin message")
@Examples({"on receive plugin message:",
        "\tbroadcast \"INCOMING PLUGIN MESSAGE FROM CHANNEL %event-text%\"",
        "\tset {_output::*} to retrieve event-data"})
@Since("1.1.0")
public class EventPluginMessageReceiveData extends Event {

    static {
        Skript.registerEvent("PluginMessage Receive Data", SimpleEvent.class, EventPluginMessageReceiveData.class, "receive plugin[ ]message");
        EventValues.registerEventValue(EventPluginMessageReceiveData.class, String.class, new Getter<String, EventPluginMessageReceiveData>() {
            @Nullable
            @Override
            public String get(final @NotNull EventPluginMessageReceiveData event) {
                return event.getChannel();
            }
        }, 0);
        EventValues.registerEventValue(EventPluginMessageReceiveData.class, AdaptData.class, new Getter<AdaptData, EventPluginMessageReceiveData>() {
            @Nullable
            @Override
            public AdaptData get(final @NotNull EventPluginMessageReceiveData event) {
                return event.getData();
            }
        }, 0);
        EventValues.registerEventValue(EventPluginMessageReceiveData.class, Player.class, new Getter<Player, EventPluginMessageReceiveData>() {
            @Nullable
            @Override
            public Player get(final @NotNull EventPluginMessageReceiveData event) {
                return event.getPlayer();
            }
        }, 0);
    }

    private static final HandlerList HANDLERS = new HandlerList();

    private final String channel;
    private final Player player;
    private final AdaptData data;

    public EventPluginMessageReceiveData(final String channel, final Player player, final List<String> answer) {
        this.channel = channel;
        this.player = player;
        this.data = new AdaptData(answer);
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
        return channel;
    }

    public Player getPlayer() {
        return this.player;
    }

    public AdaptData getData() {
        return data;
    }

}
