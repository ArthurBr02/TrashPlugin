package fr.arthurbr02.trashplugin;

import fr.arthurbr02.trashplugin.commands.TrashAutoCommand;
import fr.arthurbr02.trashplugin.commands.TrashCommand;
import fr.arthurbr02.trashplugin.listeners.TrashListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {
    FileConfiguration config = getConfig();

    public static void main(String[] args) {
        System.out.println("Hello World!");
    }

    @Override
    public void onEnable() {
        Bukkit.getLogger().info(ChatColor.GREEN + "Enabled " + this.getName());
        this.getCommand("trash").setExecutor(new TrashCommand(this));
        this.getCommand("trashauto").setExecutor(new TrashAutoCommand(this));
        getServer().getPluginManager().registerEvents(new TrashListener(this), this);

        if (!config.contains("defaultMode")) {
            config.set("defaultMode", 1);
        }
        saveConfig();
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info(ChatColor.RED + "Disabled " + this.getName());

    }
}
