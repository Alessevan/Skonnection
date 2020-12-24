package fr.bakaaless.sksocket.addon.effect;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import fr.bakaaless.sksocket.addon.type.AdaptServerSocket;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EffServerDestroy  extends Effect {

    private Expression<AdaptServerSocket> server;

    @Override
    public boolean init(final Expression<?> @NotNull [] exprs, final int matchedPattern, final @NotNull Kleenean isDelayed, final SkriptParser.@NotNull ParseResult parseResult) {
        this.server = (Expression<AdaptServerSocket>) exprs[0];
        return true;
    }

    @Override
    public @NotNull String toString(final @Nullable Event e, final boolean debug) {
        return "destroy a serversocket";
    }

    @Override
    protected void execute(final Event e) {
        final AdaptServerSocket server = this.server.getSingle(e);
        if (server == null)
            return;
        server.destroy();
    }

}
