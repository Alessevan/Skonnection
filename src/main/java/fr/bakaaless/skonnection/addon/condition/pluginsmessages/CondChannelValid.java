package fr.bakaaless.skonnection.addon.condition.pluginsmessages;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import fr.bakaaless.skonnection.addon.condition.sockets.CondSocketConnected;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Channel Validity")
@Description("This effect allows you to check if a channel is valid.")
@Examples({"on load:",
        "\tset {_myChannel} to \"BungeeCord\"",
        "\tif {_myChannel} is valid channel:",
        "\t\tregister channel {_myChannel}"})
@Since("1.1.0")
public class CondChannelValid extends Condition {

    static {
        Skript.registerCondition(CondSocketConnected.class, "%string%[ is|'s] valid[ channel]");
    }

    private Expression<String> channel;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(final Expression<?>[] expr, int matchedPattern, final Kleenean paramKleenean, final SkriptParser.ParseResult paramParseResult) {
        this.channel = (Expression<String>) expr[0];
        return true;
    }

    @Override
    public boolean check(final Event e) {
        final String channel = this.channel.getSingle(e);
        if (channel == null)
            return false;
        return (channel.equals(channel.toLowerCase()) && channel.split(":").length == 2) || channel.equals("BungeeCord");
    }

    @Override
    public String toString(final @Nullable Event e, boolean b) {
        return "is channel valid";
    }

}
