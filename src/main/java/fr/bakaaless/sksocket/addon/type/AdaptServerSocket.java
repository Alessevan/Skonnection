package fr.bakaaless.sksocket.addon.type;

import fr.bakaaless.sksocket.addon.event.EventServerReceiveConnection;
import fr.bakaaless.sksocket.plugin.SkSocket;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AdaptServerSocket {

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
                SkSocket.get().getServer().getPluginManager().callEvent(event);
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
