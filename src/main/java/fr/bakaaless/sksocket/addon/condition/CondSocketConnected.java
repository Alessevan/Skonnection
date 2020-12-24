package fr.bakaaless.sksocket.addon.condition;

import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import fr.bakaaless.sksocket.addon.type.AdaptSocket;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

public class CondSocketConnected extends Condition {

    private Expression<AdaptSocket> socket;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(final Expression<?>[] expr, int matchedPattern, final Kleenean paramKleenean, final SkriptParser.ParseResult paramParseResult) {
        this.socket = (Expression<AdaptSocket>) expr[0];
        return true;
    }

    @Override
    public String toString(final @Nullable Event e, boolean b) {
        return "socket connected";
    }

    @Override
    public boolean check(final Event e) {
        return this.socket != null && this.socket.getSingle(e) != null && this.socket.getSingle(e).isConnected();
    }

}
