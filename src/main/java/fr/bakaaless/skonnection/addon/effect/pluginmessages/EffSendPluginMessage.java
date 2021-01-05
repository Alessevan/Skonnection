package fr.bakaaless.skonnection.addon.effect.pluginmessages;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import fr.bakaaless.skonnection.addon.type.pluginmessages.AdaptPluginMessage;
import fr.bakaaless.skonnection.plugin.Skonnection;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Send Plugin Message")
@Description("This effect allows you to send a plugin message through a registered channel.")
@Examples({"send plugin message {myPluginMessage} through \"BungeeCord\"",
        "send plugin message {myPluginMessage} through \"BungeeCord\" with player"})
@Since("1.1.0")
public class EffSendPluginMessage extends Effect {

    static {
        Skript.registerEffect(EffSendPluginMessage.class, "send plugin[ ]message %pluginmessage% through [channel] %string%", "send plugin[ ]message %pluginmessage% through [channel] %string% with %player%");
    }

    private Expression<AdaptPluginMessage> pluginMessage;
    private Expression<String> channel;
    private Expression<Player> player;

    @Override
    public boolean init(final Expression<?>[] exprs, final int matchedPattern, final Kleenean isDelayed, final SkriptParser.ParseResult parseResult) {
        this.pluginMessage = (Expression<AdaptPluginMessage>) exprs[0];
        this.channel = (Expression<String>) exprs[1];
        if (matchedPattern == 2)
            this.player = (Expression<Player>) exprs[2];
        return true;
    }

    @Override
    protected void execute(final Event e) {
        final String channel = this.channel.getSingle(e);
        final AdaptPluginMessage pluginMessage = this.pluginMessage.getSingle(e);
        if (channel == null || pluginMessage == null)
            return;
        if (this.player != null) {
            final Player player = this.player.getSingle(e);
            if (player != null) {
                System.err.println(player.getName());
                player.sendPluginMessage(Skonnection.get(), channel, pluginMessage.toByteArray());
                return;
            }
        }
        Skonnection.get().getServer().sendPluginMessage(Skonnection.get(), channel, pluginMessage.toByteArray());
    }

    @Override
    public String toString(final @Nullable Event e, final boolean debug) {
        return "send " + this.pluginMessage.toString(e, debug) + " through a registered channel (" + this.channel.toString(e, debug) + ") and with an optional " + this.player.toString(e, debug);
    }

}
