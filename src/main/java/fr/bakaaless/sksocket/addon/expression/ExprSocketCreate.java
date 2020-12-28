package fr.bakaaless.sksocket.addon.expression;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import fr.bakaaless.sksocket.addon.type.AdaptSocket;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Objects;

public class ExprSocketCreate extends SimpleExpression<AdaptSocket> {

    static {
        Skript.registerExpression(ExprSocketCreate.class, AdaptSocket.class, ExpressionType.SIMPLE, "create [client ]socket [to ]%string%");
    }

    private Expression<String> ip;

    @Override
    public boolean init(final Expression<?> @NotNull [] exprs, final int matchedPattern, final @NotNull Kleenean isDelayed, final SkriptParser.@NotNull ParseResult parseResult) {
        ip = (Expression<String>) exprs[0];
        return true;
    }

    @Override
    protected AdaptSocket[] get(final @NotNull Event e) {
        if (this.ip == null || this.ip.getSingle(e) == null)
            return new AdaptSocket[0];
        final String[] fullIp = Objects.requireNonNull(this.ip.getSingle(e)).split(":");
        String ip = fullIp[0];
        int port = 25565;
        if (fullIp.length > 1) {
            try {
                port = Integer.parseInt(fullIp[1]);
            } catch (NumberFormatException ignored) {
            }
        }
        Socket socket;
        try {
            socket = new Socket(ip, port);
        } catch(UnknownHostException unknownHostException){
            Skript.exception(unknownHostException, "Server offline ?").printStackTrace();
            return new AdaptSocket[0];
        } catch(SocketTimeoutException socketTimeoutException){
            Skript.exception(socketTimeoutException, "Timed out ?").printStackTrace();
            return new AdaptSocket[0];
        } catch (SocketException socketException){
            Skript.exception(socketException, "Can't join the server").printStackTrace();
            return new AdaptSocket[0];
        } catch (Exception ex) {
            Skript.exception(ex, ex.getLocalizedMessage()).printStackTrace();
            return new AdaptSocket[0];
        }
        return new AdaptSocket[] {new AdaptSocket(socket)};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends AdaptSocket> getReturnType() {
        return AdaptSocket.class;
    }

    @Override
    public @NotNull String toString(final @Nullable Event e, final boolean debug) {
        return "create a socket and connect it to a server";
    }

}
