package fr.bakaaless.sksocket.plugin;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import fr.bakaaless.sksocket.addon.type.AdaptServerSocket;
import fr.bakaaless.sksocket.addon.type.AdaptSocket;
import org.bukkit.plugin.java.JavaPlugin;

public class SkSocket extends JavaPlugin {

    public static SkSocket get() {
        return (SkSocket) JavaPlugin.getProvidingPlugin(SkSocket.class);
    }

    @Override
    public void onEnable() {
        final SkriptAddon addon = Skript.registerAddon(this);
        try {
            addon.loadClasses("fr.bakaaless.sksocket.addon", "condition", "effect", "event", "expression", "type");
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
