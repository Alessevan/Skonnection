package fr.bakaaless.skonnection.addon.expression.encryption;

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
import fr.bakaaless.skonnection.plugin.encryption.AESEncryption;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Encrypt String")
@Description("This expression allows you to encrypt a string with the AES system. A key is necessary.")
@Examples({"on chat:",
        "\tset {_encrypt} to aes encrypt \"%message%\" with key \"mySuperKey\"",
        "\tloop all players:",
        "\t\tif loop-player has permission \"god\":",
        "\t\t\tset {_decrypt} to aes decrypt \"%{_encrypt}%\" with key \"mySuperKey\"",
        "\t\t\tsend \"%player% : %{_decrypt}%\" to loop-player",
        "\t\telse:",
        "\t\t\tsend \"%player% : %{_encrypt}%\" to loop-player"})
@Since("1.2.0")
public class ExprEncryptString extends SimpleExpression<String> {

    static {
        Skript.registerExpression(ExprEncryptString.class, String.class, ExpressionType.SIMPLE, "[aes] encrypt %string% [with] key %string%");
    }

    private Expression<String> message;
    private Expression<String> key;

    @Override
    public boolean init(final Expression<?> @NotNull [] exprs, final int matchedPattern, final @NotNull Kleenean isDelayed, final SkriptParser.@NotNull ParseResult parseResult) {
        this.message = (Expression<String>) exprs[0];
        this.key = (Expression<String>) exprs[1];
        return true;
    }

    @Override
    protected String[] get(final @NotNull Event e) {
        if (this.message == null || this.message.getSingle(e) == null)
            return new String[0];
        if (this.key == null || this.key.getSingle(e) == null)
            return new String[]{this.message.getSingle(e)};
        final AESEncryption encryption = AESEncryption.getOrCreate(this.key.getSingle(e));
        return new String[] {encryption.encrypt(this.message.getSingle(e))};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    public @NotNull String toString(final @Nullable Event e, final boolean debug) {
        return "encrypt a " + this.message.toString(e, debug) + "message with a " + this.key.toString(e, debug) + " key";
    }

}
