package fr.bakaaless.skonnection.addon.expression.sockets;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import fr.bakaaless.skonnection.addon.type.sockets.AdaptServerSocket;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("ServerSocket Port")
@Description("This expression allows you to get server socket's port.")
@Examples({"set {port} to {server}'s port"})
@Since("1.0.0")
public class ExprServerPort extends SimpleExpression<Integer> {

    static {
        Skript.registerExpression(ExprServerPort.class, Integer.class, ExpressionType.SIMPLE, "[get] port of server[ ][socket] %serversocket%", "[get] server[ ][socket] %serversocket%'s port");
    }

    private Expression<AdaptServerSocket> server;

    @Override
    public boolean init(final Expression<?> @NotNull [] exprs, final int matchedPattern, final @NotNull Kleenean isDelayed, final SkriptParser.@NotNull ParseResult parseResult) {
        this.server = (Expression<AdaptServerSocket>) exprs[0];
        return true;
    }

    @Override
    protected Integer[] get(final @NotNull Event e) {
        if (this.server == null || this.server.getSingle(e) == null)
            return new Integer[0];
        return new Integer[] {this.server.getSingle(e).getSocket().getLocalPort()};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends Integer> getReturnType() {
        return Integer.class;
    }

    @Override
    public @NotNull String toString(final @Nullable Event e, final boolean debug) {
        return "get " + this.server.toString(e, debug) + "'s port";
    }

}
