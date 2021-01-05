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
import fr.bakaaless.skonnection.plugin.Skonnection;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Unregister Channel")
@Description("This effect allows you to unregister a registered channel.")
@Examples({"on unload:",
        "\tunregister channel \"BungeeCord\"",
        "\tunregister channel \"my:channel\""})
@Since("1.1.0")
public class EffUnregisterChannel extends Effect {

    static {
        Skript.registerEffect(EffUnregisterChannel.class, "unregister channel %string%");
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
        if (Skonnection.getMessenger().isOutgoingChannelRegistered(Skonnection.get(), channel) || Skonnection.getMessenger().isIncomingChannelRegistered(Skonnection.get(), channel))
            return;
        Skonnection.getMessenger().unregisterOutgoingPluginChannel(Skonnection.get(), channel);
        Skonnection.getMessenger().unregisterIncomingPluginChannel(Skonnection.get(), channel);
    }

    @Override
    public String toString(final @Nullable Event e, final boolean debug) {
        return "unregister a channel";
    }

}