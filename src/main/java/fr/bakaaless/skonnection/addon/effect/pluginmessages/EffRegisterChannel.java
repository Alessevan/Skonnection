package fr.bakaaless.skonnection.addon.effect.pluginmessages;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import fr.bakaaless.skonnection.plugin.listeners.PluginMessagesListener;
import fr.bakaaless.skonnection.plugin.Skonnection;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Register Channel")
@Description("This effect allows you to register a channel to send plugin messages through it.")
@Examples({ "on load:",
        "\tregister channel \"BungeeCord\"",
        "\tregister channel \"my:channel\""})
@Since("1.1.0")
public class EffRegisterChannel extends Effect {

    static {
        Skript.registerEffect(EffRegisterChannel.class, "register channel %string%");
    }

    private Expression<String> channel;

    @Override
    public boolean init(final Expression<?>[] exprs, final int matchedPattern, final Kleenean isDelayed, final SkriptParser.ParseResult parseResult) {
        this.channel = (Expression<String>) exprs[0];
        return true;
    }

    @Override
    protected void execute(final Event e) {
        final String channel = this.channel.getSingle(e);
        if (channel == null)
            return;
        if ((!channel.equals(channel.toLowerCase()) || channel.split(":").length != 2) && !channel.equals("BungeeCord"))
            return;
        if (Skonnection.getMessenger().isReservedChannel(channel))
            return;
        try {
            Skonnection.getMessenger().registerOutgoingPluginChannel(Skonnection.get(), channel);
            Skonnection.getMessenger().registerIncomingPluginChannel(Skonnection.get(), channel, PluginMessagesListener.get());
        } catch (IllegalArgumentException ignored) {
        }
    }

    @Override
    public String toString(final @Nullable Event e, final boolean debug) {
        return "register a channel " + this.channel.toString(e, debug);
    }

}
