package fr.bakaaless.skonnection.addon.type.sockets;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.lang.parser.ParserInstance;
import ch.njol.skript.registrations.Classes;
import fr.bakaaless.skonnection.addon.event.sockets.EventClientDisconnect;
import fr.bakaaless.skonnection.addon.event.sockets.EventServerReceiveData;
import fr.bakaaless.skonnection.plugin.Skonnection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.UUID;

public class AdaptClient {

    static {
        Classes.registerClass(
                new ClassInfo<>(AdaptClient.class, "clientsocket")
                        .user("clientsockets?")
                        .name("ClientSocket")
                        .since("1.0.0")
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

    private final UUID uniqueId;
    private final AdaptServerSocket server;
    private final Socket socket;
    private final String ip;
    private BufferedReader reader;
    private Thread readThread;
    private PrintWriter writer;

    public AdaptClient(final AdaptServerSocket server, final Socket socket) {
        this.uniqueId = UUID.randomUUID();
        this.server = server;
        this.socket = socket;
        this.ip = this.socket.getInetAddress().getHostAddress();
        this.reader = null;
        this.writer = null;
        try {
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.writer = new PrintWriter(socket.getOutputStream(), true);
            this.readThread = new Thread(this::read);
            this.readThread.setDaemon(true);
            this.readThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void read() {
        while(this.isConnected()) {
            try {
                final String data = this.reader.readLine();
                if (data == null)
                    continue;
                final Thread catchData = new Thread(() -> {
                    synchronized (this.server.getClients()) {
                        if (!this.server.getClients().contains(this)) {
                            for (int i = 0; i < 10 && !this.server.getClients().contains(this); i++) {
                                try {
                                    Thread.sleep(100);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                if (i == 9 && !this.server.getClients().contains(this))
                                    return;
                            }
                        }
                    }
                    final EventServerReceiveData event = new EventServerReceiveData(this.server, this, data);
                    Skonnection.get().getServer().getPluginManager().callEvent(event);
                });
                catchData.setDaemon(true);
                catchData.start();
            } catch (SocketException e) {
                final EventClientDisconnect event = new EventClientDisconnect(this, true);
                Skonnection.get().getServer().getPluginManager().callEvent(event);
            } catch (IOException | NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isConnected() {
        return this.getSocket() != null && this.getSocket().isConnected() && !this.getSocket().isClosed();
    }

    public void disconnect() {
        if (!this.getSocket().isClosed()) {
            try {
                this.getSocket().close();
                this.getReader().close();
                this.getWriter().close();
                this.readThread.interrupt();
                this.server.getClients().remove(this);
                final EventClientDisconnect event = new EventClientDisconnect(this, false);
                Skonnection.get().getServer().getPluginManager().callEvent(event);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    public void forceDisconnect() {
        if (!this.getSocket().isClosed()) {
            try {
                this.getSocket().close();
                this.getReader().close();
                this.getWriter().close();
                this.readThread.interrupt();
                this.server.getClients().remove(this);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    public UUID getUniqueId() {
        return this.uniqueId;
    }

    public AdaptServerSocket getServer() {
        return this.server;
    }

    public Socket getSocket() {
        return this.socket;
    }

    public String getIp() {
        return this.ip;
    }

    public BufferedReader getReader() {
        return this.reader;
    }

    public PrintWriter getWriter() {
        return this.writer;
    }

}
