package fr.bakaaless.sksocket.plugin;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.lang.parser.ParserInstance;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.registrations.Classes;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import fr.bakaaless.sksocket.addon.condition.CondSocketConnected;
import fr.bakaaless.sksocket.addon.effect.EffSocketDestroy;
import fr.bakaaless.sksocket.addon.effect.EffSocketDisconnect;
import fr.bakaaless.sksocket.addon.effect.EffSocketSendData;
import fr.bakaaless.sksocket.addon.event.EventSocketReceiveData;
import fr.bakaaless.sksocket.addon.expression.ExprSocketCreate;
import fr.bakaaless.sksocket.addon.expression.ExprSocketUUID;
import fr.bakaaless.sksocket.addon.type.AdaptServerSocket;
import fr.bakaaless.sksocket.addon.type.AdaptSocket;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.UUID;

public class SkSocket extends JavaPlugin {

    public static SkSocket get() {
        return (SkSocket) JavaPlugin.getProvidingPlugin(SkSocket.class);
    }

    @Override
    public void onEnable() {
        Skript.registerAddon(this);
        Classes.registerClass(
                new ClassInfo<>(AdaptSocket.class, "socket")
                        .user("sockets?")
                        .name("Socket")
                        .description("Represents a java socket")
                        .parser(new Parser<AdaptSocket>() {

                            @Nullable
                            @Override
                            public AdaptSocket parse(final String uuid, final ParseContext context, final ParserInstance pi) {
                                return this.parse(uuid, context);
                            }

                            @Nullable
                            @Override
                            public AdaptSocket parse(final String uuid, final ParseContext context) {
                                try {
                                    final UUID realUUID = UUID.fromString(uuid);
                                    return AdaptSocket.getSkriptSocketById(realUUID);
                                } catch (IllegalArgumentException ignored) {
                                }
                                return null;
                            }

                            @Override
                            public String toString(final AdaptSocket socket, final int flags) {
                                return socket.getUniqueId().toString();
                            }

                            @Override
                            public String toVariableNameString(final AdaptSocket socket) {
                                return socket.getUniqueId().toString();
                            }

                            @Override
                            public String getVariableNamePattern() {
                                return ".+";
                            }
                        })
        );
        Classes.registerClass(
                new ClassInfo<>(AdaptServerSocket.class, "socketserver")
                        .user("socketservers?")
                        .name("SocketServer")
                        .description("Represents a java socketserver")
                        .parser(new Parser<AdaptServerSocket>() {

                            @Nullable
                            @Override
                            public AdaptServerSocket parse(final String uuid, final ParseContext context, final ParserInstance pi) {
                                return this.parse(uuid, context);
                            }

                            @Nullable
                            @Override
                            public AdaptServerSocket parse(final @NotNull String uuid, final @NotNull ParseContext context) {
                                try {
                                    final UUID realUUID = UUID.fromString(uuid);
                                    return AdaptServerSocket.getSkriptServerById(realUUID);
                                } catch (IllegalArgumentException ignored) {
                                }
                                return null;
                            }

                            @Override
                            public @NotNull String toString(final @NotNull AdaptServerSocket socket, final int flags) {
                                return socket.getUniqueId().toString();
                            }

                            @Override
                            public @NotNull String toVariableNameString(final @NotNull AdaptServerSocket socket) {
                                return socket.getUniqueId().toString();
                            }

                            @Override
                            public @NotNull String getVariableNamePattern() {
                                return ".+";
                            }
                        })
        );
        Skript.registerCondition(CondSocketConnected.class, "%socket% is connected", "%socket%'s connected", "%socket% is connect", "%socket%'s connect", "%socket% connected", "%socket% connect");
        Skript.registerEffect(EffSocketSendData.class, "send data %string% to [socket ]%socket%");
        Skript.registerEffect(EffSocketDestroy.class, "destroy [socket ]%socket%");
        Skript.registerEffect(EffSocketDisconnect.class, "disconnect [socket ]%socket%");
        Skript.registerEvent("Socket Receive Data Event", SimpleEvent.class, EventSocketReceiveData.class, "[socket ]receive data[ async]");
        EventValues.registerEventValue(EventSocketReceiveData.class, AdaptSocket.class, new Getter<AdaptSocket, EventSocketReceiveData>() {
            @Nullable
            @Override
            public AdaptSocket get(final @NotNull EventSocketReceiveData event) {
                return event.getSocket();
            }
        }, 0);
        EventValues.registerEventValue(EventSocketReceiveData.class, String.class, new Getter<String, EventSocketReceiveData>() {
            @Nullable
            @Override
            public String get(final @NotNull EventSocketReceiveData event) {
                return event.getData();
            }
        }, 0);
        Skript.registerExpression(ExprSocketCreate.class, AdaptSocket.class, ExpressionType.SIMPLE, "create [client ]socket [to ]%string%");
        Skript.registerExpression(ExprSocketUUID.class, String.class, ExpressionType.SIMPLE, "uuid of %socket%", "%socket%'s uuid");
        Skript.registerExpression(ExprSocketUUID.class, String.class, ExpressionType.SIMPLE, "ip of %socket%", "%socket%'s ip");
    }

    @Override
    public void onDisable() {
        AdaptSocket.getSkriptSockets().forEach(AdaptSocket::disconnectAndRemove);
    }
}
