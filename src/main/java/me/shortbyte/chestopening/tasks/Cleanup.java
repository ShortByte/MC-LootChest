package me.shortbyte.chestopening.tasks;

import me.shortbyte.chestopening.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.plugin.java.JavaPlugin;

public class Cleanup implements Runnable {

    private static final int WINNING_SLOT = 13;

    private final Player player;
    private final Inventory inventory;
    private final BukkitTask task;

    private int step = 1;

    public Cleanup(JavaPlugin plugin, Player player, Inventory inventory) {
      this.player = player;
      this.inventory = inventory;
      this.task = plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, this, 0L, 20L);
    }

    private void clearSlot(int slot) {
        if (inventory.getItem(slot) != null) {
            inventory.setItem(slot, ItemBuilder.item(Material.AIR).build());
        }
    }

    @Override
    public void run() {
        if (step == 5) {
          return;
        }
        clearSlot(WINNING_SLOT - step);
        clearSlot(WINNING_SLOT + step);
        step++;

        if (step == 5) {
            synchronized(inventory) {
                task.cancel();
                player.getInventory().addItem(inventory.getItem(WINNING_SLOT));
                player.closeInventory();
            }
        }
    }

    public BukkitTask getTask() {
      return this.task;
    }
}