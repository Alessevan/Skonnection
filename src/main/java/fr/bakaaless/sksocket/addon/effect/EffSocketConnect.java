package fr.bakaaless.sksocket.addon.effect;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
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

public class EffSocketConnect extends Effect {

    private Expression<AdaptSocket> socket;
    private Expression<String> ip;

    @Override
    public boolean init(final Expression<?> @NotNull [] exprs, final int matchedPattern, final @NotNull Kleenean isDelayed, final SkriptParser.@NotNull ParseResult parseResult) {
        this.socket = (Expression<AdaptSocket>) exprs[0];
        this.ip = (Expression<String>) exprs[1];
        return true;
    }

    @Override
    public @NotNull String toString(final @Nullable Event e, final boolean debug) {
        return "connect a socket";
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

}
