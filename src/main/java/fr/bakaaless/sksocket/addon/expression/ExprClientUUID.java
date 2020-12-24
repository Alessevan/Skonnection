package fr.bakaaless.sksocket.addon.expression;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import fr.bakaaless.sksocket.addon.type.AdaptClient;
import fr.bakaaless.sksocket.addon.type.AdaptSocket;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ExprClientUUID extends SimpleExpression<String> {

    private Expression<AdaptClient> client;

    @Override
    protected String[] get(final @NotNull Event e) {
        if (this.client == null || this.client.getSingle(e) == null)
            return new String[0];
        return new String[] {client.getSingle(e).getUniqueId().toString()};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    public @NotNull String toString(final @Nullable Event e, final boolean debug) {
        return "get client's uuid";
    }

    @Override
    public boolean init(final Expression<?> @NotNull [] exprs, final int matchedPattern, final @NotNull Kleenean isDelayed, final SkriptParser.@NotNull ParseResult parseResult) {
        this.client = (Expression<AdaptClient>) exprs[0];
        return true;
    }

}
