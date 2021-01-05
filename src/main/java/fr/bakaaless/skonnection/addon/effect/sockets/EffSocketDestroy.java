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

@Name("Destroy Socket")
@Description("This effect allows you to destroy completely a socket.")
@Examples({"destroy socket {socket}"})
@Since("1.0.0")
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
        return "destroy a " + this.socket.toString(e, debug);
    }


}
