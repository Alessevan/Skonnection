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
import fr.bakaaless.skonnection.addon.type.sockets.AdaptServerSocket;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Server Send Data")
@Description("This effect allows you to send data from a server.")
@Examples({"set {_clients::*} to clients of {server}",
            "send data \"You are a wolf\" from server socket {server} to {_clients::1}",
            "send data \"A wolf was chosen\" from server socket {server}"})
@Since("1.0.0")
public class EffServerSendData extends Effect {

    static {
        Skript.registerEffect(EffServerSendData.class, "send data %string% from server[ ][socket] %serversocket% [to %clientsocket%]");
    }

    private Expression<String> data;
    private Expression<AdaptServerSocket> server;
    private Expression<AdaptClient> client;

    @Override
    public boolean init(final Expression<?>[] exprs, final int matchedPattern, final Kleenean isDelayed, final SkriptParser.ParseResult parseResult) {
        this.data = (Expression<String>) exprs[0];
        this.server = (Expression<AdaptServerSocket>) exprs[1];
        if (exprs.length > 2)
            this.client = (Expression<AdaptClient>) exprs[2];
        return true;
    }

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

}
