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
import fr.bakaaless.skonnection.addon.type.sockets.AdaptSocket;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Socket UUID")
@Description("This expression allows you to get a socket's uuid.")
@Examples({"set {uuid} to {socket}'s uuid"})
@Since("1.0.0")
public class ExprSocketUUID extends SimpleExpression<String> {

    static {
        Skript.registerExpression(ExprSocketUUID.class, String.class, ExpressionType.SIMPLE, "[get] uuid of socket %socket%", "[get] socket %socket%'s uuid");
    }

    private Expression<AdaptSocket> socket;

    @Override
    public boolean init(final Expression<?> @NotNull [] exprs, final int matchedPattern, final @NotNull Kleenean isDelayed, final SkriptParser.@NotNull ParseResult parseResult) {
        this.socket = (Expression<AdaptSocket>) exprs[0];
        return true;
    }

    @Override
    protected String[] get(final @NotNull Event e) {
        if (this.socket == null || this.socket.getSingle(e) == null)
            return new String[0];
        return new String[] {socket.getSingle(e).getUniqueId().toString()};
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
        return "get " + this.socket.toString(e, debug) + "'s uuid";
    }

}
