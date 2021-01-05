package fr.bakaaless.skonnection.addon.type.pluginmessages;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.classes.Serializer;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.lang.parser.ParserInstance;
import ch.njol.skript.registrations.Classes;
import ch.njol.yggdrasil.Fields;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.NotSerializableException;
import java.io.StreamCorruptedException;
import java.util.List;

public class AdaptData {

    static {
        Classes.registerClass(new ClassInfo<>(AdaptData.class, "data")
                .user("datas?")
                .name("Data")
                .since("1.1.0")
                .description("An object to store data")
                .parser(new Parser<AdaptData>() {

                    @Nullable
                    @Override
                    public AdaptData parse(final String uuid, final ParseContext context, final ParserInstance pi) {
                        return this.parse(uuid, context);
                    }

                    @Nullable
                    @Override
                    public AdaptData parse(final @NotNull String string, final @NotNull ParseContext context) {
                        return null;
                    }

                    @Override
                    public @NotNull String toString(final @NotNull AdaptData data, final int flags) {
                        return data.toString();
                    }

                    @Override
                    public @NotNull String toVariableNameString(final @NotNull AdaptData data) {
                        return data.getStringContent();
                    }

                    @Override
                    public @NotNull String getVariableNamePattern() {
                        return ".+";
                    }

                }).serializer(new Serializer<AdaptData>() {
                    @Override
                    public Fields serialize(final AdaptData data) throws NotSerializableException {
                        final Fields fields = new Fields();
                        fields.putObject("data", data.getContent());
                        return fields;
                    }

                    @Override
                    public void deserialize(final AdaptData data, final Fields fields) throws StreamCorruptedException, NotSerializableException {
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


    private final List<String> data;

    public AdaptData(final List<String> data) {
        this.data = data;
    }

    public void addContent(final String data) {
        this.data.add(data);
    }

    public void removeContent(final String data) {
        this.data.remove(data);
    }

    public String getStringContent() {
        final StringBuilder builder = new StringBuilder();
        this.data.forEach(string -> builder.append(string).append(" "));
        return builder.toString();
    }

    public List<String> getContent() {
        return data;
    }

    public byte[] toByteArray() {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        this.data.stream().filter(data -> !data.equals("")).forEach(out::writeUTF);
        return out.toByteArray();
    }
}
