package one.hyro.paper;

import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.translation.GlobalTranslator;
import net.kyori.adventure.translation.TranslationRegistry;
import net.kyori.adventure.util.UTF8ResourceBundleControl;
import one.hyro.managers.PermissionManager;
import one.hyro.paper.commands.*;
import one.hyro.paper.events.*;
import one.hyro.paper.managers.TagsManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Locale;
import java.util.ResourceBundle;

public final class HyrosPaper extends JavaPlugin {
    private static HyrosPaper instance;
    private static PermissionManager permissionManager;

    @Override
    public void onEnable() {
        instance = this;

        saveResource("config.yml", false);
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        LifecycleEventManager<Plugin> manager = this.getLifecycleManager();
        manager.registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            final Commands commands = event.registrar();
            commands.register("minigames", "Open the minigames menu", new MinigamesCommand());
            commands.register("ping", "See you server latency", new PingCommand());
            commands.register("hreload", "Reload the plugin configuration", new ReloadCommand());
        });

        getServer().getPluginManager().registerEvents(new AsyncChatPlayerListener(), this);
        getServer().getPluginManager().registerEvents(new InventoryClickListener(), this);
        getServer().getPluginManager().registerEvents(new LobbyPlayerStatusListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerClientOptionsChangeListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(),this);

        TranslationRegistry registry = TranslationRegistry.create(Key.key("paper:i18n"));
        ResourceBundle bundleEN = ResourceBundle.getBundle("translations.en", Locale.ENGLISH, UTF8ResourceBundleControl.get());
        ResourceBundle bundleES = ResourceBundle.getBundle("translations.es", Locale.of("es"), UTF8ResourceBundleControl.get());
        registry.registerAll(Locale.ENGLISH, bundleEN, true);
        registry.registerAll(Locale.forLanguageTag("es"), bundleES, true);
        GlobalTranslator.translator().addSource(registry);

        TagsManager.registerRanksTeams();
        Bukkit.getLogger().info("HyrosPaper has been enabled!");
    }

    @Override
    public void onDisable() {
        TagsManager.unregisterRanksTeams();
        Bukkit.getLogger().info("HyrosPaper has been disabled!");
    }

    public static HyrosPaper getInstance() {
        return instance;
    }

    public static PermissionManager getPermissionManager() {
        if (permissionManager == null) return new PermissionManager(getInstance());
        return permissionManager;
    }
}
