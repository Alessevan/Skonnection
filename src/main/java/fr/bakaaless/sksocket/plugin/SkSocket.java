package fr.bakaaless.sksocket.plugin;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.classes.Serializer;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.lang.parser.ParserInstance;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.registrations.Classes;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import ch.njol.yggdrasil.Fields;
import fr.bakaaless.sksocket.addon.condition.CondSocketConnected;
import fr.bakaaless.sksocket.addon.effect.*;
import fr.bakaaless.sksocket.addon.event.*;
import fr.bakaaless.sksocket.addon.expression.*;
import fr.bakaaless.sksocket.addon.type.AdaptClient;
import fr.bakaaless.sksocket.addon.type.AdaptServerSocket;
import fr.bakaaless.sksocket.addon.type.AdaptSocket;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.NotSerializableException;
import java.io.StreamCorruptedException;
import java.util.UUID;
import java.util.stream.Collectors;

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
                                return socket.toString();
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
                new ClassInfo<>(AdaptServerSocket.class, "serversocket")
                        .user("serversockets?")
                        .name("ServerSocket")
                        .description("Represents a java serversocket")
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
                                return socket.toString();
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
        Classes.registerClass(
                new ClassInfo<>(AdaptClient.class, "clientsocket")
                        .user("clientsockets?")
                        .name("ClientSocket")
                        .description("Represents a java clientsocket")
                        .parser(new Parser<AdaptClient>() {

                            @Nullable
                            @Override
                            public AdaptClient parse(final String uuid, final ParseContext context, final ParserInstance pi) {
                                return this.parse(uuid, context);
                            }

                            @Nullable
                            @Override
                            public AdaptClient parse(final @NotNull String uuid, final @NotNull ParseContext context) {
                                try {
                                    final UUID realUUID = UUID.fromString(uuid);
                                    return AdaptServerSocket.getSkriptServers().stream()
                                            .map(server ->
                                                    server.getClients().stream()
                                                            .filter(adaptClient -> adaptClient.getUniqueId().equals(realUUID))
                                                            .findFirst()
                                                            .get()
                                            )
                                            .findFirst().get();
                                } catch (IllegalArgumentException ignored) {
                                }
                                return null;
                            }

                            @Override
                            public @NotNull String toString(final @NotNull AdaptClient client, final int flags) {
                                return client.toString();
                            }

                            @Override
                            public @NotNull String toVariableNameString(final @NotNull AdaptClient client) {
                                return client.getUniqueId().toString();
                            }

                            @Override
                            public @NotNull String getVariableNamePattern() {
                                return ".+";
                            }
                        })
        );
        Skript.registerCondition(CondSocketConnected.class, "%socket%[ is|'s] connect[ed]");
        Skript.registerEffect(EffSocketSendData.class, "send data %string% from socket %socket%");
        Skript.registerEffect(EffSocketDestroy.class, "destroy socket %socket%");
        Skript.registerEffect(EffSocketDisconnect.class, "disconnect socket %socket%");
        Skript.registerEffect(EffServerSendData.class, "send data %string% from server[ ][socket] %serversocket% [to %clientsocket%]");
        Skript.registerEffect(EffServerDisconnect.class, "destroy server[ ][socket] %serversocket%");
        Skript.registerEffect(EffServerDestroy.class, "disconnect server[ ][socket] %serversocket%");
        Skript.registerEffect(EffClientDisconnect.class, "disconnect client %clientsocket%");
        Skript.registerEvent("Socket Receive Data Event", SimpleEvent.class, EventSocketReceiveData.class, "socket receive data[ async]");
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
        Skript.registerEvent("Client Attempt To Connect", SimpleEvent.class, EventServerReceiveConnection.class, "[client ]attempt to connect[ on server][ async]");
        EventValues.registerEventValue(EventServerReceiveConnection.class, AdaptServerSocket.class, new Getter<AdaptServerSocket, EventServerReceiveConnection>() {
            @Nullable
            @Override
            public AdaptServerSocket get(final @NotNull EventServerReceiveConnection event) {
                return event.getServer();
            }
        }, 0);
        EventValues.registerEventValue(EventServerReceiveConnection.class, AdaptClient.class, new Getter<AdaptClient, EventServerReceiveConnection>() {
            @Nullable
            @Override
            public AdaptClient get(final @NotNull EventServerReceiveConnection event) {
                return event.getClient();
            }
        }, 0);
        Skript.registerEvent("ServerSocket Receive Data", SimpleEvent.class, EventServerReceiveData.class, "server[ ][socket] receive data[ async]");
        EventValues.registerEventValue(EventServerReceiveData.class, AdaptServerSocket.class, new Getter<AdaptServerSocket, EventServerReceiveData>() {
            @Nullable
            @Override
            public AdaptServerSocket get(final @NotNull EventServerReceiveData event) {
                return event.getServer();
            }
        }, 0);
        EventValues.registerEventValue(EventServerReceiveData.class, AdaptClient.class, new Getter<AdaptClient, EventServerReceiveData>() {
            @Nullable
            @Override
            public AdaptClient get(final @NotNull EventServerReceiveData event) {
                return event.getClient();
            }
        }, 0);
        EventValues.registerEventValue(EventServerReceiveData.class, String.class, new Getter<String, EventServerReceiveData>() {
            @Nullable
            @Override
            public String get(final @NotNull EventServerReceiveData event) {
                return event.getData();
            }
        }, 0);
        Skript.registerEvent("Socket Disconnect", SimpleEvent.class, EventSocketDisconnect.class, "socket disconnected");
        EventValues.registerEventValue(EventSocketDisconnect.class, AdaptSocket.class, new Getter<AdaptSocket, EventSocketDisconnect>() {
            @Nullable
            @Override
            public AdaptSocket get(final @NotNull EventSocketDisconnect event) {
                return event.getSocket();
            }
        }, 0);
        Skript.registerEvent("Client Disconnect", SimpleEvent.class, EventClientDisconnect.class, "client disconnected");
        EventValues.registerEventValue(EventClientDisconnect.class, AdaptClient.class, new Getter<AdaptClient, EventClientDisconnect>() {
            @Nullable
            @Override
            public AdaptClient get(final @NotNull EventClientDisconnect event) {
                return event.getClient();
            }
        }, 0);
        Skript.registerExpression(ExprSocketCreate.class, AdaptSocket.class, ExpressionType.SIMPLE, "create [client ]socket [to ]%string%");
        Skript.registerExpression(ExprSocketUUID.class, String.class, ExpressionType.SIMPLE, "[get ]uuid of socket %socket%", "[get ]socket %socket%'s uuid");
        Skript.registerExpression(ExprSocketIP.class, String.class, ExpressionType.SIMPLE, "[get ]ip of socket %socket%", "[get ]socket %socket%'s ip");
        Skript.registerExpression(ExprServerCreate.class, AdaptServerSocket.class, ExpressionType.SIMPLE, "create server[ ][socket] [with port ]%integer%");
        Skript.registerExpression(ExprServerUUID.class, String.class, ExpressionType.SIMPLE, "[get ]uuid of server[ ][socket] %serversocket%", "[get ]server[ ][socket] %serversocket%'s uuid");
        Skript.registerExpression(ExprServerPort.class, Integer.class, ExpressionType.SIMPLE, "[get ]port of server[ ][socket] %serversocket%", "[get ]server[ ][socket] %serversocket%'s port");
        Skript.registerExpression(ExprServerClients.class, AdaptClient.class, ExpressionType.SIMPLE, "[get ]client[s] of server[ ][socket] %serversocket%", "[get ]server[ ][socket] %serversocket%'s client[s]");
        Skript.registerExpression(ExprClientUUID.class, String.class, ExpressionType.SIMPLE, "[get ]uuid of client %clientsocket%", "[get ]client %clientsocket%'s uuid");
        Skript.registerExpression(ExprClientIP.class, String.class, ExpressionType.SIMPLE, "[get ]ip of client %clientsocket%", "[get ]client %clientsocket%'s ip");
        Skript.registerExpression(ExprClientServer.class, AdaptServerSocket.class, ExpressionType.SIMPLE, "[get ]server of client %clientsocket%", "[get ]client %clientsocket%'s server");
    }

    @Override
    public void onDisable() {
        AdaptSocket.getSkriptSockets().forEach(AdaptSocket::disconnect);
        AdaptServerSocket.getSkriptServers().forEach(AdaptServerSocket::disconnect);
    }
}
