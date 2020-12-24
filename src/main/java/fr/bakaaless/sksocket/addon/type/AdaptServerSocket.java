package fr.bakaaless.sksocket.addon.type;

import org.jetbrains.annotations.Nullable;

import java.net.ServerSocket;
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

    public AdaptServerSocket(final ServerSocket socket) {
        this.uniqueId = UUID.randomUUID();
        this.socket = socket;
        skriptServers.add(this);
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public ServerSocket getSocket() {
        return socket;
    }
}
