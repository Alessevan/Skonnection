package fr.bakaaless.sksocket.plugin;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.classes.Serializer;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.lang.parser.ParserInstance;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.registrations.Classes;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import ch.njol.yggdrasil.Fields;
import fr.bakaaless.sksocket.addon.condition.CondSocketConnected;
import fr.bakaaless.sksocket.addon.effect.*;
import fr.bakaaless.sksocket.addon.event.*;
import fr.bakaaless.sksocket.addon.expression.*;
import fr.bakaaless.sksocket.addon.type.AdaptClient;
import fr.bakaaless.sksocket.addon.type.AdaptServerSocket;
import fr.bakaaless.sksocket.addon.type.AdaptSocket;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.NotSerializableException;
import java.io.StreamCorruptedException;
import java.util.UUID;
import java.util.stream.Collectors;

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
