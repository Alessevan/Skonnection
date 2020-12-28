package fr.bakaaless.sksocket.addon.expression;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import fr.bakaaless.sksocket.addon.type.AdaptClient;
import fr.bakaaless.sksocket.addon.type.AdaptServerSocket;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ExprClientServer extends SimpleExpression<AdaptServerSocket> {

    static {
        Skript.registerExpression(ExprClientServer.class, AdaptServerSocket.class, ExpressionType.SIMPLE, "[get ]server of client %clientsocket%", "[get ]client %clientsocket%'s server");
    }

    private Expression<AdaptClient> client;

    @Override
    public boolean init(final Expression<?> @NotNull [] exprs, final int matchedPattern, final @NotNull Kleenean isDelayed, final SkriptParser.@NotNull ParseResult parseResult) {
        this.client = (Expression<AdaptClient>) exprs[0];
        return true;
    }

    @Override
    protected AdaptServerSocket[] get(final @NotNull Event e) {
        if (this.client == null || this.client.getSingle(e) == null)
            return new AdaptServerSocket[0];
        return new AdaptServerSocket[] {client.getSingle(e).getServer()};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends AdaptServerSocket> getReturnType() {
        return AdaptServerSocket.class;
    }

    @Override
    public @NotNull String toString(final @Nullable Event e, final boolean debug) {
        return "get client's server";
    }

}
