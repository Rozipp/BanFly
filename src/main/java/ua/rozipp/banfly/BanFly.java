package ua.rozipp.banfly;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public final class BanFly extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onPlayerToggleFlightEvent(PlayerToggleFlightEvent event) {
        Player player = event.getPlayer();
        if (!player.isOp())
            if (player.getGameMode() == GameMode.SURVIVAL || player.getGameMode() == GameMode.ADVENTURE) {
                ItemStack item = player.getInventory().getChestplate();
                event.setCancelled(true);
                if (item != null && item.getType() == Material.ELYTRA) {
                    player.setAllowFlight(true);
                    player.setFlying(event.isFlying());
                } else {
                    player.setAllowFlight(false);
                    player.setFlying(false);
                }
            }
    }

    @EventHandler
    public void onInventoryClick(final InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            Player player = (Player) event.getWhoClicked();
            if (!player.isOp())
                if (player.getGameMode() == GameMode.SURVIVAL || player.getGameMode() == GameMode.ADVENTURE)
                    if ((event.getRawSlot() == 6 && event.getSlot() == 38)
                            || (event.getCurrentItem() != null && event.getCurrentItem().getType() == Material.ELYTRA)
                            || (event.getCursor() != null && event.getCursor().getType() == Material.ELYTRA)) {
                        CheckTask(player);
                    }
        }
    }

    @EventHandler
    public void onDropItem(final PlayerDropItemEvent event) {
        if (event.getItemDrop().getItemStack().getType() == Material.ELYTRA)
            CheckTask(event.getPlayer());
    }

    public void CheckTask(Player player) {
        Bukkit.getScheduler().runTaskLater(this, () -> {
            ItemStack item = player.getInventory().getChestplate();
            if (item != null && item.getType() == Material.ELYTRA) {
                player.setAllowFlight(true);
            } else {
                player.setAllowFlight(false);
                player.setFlying(false);
            }
        }, 1);
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageByBlockEvent e) {
        if (e.getCause() != EntityDamageEvent.DamageCause.FALL) return;

        if (e.getEntity() instanceof Player) {
            Player player = (Player) e.getEntity();
            ItemStack item = player.getInventory().getChestplate();
            if (item != null && item.getType() == Material.ELYTRA) {
                e.setCancelled(true);
            }
        }
    }


}
