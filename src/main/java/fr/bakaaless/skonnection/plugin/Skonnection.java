package fr.bakaaless.skonnection.plugin;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import fr.bakaaless.skonnection.addon.type.sockets.AdaptServerSocket;
import fr.bakaaless.skonnection.addon.type.sockets.AdaptSocket;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.Messenger;

public class Skonnection extends JavaPlugin {

    public static Skonnection get() {
        return (Skonnection) JavaPlugin.getProvidingPlugin(Skonnection.class);
    }

    public static Messenger getMessenger() {
        return Skonnection.get().getServer().getMessenger();
    }

    @Override
    public void onEnable() {
        final SkriptAddon addon = Skript.registerAddon(this);
        try {
            addon.loadClasses("fr.bakaaless.skonnection.addon", "condition", "effect", "event", "expression", "type");
        } catch (Exception e) {
            Skript.exception(e).printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        AdaptSocket.getSkriptSockets().forEach(AdaptSocket::disconnect);
        AdaptServerSocket.getSkriptServers().forEach(AdaptServerSocket::disconnect);
    }
}
