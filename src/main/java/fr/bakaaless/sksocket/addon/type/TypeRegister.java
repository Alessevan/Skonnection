package fr.bakaaless.sksocket.addon.type;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.lang.parser.ParserInstance;
import ch.njol.skript.registrations.Classes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class TypeRegister {

    static {
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
    }

}
