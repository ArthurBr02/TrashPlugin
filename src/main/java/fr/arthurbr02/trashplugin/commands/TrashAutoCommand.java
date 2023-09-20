package fr.arthurbr02.trashplugin.commands;

import fr.arthurbr02.trashplugin.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class TrashAutoCommand implements CommandExecutor {
    private Main main;
    FileConfiguration config;

    public TrashAutoCommand(Main main) {
        this.main = main;
        config = main.getConfig();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) { // If the sender is not a player
            sender.sendMessage("You must be a player to use this command");
            return true;
        }
        Player player = (Player) sender;

        if (!config.contains(player.getUniqueId().toString())) {
            config.getConfigurationSection(player.getUniqueId().toString()).set(".filter", new ArrayList<>());
            main.saveConfig();
        }

        if (args.length != 0) {
            player.sendMessage("Commande incorrecte: " + command.getUsage());
            return true;

        }

        List<String> filter = config.getStringList(player.getUniqueId().toString() + ".filter");
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && !Material.AIR.equals(item.getType())) {
                if (filter.contains(item.getType().toString())) {
                    item.setAmount(0);

                }

            }

        }

        return true;
    }
}
