package fr.bakaaless.skonnection.addon.effect.sockets;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import fr.bakaaless.skonnection.addon.type.sockets.AdaptSocket;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Objects;

@Name("Connect Socket")
@Description("This effect allows you to connect a socket to a server.")
@Examples({"connect socket {socket} to \"127.0.0.1:55555\""})
@Since("1.0.0")
@Deprecated
public class EffSocketConnect extends Effect {

    static {
        Skript.registerEffect(EffSocketConnect.class, "connect socket %socket% to %string%");
    }

    private Expression<AdaptSocket> socket;
    private Expression<String> ip;

    @Override
    public boolean init(final Expression<?> @NotNull [] exprs, final int matchedPattern, final @NotNull Kleenean isDelayed, final SkriptParser.@NotNull ParseResult parseResult) {
        this.socket = (Expression<AdaptSocket>) exprs[0];
        this.ip = (Expression<String>) exprs[1];
        return true;
    }

    @Override
    protected void execute(final Event e) {
        final AdaptSocket socket = this.socket.getSingle(e);
        if (this.ip == null || this.ip.getSingle(e) == null || socket == null)
            return;
        final String[] fullIp = Objects.requireNonNull(this.ip.getSingle(e)).split(":");
        String ip = fullIp[0];
        int port = 25565;
        if (fullIp.length > 1) {
            try {
                port = Integer.parseInt(fullIp[1]);
            } catch (NumberFormatException ignored) {
            }
        }
        try {
            socket.connect(new Socket(ip, port));
        } catch(UnknownHostException unknownHostException){
            Skript.exception(unknownHostException, "Server offline ?").printStackTrace();
        } catch(SocketTimeoutException socketTimeoutException){
            Skript.exception(socketTimeoutException, "Timed out ?").printStackTrace();
        } catch (SocketException socketException){
            Skript.exception(socketException, "Can't join the server").printStackTrace();
        } catch (Exception ignored) {
        }
    }

    @Override
    public @NotNull String toString(final @Nullable Event e, final boolean debug) {
        return "connect a socket";
    }

}
