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
import org.jetbrains.annotations.Nullable;

@Name("Socket Send Data")
@Description("This effect allows you to send data to a server from a socket.")
@Examples({"send data \"Hello world\" from {socket}"})
@Since("1.0.0")
public class EffSocketSendData extends Effect {

    static {
        Skript.registerEffect(EffSocketSendData.class, "send data %string% from socket %socket%");
    }

    private Expression<String> data;
    private Expression<AdaptSocket> socket;

    @Override
    public boolean init(final Expression<?>[] exprs, final int matchedPattern, final Kleenean isDelayed, final SkriptParser.ParseResult parseResult) {
        this.data = (Expression<String>) exprs[0];
        this.socket = (Expression<AdaptSocket>) exprs[1];
        return true;
    }

    @Override
    protected void execute(final Event e) {
        if (this.data.getSingle(e) == null || this.socket.getSingle(e) == null)
            return;
        final String data = this.data.getSingle(e).replace("||", "%nl%");
        final AdaptSocket socket = this.socket.getSingle(e);
        for (final String datas : data.split("%nl%")) {
            socket.getPrintWriter().println(datas);
        }
    }

    @Override
    public String toString(final @Nullable Event e, final boolean debug) {
        return "send data to a server with a stock";
    }

}
