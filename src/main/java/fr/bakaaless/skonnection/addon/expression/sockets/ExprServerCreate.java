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

import java.net.ServerSocket;

@Name("Create ServerSocket")
@Description("This expression allows you to create a server socket.")
@Examples({"set {server} to create server socket with port 55555"})
@Since("1.0.0")
public class ExprServerCreate extends SimpleExpression<AdaptServerSocket> {

    static {
        Skript.registerExpression(ExprServerCreate.class, AdaptServerSocket.class, ExpressionType.SIMPLE, "create server[ ][socket] [with port ]%integer%");
    }

    private Expression<Integer> port;

    @Override
    public boolean init(final Expression<?> @NotNull [] exprs, final int matchedPattern, final @NotNull Kleenean isDelayed, final SkriptParser.@NotNull ParseResult parseResult) {
        port = (Expression<Integer>) exprs[0];
        return true;
    }

    @Override
    protected AdaptServerSocket[] get(final @NotNull Event e) {
        if (this.port == null || this.port.getSingle(e) == null)
            return new AdaptServerSocket[0];
        ServerSocket socket;
        try {
            final int port = Integer.parseInt(this.port.getSingle(e).toString());
            socket = new ServerSocket(port);
        } catch (Exception ex) {
            Skript.exception(ex, ex.getLocalizedMessage()).printStackTrace();
            return new AdaptServerSocket[0];
        }
        return new AdaptServerSocket[] {new AdaptServerSocket(socket)};
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
        return "create a server socket";
    }

}
