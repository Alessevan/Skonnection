package fr.bakaaless.sksocket.addon.effect;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import fr.bakaaless.sksocket.addon.type.AdaptSocket;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

public class EffSocketSendData extends Effect {

    private Expression<String> data;
    private Expression<AdaptSocket> socket;

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

    @Override
    public boolean init(final Expression<?>[] exprs, final int matchedPattern, final Kleenean isDelayed, final SkriptParser.ParseResult parseResult) {
        this.data = (Expression<String>) exprs[0];
        this.socket = (Expression<AdaptSocket>) exprs[1];
        return true;
    }

}
