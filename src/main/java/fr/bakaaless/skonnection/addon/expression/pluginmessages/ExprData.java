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
import fr.bakaaless.skonnection.addon.type.pluginmessages.AdaptData;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Name("Retrieve Data")
@Description("This expression allows you to retrieve the data of an incoming plugin message")
@Examples({"on receive plugin message:", "\tset {_output::*} to retrieve event-data"})
@Since("1.1.0")
public class ExprData extends SimpleExpression<String> {

    static {
        Skript.registerExpression(ExprData.class, String.class, ExpressionType.SIMPLE, "retrieve %data%");
    }

    private Expression<AdaptData> data;
    private int parseMark;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        this.data = (Expression<AdaptData>) exprs[0];
        this.parseMark = parseResult.mark;
        return true;
    }

    @Nullable
    @Override
    protected String[] get(Event e) {
        final AdaptData data = this.data.getSingle(e);
        if (data == null)
            return new String[0];
        return data.getContent().toArray(new String[0]);
    }

    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.REMOVE_ALL)
            return null;
        return CollectionUtils.array((parseMark == 1) ? String.class : String[].class);
    }

    @Override
    public void change(final Event e, final @Nullable Object[] delta, final Changer.ChangeMode mode) {
        final AdaptData data = this.data.getSingle(e);
        if (data == null)
            return;
        final List<String> content = Arrays.stream(delta)
                .filter(Objects::nonNull)
                .map(Object::toString)
                .collect(Collectors.toList());
        switch (mode) {
            case ADD:
                for (final String line : content)
                    data.addContent(line);
                break;
            case REMOVE:
                for (final String line : content)
                    data.removeContent(line);
                break;
            case DELETE:
            case RESET:
                for (final String line : data.getContent())
                    data.removeContent(line);
                break;
            case SET:
                for (final String line : data.getContent())
                    data.removeContent(line);
                for (final String line : content)
                    data.addContent(line);
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
        return "retrive " + this.data.toString(e, debug);
    }

}
