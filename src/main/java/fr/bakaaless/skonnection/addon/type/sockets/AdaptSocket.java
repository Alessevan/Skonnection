package fr.bakaaless.skonnection.addon.type.sockets;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.lang.parser.ParserInstance;
import ch.njol.skript.registrations.Classes;
import fr.bakaaless.skonnection.addon.event.sockets.EventSocketDisconnect;
import fr.bakaaless.skonnection.addon.event.sockets.EventSocketReceiveData;
import fr.bakaaless.skonnection.plugin.Skonnection;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AdaptSocket {

    static {
        Classes.registerClass(
                new ClassInfo<>(AdaptSocket.class, "socket")
                        .user("sockets?")
                        .name("Socket")
                        .since("1.0.0")
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
    }

    private static final List<AdaptSocket> skriptSockets = new ArrayList<>();

    public static List<AdaptSocket> getSkriptSockets() {
        return skriptSockets;
    }

    @Nullable
    public static AdaptSocket getSkriptSocketById(final UUID uuid) {
        return getSkriptSockets().stream().filter(socket -> socket.getUniqueId().equals(uuid)).findFirst().get();
    }

    private final UUID uniqueId;
    private Socket socket;
    private String ip;
    private BufferedReader reader;
    private PrintWriter writer;
    private Thread readerThread;

    private AdaptSocket() {
        this.uniqueId = UUID.randomUUID();
        this.socket = null;
        this.reader = null;
        this.writer = null;
        getSkriptSockets().add(this);
    }

    public AdaptSocket(final Socket socket) {
        this();
        this.connect(socket);
    }

    public UUID getUniqueId() {
        return this.uniqueId;
    }

    @Nullable
    public Socket getSocket() {
        return this.socket;
    }

    @Nullable
    public String getIp() {
        return this.ip;
    }

    @Nullable
    public PrintWriter getPrintWriter() {
        return this.writer;
    }

    @Nullable
    public BufferedReader getBufferedReader() {
        return this.reader;
    }

    @Nullable
    public Thread getReaderThread() {
        return this.readerThread;
    }

    public boolean isConnected() {
        return this.getSocket() != null && this.getSocket().isConnected() && !this.getSocket().isClosed();
    }

    public void connect(final Socket socket) {
        this.disconnect();
        this.socket = socket;
        this.ip = this.socket.getInetAddress().getHostAddress() + ":" + this.socket.getPort();
        try {
            this.reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.writer = new PrintWriter(this.socket.getOutputStream(), true);
            this.readerThread = new Thread(() -> {
                if (this.getSocket() != null && this.getBufferedReader() != null) {
                    while (this.isConnected()) {
                        try {
                            final String data = this.getBufferedReader().readLine();
                            if (data == null)
                                continue;
                            final EventSocketReceiveData event = new EventSocketReceiveData(this, data);
                            Skonnection.get().getServer().getPluginManager().callEvent(event);
                        } catch (SocketException e) {
                            final EventSocketDisconnect event = new EventSocketDisconnect(this, true);
                            Skonnection.get().getServer().getPluginManager().callEvent(event);
                        } catch (IOException | NullPointerException ignored) {
                        }
                    }
                }
            });
            this.readerThread.setDaemon(true);
            this.readerThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        if (this.getSocket() != null) {
            if (this.getSocket().isConnected()) {
                try {
                    this.getSocket().close();
                    final EventSocketDisconnect event = new EventSocketDisconnect(this, false);
                    Skonnection.get().getServer().getPluginManager().callEvent(event);
                } catch (IOException ignored) {
                }
            }
        } if (this.getPrintWriter() != null) {
            this.getPrintWriter().close();
            this.writer = null;
        } if (this.getBufferedReader() != null) {
            try {
                this.getBufferedReader().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.reader = null;
        } if (this.getReaderThread() != null && this.getReaderThread().isAlive()) {
            this.getReaderThread().interrupt();
            this.readerThread = null;
        }
    }

    public void disconnectAndRemove() {
        this.disconnect();
        getSkriptSockets().remove(this);
    }

}
