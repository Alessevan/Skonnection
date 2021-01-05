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
import fr.bakaaless.skonnection.addon.type.sockets.AdaptClient;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

@Name("Disconnect Client")
@Description("This effect allows you to disconnect a client.")
@Examples({"set {_clients::*} to clients of {server}",
        "if size of {_clients::*} > 1:",
        "\tdisconnect client {_client::1}"})
@Since("1.0.0")
public class EffClientDisconnect extends Effect {

    static {
        Skript.registerEffect(EffClientDisconnect.class, "disconnect client %clientsocket%");
    }

    private Expression<AdaptClient> client;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        this.client = (Expression<AdaptClient>) exprs[0];
        return true;
    }

    @Override
    protected void execute(Event e) {
        if (this.client == null || this.client.getSingle(e) == null)
            return;
        Objects.requireNonNull(this.client.getSingle(e)).disconnect();
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "disconnect a " + this.client.toString(e, debug);
    }
}
