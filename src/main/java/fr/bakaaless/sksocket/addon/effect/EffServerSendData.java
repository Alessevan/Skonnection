package fr.bakaaless.sksocket.addon.effect;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import fr.bakaaless.sksocket.addon.type.AdaptClient;
import fr.bakaaless.sksocket.addon.type.AdaptServerSocket;
import fr.bakaaless.sksocket.addon.type.AdaptSocket;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

public class EffServerSendData extends Effect {

    private Expression<String> data;
    private Expression<AdaptServerSocket> server;
    private Expression<AdaptClient> client;

    @Override
    protected void execute(final Event e) {
        if (this.data.getSingle(e) == null)
            return;
        if (this.server == null || this.server.getSingle(e) == null)
            return;
        final String data = this.data.getSingle(e).replace("||", "%nl%");
        final AdaptServerSocket server = this.server.getSingle(e);
        for (final String datas : data.split("%nl%")) {
            if (this.client == null || this.client.getSingle(e) == null) {
                server.getClients().forEach(adaptClient -> adaptClient.getWriter().println(datas));
            } else {
                if (server.getClients().contains(this.client.getSingle(e))) {
                    this.client.getSingle(e).getWriter().println(datas);
                }
            }
        }
    }

    @Override
    public String toString(final @Nullable Event e, final boolean debug) {
        return "send data to one or all clients of a server";
    }

    @Override
    public boolean init(final Expression<?>[] exprs, final int matchedPattern, final Kleenean isDelayed, final SkriptParser.ParseResult parseResult) {
        this.data = (Expression<String>) exprs[0];
        this.server = (Expression<AdaptServerSocket>) exprs[1];
        if (exprs.length > 2)
            this.client = (Expression<AdaptClient>) exprs[2];
        return true;
    }

}
