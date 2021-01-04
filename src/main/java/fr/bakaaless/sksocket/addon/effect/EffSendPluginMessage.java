package fr.bakaaless.sksocket.addon.effect;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import fr.bakaaless.sksocket.addon.type.AdaptPluginMessage;
import fr.bakaaless.sksocket.plugin.SkSocket;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

public class EffSendPluginMessage extends Effect {

    static {
        Skript.registerEffect(EffSendPluginMessage.class, "send plugin[]message %pluginmessage% through [channel ]%string%");
    }

    private Expression<AdaptPluginMessage> pluginMessage;
    private Expression<String> channel;

    @Override
    public boolean init(final Expression<?>[] exprs, final int matchedPattern, final Kleenean isDelayed, final SkriptParser.ParseResult parseResult) {
        this.pluginMessage = (Expression<AdaptPluginMessage>) exprs[0];
        this.channel = (Expression<String>) exprs[1];
        return true;
    }

    @Override
    protected void execute(final Event e) {
        if (this.pluginMessage.getSingle(e) == null || this.channel.getSingle(e) == null)
            return;
        SkSocket.get().getServer().sendPluginMessage(SkSocket.get(), this.channel.getSingle(e), this.pluginMessage.getSingle(e).toByteArray());
    }

    @Override
    public String toString(final @Nullable Event e, final boolean debug) {
        return "send plugin message through a registered channel";
    }

}
