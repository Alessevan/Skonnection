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
import fr.bakaaless.skonnection.addon.type.sockets.AdaptServerSocket;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Destroy Server")
@Description("This effect allows you to destroy completely a server.")
@Examples({"destroy server socket {server}"})
@Since("1.0.0")
public class EffServerDestroy  extends Effect {

    static {
        Skript.registerEffect(EffServerDisconnect.class, "destroy server[ ][socket] %serversocket%");
    }

    private Expression<AdaptServerSocket> server;

    @Override
    public boolean init(final Expression<?> @NotNull [] exprs, final int matchedPattern, final @NotNull Kleenean isDelayed, final SkriptParser.@NotNull ParseResult parseResult) {
        this.server = (Expression<AdaptServerSocket>) exprs[0];
        return true;
    }

    @Override
    protected void execute(final Event e) {
        final AdaptServerSocket server = this.server.getSingle(e);
        if (server == null)
            return;
        server.destroy();
    }

    @Override
    public @NotNull String toString(final @Nullable Event e, final boolean debug) {
        return "destroy a " + this.server.toString(e, debug);
    }


}
