package fr.bakaaless.skonnection.addon.type.pluginmessages;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.classes.Serializer;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.lang.parser.ParserInstance;
import ch.njol.skript.registrations.Classes;
import ch.njol.yggdrasil.Fields;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.NotSerializableException;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.List;

public class AdaptPluginMessage extends AdaptData {

    static {
        Classes.registerClass(new ClassInfo<>(AdaptPluginMessage.class, "pluginmessage")
                .user("pluginmessages?")
                .name("PluginMessage")
                .since("1.1.0")
                .description("An object to store data")
                .parser(new Parser<AdaptPluginMessage>() {

                    @Nullable
                    @Override
                    public AdaptPluginMessage parse(final String uuid, final ParseContext context, final ParserInstance pi) {
                        return this.parse(uuid, context);
                    }

                    @Nullable
                    @Override
                    public AdaptPluginMessage parse(final @NotNull String string, final @NotNull ParseContext context) {
                        return null;
                    }

                    @Override
                    public @NotNull String toString(final @NotNull AdaptPluginMessage pluginMessage, final int flags) {
                        return pluginMessage.toString();
                    }

                    @Override
                    public @NotNull String toVariableNameString(final @NotNull AdaptPluginMessage pluginMessage) {
                        return pluginMessage.getStringContent();
                    }

                    @Override
                    public @NotNull String getVariableNamePattern() {
                        return ".+";
                    }

                }).serializer(new Serializer<AdaptPluginMessage>() {
                    @Override
                    public Fields serialize(final AdaptPluginMessage data) throws NotSerializableException {
                        final Fields fields = new Fields();
                        fields.putObject("data", data.getContent());
                        return fields;
                    }

                    @Override
                    public void deserialize(final AdaptPluginMessage data, final Fields fields) throws StreamCorruptedException, NotSerializableException {
                        data.getContent().forEach(data::removeContent);
                        ((List<String>) fields.getObject("data")).forEach(data::addContent);
                    }

                    @Override
                    public boolean mustSyncDeserialization() {
                        return false;
                    }

                    @Override
                    protected boolean canBeInstantiated() {
                        return false;
                    }
                })
        );
    }

    public AdaptPluginMessage() {
        super(new ArrayList<>());
    }

}
