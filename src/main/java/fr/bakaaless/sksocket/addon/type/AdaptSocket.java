package fr.bakaaless.sksocket.addon.type;

import fr.bakaaless.sksocket.addon.event.EventSocketDisconnect;
import fr.bakaaless.sksocket.addon.event.EventSocketReceiveData;
import fr.bakaaless.sksocket.plugin.SkSocket;
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
                            SkSocket.get().getServer().getPluginManager().callEvent(event);
                        } catch (SocketException e) {
                            final EventSocketDisconnect event = new EventSocketDisconnect(this);
                            SkSocket.get().getServer().getPluginManager().callEvent(event);
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
        SkSocket.get().getServer().getScheduler().runTaskAsynchronously(SkSocket.get(), () -> {
            final EventSocketDisconnect event = new EventSocketDisconnect(this);
            SkSocket.get().getServer().getPluginManager().callEvent(event);
        });
    }

    public void disconnectAndRemove() {
        this.disconnect();
        getSkriptSockets().remove(this);
    }

}
