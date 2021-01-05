package fr.bakaaless.skonnection.addon.expression.pluginmessages;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import fr.bakaaless.skonnection.addon.type.pluginmessages.AdaptPluginMessage;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Name("Plugin Message's Data")
@Description("This expression allows you to retrieve the data of a plugin message")
@Examples({"add \"KickPlayer\" to data of {myPluginMessage}",
            "remove \"KickPlayer\" from data of {myPluginMessage}"})
@Since("1.1.0")
public class ExprPluginMessageData extends SimpleExpression<String> {

    static {
        Skript.registerExpression(ExprPluginMessageData.class, String.class, ExpressionType.SIMPLE, "data of %pluginmessage%");
    }

    private Expression<AdaptPluginMessage> pluginMessage;
    private int parseMark;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        this.pluginMessage = (Expression<AdaptPluginMessage>) exprs[0];
        this.parseMark = parseResult.mark;
        return true;
    }

    @Nullable
    @Override
    protected String[] get(Event e) {
        final AdaptPluginMessage pluginMessage = this.pluginMessage.getSingle(e);
        if (pluginMessage == null)
            return new String[0];
        return pluginMessage.getContent().toArray(new String[0]);
    }

    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.REMOVE_ALL)
            return null;
        return CollectionUtils.array((parseMark == 1) ? String.class : String[].class);
    }

    @Override
    public void change(final Event e, final @Nullable Object[] delta, final Changer.ChangeMode mode) {
        final AdaptPluginMessage pluginMessage = this.pluginMessage.getSingle(e);
        if (pluginMessage == null)
            return;
        final List<String> content = Arrays.stream(delta)
                .filter(Objects::nonNull)
                .map(Object::toString)
                .collect(Collectors.toList());
        switch (mode) {
            case ADD:
                for (final String line : content)
                    pluginMessage.addContent(line);
                break;
            case REMOVE:
                for (final String line : content)
                    pluginMessage.removeContent(line);
                break;
            case DELETE:
            case RESET:
                for (final String line : pluginMessage.getContent())
                    pluginMessage.removeContent(line);
                break;
            case SET:
                for (final String line : pluginMessage.getContent())
                    pluginMessage.removeContent(line);
                for (final String line : content)
                    pluginMessage.addContent(line);
        }
    }

    @Override
    public boolean isSingle() {
        return parseMark == 1;
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "data of " + this.pluginMessage.toString(e, debug);
    }

}
