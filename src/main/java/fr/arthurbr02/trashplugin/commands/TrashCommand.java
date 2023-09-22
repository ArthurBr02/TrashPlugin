package fr.arthurbr02.trashplugin.commands;

import fr.arthurbr02.trashplugin.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class TrashCommand implements CommandExecutor {
    private Main main;
    FileConfiguration config;

    public TrashCommand(Main main) {
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

        if (config.getConfigurationSection(player.getUniqueId().toString()) == null) {
            config.createSection(player.getUniqueId().toString());
            config.getConfigurationSection(player.getUniqueId().toString()).set(".mode", config.getInt("defaultMode"));
            main.saveConfig();
        }

        if (args.length > 2) {
            player.sendMessage("Commande incorrecte: " + command.getUsage());
            return true;

        }

        int mode = 0;

        if (args.length == 2 && args[0].equals("mode")) {
            if (args[1].equals("1")) {
                config.getConfigurationSection(player.getUniqueId().toString()).set(".mode", 1);
                main.saveConfig();
                player.sendMessage("Trash: Mode 1");

            } else if (args[1].equals("2")) {
                config.getConfigurationSection(player.getUniqueId().toString()).set(".mode", 2);
                main.saveConfig();
                player.sendMessage("Trash: Mode 2");

            } else {
                player.sendMessage("Commande incorrecte: " + command.getUsage());

            }
            return true;

        } else if (args.length == 0) {
            mode = config.getInt(player.getUniqueId() + ".mode");

        } else if (args.length == 1) {
            switch (args[0]) {
                case "safe":
                    mode = 1;
                    break;
                case "unsafe":
                    mode = 2;
                    break;
                case "filter":
                    mode = 3;
                    break;
                default:
                    player.sendMessage("Commande incorrecte: " + command.getUsage());
                    return true;

            }
        } else {
            player.sendMessage("Commande incorrecte: " + command.getUsage());
            return true;

        }

        Inventory inventory;

        switch (mode) {
            case 1:
                // Mode 1: Inventaire coffre
                inventory = Bukkit.createInventory(null, 54, ChatColor.DARK_GREEN + ChatColor.BOLD.toString() + "Poubelle sécurisée");
                break;
            case 2:
                // Mode 2: unsafeTrash
                inventory = Bukkit.createInventory(null, 9, ChatColor.DARK_RED + ChatColor.BOLD.toString() + "Poubelle");

                ItemStack grayGlassPane = new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1);
                ItemMeta im = grayGlassPane.getItemMeta();
                assert im != null;
                im.setDisplayName(" ");
                grayGlassPane.setItemMeta(im);
                for (int i = 0; i < 9; i++) {
                    if (i != 4) inventory.setItem(i, grayGlassPane);
                }

                ItemStack redGlassPane = new ItemStack(Material.RED_STAINED_GLASS_PANE, 1);
                im = redGlassPane.getItemMeta();
                assert im != null;
                im.setDisplayName(" ");
                List<String> lores = new ArrayList<>();
                lores.add(ChatColor.DARK_RED + ChatColor.BOLD.toString() + "/!\\ Attention /!\\");
                lores.add(ChatColor.DARK_RED + ChatColor.BOLD.toString() + "Les objets sont supprimés dès");
                lores.add(ChatColor.DARK_RED + ChatColor.BOLD.toString() + "que tu cliqueras dessus !");
                lores.add(" ");
                lores.add(ChatColor.DARK_RED + ChatColor.BOLD.toString() + "Sois prudent !");
                im.setLore(lores);
                redGlassPane.setItemMeta(im);
                inventory.setItem(4, redGlassPane);
                break;
            case 3:
                // Mode 3: filterTrash
                List<String> items = new ArrayList<>();
                if (config.getConfigurationSection(player.getUniqueId().toString()).contains(".filter")) {
                    config.getConfigurationSection(player.getUniqueId().toString()).getStringList(".filter").forEach(item -> items.add(item));

                } else {
                    config.getConfigurationSection(player.getUniqueId().toString()).set(".filter", items);
                    main.saveConfig();

                }

                inventory = Bukkit.createInventory(null, 54, ChatColor.DARK_BLUE + ChatColor.BOLD.toString() + "Filtre de la poubelle automatique");
                for (int i = 0; i < 54; i++) {
                    if (i < items.size()) {
                        inventory.setItem(i, new ItemStack(Material.valueOf(items.get(i))));
                    } else {
                        inventory.setItem(i, new ItemStack(Material.AIR));
                    }
                }
                break;
            default:
                player.sendMessage("Erreur: mode inconnu");
                return true;

        }

        player.openInventory(inventory);

        return true;
    }
}
