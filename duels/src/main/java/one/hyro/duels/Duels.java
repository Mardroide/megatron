package one.hyro.duels;

import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import lombok.Getter;
import one.hyro.duels.commands.JoinCommand;
import one.hyro.duels.events.BlockBreakListener;
import one.hyro.duels.events.BlockPlaceListener;
import one.hyro.instances.GameSession;
import one.hyro.instances.Minigame;
import one.hyro.managers.BlockManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class Duels extends JavaPlugin implements Minigame {
    @Getter
    private static Duels instance;
    private final BlockManager blockManager = new BlockManager();

    @Override
    public void onEnable() {
        instance = this;

        LifecycleEventManager<Plugin> manager = this.getLifecycleManager();
        manager.registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            final Commands commands = event.registrar();
            commands.register("join", "Join a duels game", new JoinCommand());
        });

        getServer().getPluginManager().registerEvents(new BlockBreakListener(), this);
        getServer().getPluginManager().registerEvents(new BlockPlaceListener(), this);

        Bukkit.getLogger().info("Duels has been enabled!");
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info("Duels has been disabled!");
    }

    @Override
    public void waiting(GameSession session) {

    }

    @Override
    public void starting(GameSession session) {

    }

    @Override
    public void inGame(GameSession session) {

    }

    @Override
    public void ending(GameSession session) {
        blockManager.removeBlocksFromSession(session);
    }
}