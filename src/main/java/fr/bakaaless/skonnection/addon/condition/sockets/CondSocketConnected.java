package fr.bakaaless.skonnection.addon.condition.sockets;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import fr.bakaaless.skonnection.addon.type.sockets.AdaptSocket;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Socket Connected")
@Description("This condition allows you to check if a socket is connected.")
@Examples({"on load:",
        "\tset {socket} to create socket to \"127.0.0.1:55555\"",
        "\tif {socket} is connected:",
        "\t\tbroadcast \"Socket connected\""})
@Since("1.0.0")
public class CondSocketConnected extends Condition {

    static {
        Skript.registerCondition(CondSocketConnected.class, "socket %socket%('s| is) connect[ed]");
    }

    private Expression<AdaptSocket> socket;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(final Expression<?>[] expr, int matchedPattern, final Kleenean paramKleenean, final SkriptParser.ParseResult paramParseResult) {
        this.socket = (Expression<AdaptSocket>) expr[0];
        return true;
    }

    @Override
    public boolean check(final Event e) {
        return this.socket != null && this.socket.getSingle(e) != null && this.socket.getSingle(e).isConnected();
    }

    @Override
    public String toString(final @Nullable Event e, boolean debug) {
        return this.socket.toString(e, debug) + " is connected";
    }


}
