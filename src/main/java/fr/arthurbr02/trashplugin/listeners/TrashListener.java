package fr.arthurbr02.trashplugin.listeners;

import fr.arthurbr02.trashplugin.Main;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class TrashListener implements Listener {
    private Main main;
    public TrashListener(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals(ChatColor.DARK_RED + ChatColor.BOLD.toString() + "Poubelle")) {
            event.setCancelled(true);

            if (InventoryType.PLAYER.equals(event.getClickedInventory().getType())) {
                event.getCurrentItem().setAmount(0);
            }
            return;
        }

        if (event.getView().getTitle().equals(ChatColor.DARK_BLUE + ChatColor.BOLD.toString() + "Filtre de la poubelle automatique")) {
            event.setCancelled(true);

            if (InventoryType.PLAYER.equals(event.getClickedInventory().getType())) {
                if (!event.getInventory().contains(event.getCurrentItem())) {
                    event.getInventory().addItem(event.getCurrentItem());
                }
            }else if (InventoryType.CHEST.equals(event.getClickedInventory().getType())) {
                if (event.getInventory().contains(event.getCurrentItem())) {
                    event.getInventory().remove(event.getCurrentItem());
                }
            }
            return;
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getView().getTitle().equals(ChatColor.DARK_BLUE + ChatColor.BOLD.toString() + "Filtre de la poubelle automatique")) {
            Inventory inv = event.getInventory();
            List<String> items = new ArrayList<>();
            for (ItemStack item : inv.getContents()) {
                if (item != null && !Material.AIR.equals(item.getType())) {
                    items.add(item.getType().toString());

                }
            }
            if (main.getConfig().getConfigurationSection(event.getPlayer().getUniqueId().toString()) == null) {
                main.getConfig().createSection(event.getPlayer().getUniqueId().toString());
            }
            main.getConfig().getConfigurationSection(event.getPlayer().getUniqueId().toString()).set(".filter", items);
            main.saveConfig();
            return;
        }
    }
}
