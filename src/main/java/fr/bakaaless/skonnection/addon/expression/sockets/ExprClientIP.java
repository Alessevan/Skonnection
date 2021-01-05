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
import fr.bakaaless.skonnection.addon.type.sockets.AdaptClient;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Client IP")
@Description("This expression allows you to get a client's ip.")
@Examples({"on client attempt to connect on server:",
        "\tif ip of event-clientsocket is not \"127.0.0.1\":",
        "\t\tcancel event"})
@Since("1.0.0")
public class ExprClientIP extends SimpleExpression<String> {

    static {
        Skript.registerExpression(ExprClientIP.class, String.class, ExpressionType.SIMPLE, "[get ]ip of client %clientsocket%", "[get ]client %clientsocket%'s ip");
    }

    private Expression<AdaptClient> client;

    @Override
    public boolean init(final Expression<?> @NotNull [] exprs, final int matchedPattern, final @NotNull Kleenean isDelayed, final SkriptParser.@NotNull ParseResult parseResult) {
        this.client = (Expression<AdaptClient>) exprs[0];
        return true;
    }

    @Override
    protected String[] get(final @NotNull Event e) {
        if (this.client == null || this.client.getSingle(e) == null)
            return new String[0];
        return new String[] {client.getSingle(e).getIp()};
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
        return "get client's ip";
    }

}
