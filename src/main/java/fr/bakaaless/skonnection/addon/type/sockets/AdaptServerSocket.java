package fr.bakaaless.skonnection.addon.type.sockets;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.lang.parser.ParserInstance;
import ch.njol.skript.registrations.Classes;
import fr.bakaaless.skonnection.addon.event.sockets.EventServerReceiveConnection;
import fr.bakaaless.skonnection.plugin.Skonnection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AdaptServerSocket {

    static {
        Classes.registerClass(
                new ClassInfo<>(AdaptServerSocket.class, "serversocket")
                        .user("serversockets?")
                        .name("ServerSocket")
                        .since("1.0.0")
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
    }

    private static final List<AdaptServerSocket> skriptServers = new ArrayList<>();

    public static List<AdaptServerSocket> getSkriptServers() {
        return skriptServers;
    }

    @Nullable
    public static AdaptServerSocket getSkriptServerById(final UUID uuid) {
        return skriptServers.stream().filter(server -> server.getUniqueId().equals(uuid)).findFirst().get();
    }

    private final UUID uniqueId;
    private final ServerSocket socket;
    private final List<AdaptClient> clients;
    private final Thread connections;

    public AdaptServerSocket(final ServerSocket socket) {
        this.uniqueId = UUID.randomUUID();
        this.socket = socket;
        this.clients = new ArrayList<>();
        this.connections = new Thread(this::waitForConnections);
        this.connections.setDaemon(true);
        this.connections.start();
        skriptServers.add(this);
    }

    public void waitForConnections() {
        while(!this.getSocket().isClosed()) {
            try {
                final Socket socket = this.socket.accept();
                final AdaptClient client = new AdaptClient(this, socket);
                final EventServerReceiveConnection event = new EventServerReceiveConnection(this, client);
                Skonnection.get().getServer().getPluginManager().callEvent(event);
                if (event.isCancelled()) {
                    client.disconnect();
                } else {
                    this.clients.add(client);
                }
            } catch (IOException | NullPointerException ignored) {
            }
        }
    }

    public void disconnect() {
        if (!this.getSocket().isClosed()) {
            try {
                this.clients.forEach(AdaptClient::disconnect);
                this.clients.clear();
                this.connections.interrupt();
                this.socket.close();
            } catch (Throwable ignored) {
            }
        }
    }

    public void forceDisconnect() {
        if (!this.getSocket().isClosed()) {
            try {
                this.clients.forEach(AdaptClient::forceDisconnect);
                this.clients.clear();
                this.connections.interrupt();
                this.socket.close();
            } catch (Throwable ignored) {
            }
        }
    }

    public void destroy() {
        this.disconnect();
        skriptServers.remove(this);
        try {
            this.finalize();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    public UUID getUniqueId() {
        return this.uniqueId;
    }

    public ServerSocket getSocket() {
        return this.socket;
    }

    public List<AdaptClient> getClients() {
        return this.clients;
    }

}
