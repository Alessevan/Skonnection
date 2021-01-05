package fr.bakaaless.skonnection.addon.expression.pluginmessages;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import fr.bakaaless.skonnection.addon.type.pluginmessages.AdaptPluginMessage;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Create Plugin Message")
@Description("This expression allows you to create a plugin message")
@Examples("set {myPluginMessage} to create plugin message")
@Since("1.1.0")
public class ExprPluginMessageCreate extends SimpleExpression<AdaptPluginMessage> {

    static {
        Skript.registerExpression(ExprPluginMessageCreate.class, AdaptPluginMessage.class, ExpressionType.SIMPLE, "create plugin[ ]message");
    }

    @Override
    public boolean init(final Expression<?> @NotNull [] exprs, final int matchedPattern, final @NotNull Kleenean isDelayed, final SkriptParser.@NotNull ParseResult parseResult) {
        return true;
    }

    @Override
    protected AdaptPluginMessage[] get(final @NotNull Event e) {
        return new AdaptPluginMessage[] {new AdaptPluginMessage()};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends AdaptPluginMessage> getReturnType() {
        return AdaptPluginMessage.class;
    }

    @Override
    public @NotNull String toString(final @Nullable Event e, final boolean debug) {
        return "create a plugin message object";
    }
}
