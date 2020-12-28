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

public class EffSocketDestroy extends Effect {

    static {
        Skript.registerEffect(EffSocketDestroy.class, "destroy socket %socket%");
    }

    private Expression<AdaptSocket> socket;

    @Override
    public boolean init(final Expression<?> @NotNull [] exprs, final int matchedPattern, final @NotNull Kleenean isDelayed, final SkriptParser.@NotNull ParseResult parseResult) {
        this.socket = (Expression<AdaptSocket>) exprs[0];
        return true;
    }

    @Override
    protected void execute(final Event e) {
        final AdaptSocket socket = this.socket.getSingle(e);
        if (socket == null)
            return;
        socket.disconnectAndRemove();
    }

    @Override
    public @NotNull String toString(final @Nullable Event e, final boolean debug) {
        return "disconnect a socket";
    }


}
