package fr.bakaaless.sksocket.plugin;

import fr.bakaaless.sksocket.addon.event.EventPluginMessageReceiveData;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.ArrayList;

public class PluginMessagesListener implements PluginMessageListener {

    private static PluginMessagesListener instance;

    public static PluginMessagesListener get() {
        if (instance == null)
            instance = new PluginMessagesListener();
        return instance;
    }

    private PluginMessagesListener() {
    }

    @Override
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, @NotNull byte[] message) {
        final ArrayList<String> received = new ArrayList<>();
        final DataInputStream in = new DataInputStream(new ByteArrayInputStream(message));
        try {
            while (in.available() > 0) {
                received.add(in.readUTF());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        final Event event = new EventPluginMessageReceiveData(channel, player, received);
        SkSocket.get().getServer().getPluginManager().callEvent(event);
    }

}
