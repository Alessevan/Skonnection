package fr.bakaaless.sksocket.addon.expression;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import fr.bakaaless.sksocket.addon.type.AdaptClient;
import fr.bakaaless.sksocket.addon.type.AdaptServerSocket;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

public class ExprServerClients extends SimpleExpression<AdaptClient> {

    private Expression<AdaptServerSocket> server;

    @Nullable
    @Override
    protected AdaptClient[] get(final Event e) {
        if (this.server == null || this.server.getSingle(e) == null)
            return new AdaptClient[0];
        return this.server.getSingle(e).getClients().toArray(new AdaptClient[0]);
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends AdaptClient> getReturnType() {
        return AdaptClient.class;
    }

    @Override
    public String toString(final @Nullable Event e, final boolean debug) {
        return "return all connected clients of a serversocket";
    }

    @Override
    public boolean init(final Expression<?>[] exprs, final int matchedPattern, final Kleenean isDelayed, final SkriptParser.ParseResult parseResult) {
        this.server = (Expression<AdaptServerSocket>) exprs[0];
        return true;
    }
}
