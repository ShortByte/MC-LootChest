package me.shortbyte.chestopening.tasks;

import org.bukkit.Sound;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import java.util.List;
import java.util.Random;

public class Opening implements Runnable {
    private final static Random RANDOM = new Random(System.currentTimeMillis());
    private final int WINNING_SLOT = 13;

    private final JavaPlugin plugin;
    private final Player player;
    private final Inventory inventory;
    private final List<ItemStack> items;


    private int step = 0;
    private boolean stop = false;
    private BukkitTask task;
    private Cleanup cleanup;

    public Opening(JavaPlugin plugin, Player player, Inventory inventory, List<ItemStack> items) {
        this.plugin = plugin;
        this.player = player;
        this.inventory = inventory;
        this.items = items;
        this.task = this.plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, this, 0L, 1L);
    }

    @Override
    public void run() {
        if (stop) {
          Thread.currentThread().interrupt();
        }

        /**
          <------------------------
          x  x  x  x  o  x  x  x  x
          s  s  s  s  w  s  s  s  s
          x  x  x  x  o  x  x  x  x
        */

        for (int slot = 10; slot <= 16; slot++) {
            if (inventory.getItem(slot) == null)
                continue;
            inventory.setItem(slot - 1, inventory.getItem(slot));
        }
        // add new item to end
        inventory.setItem(17, items.get(RANDOM.nextInt(items.size())));

        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 10F, 10F);
        step++;

        this.task.cancel();
        switch(step) {
          case 60:
            this.task = this.plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, this, 0L, 2L);
            break;
          case 90:
            this.task = this.plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, this, 0L, 4L);
            break;
          case 105:
            this.task = this.plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, this, 0L, 10L);
            break;
          case 110:
            this.task = this.plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, this, 0L, 20L);
            break;
          case 113:
            if (inventory.getItem(WINNING_SLOT) == null) {
                player.sendMessage("Item not found!");
                player.closeInventory();
                return;
            }
            player.sendMessage("Won item : " + inventory.getItem(WINNING_SLOT).getItemMeta().getDisplayName());

            this.cleanup = new Cleanup(plugin, player, inventory);
        }
    }

    public void cancel() {
        this.task.cancel();
        if (this.cleanup != null) {
            synchronized (inventory) {
                if (this.cleanup.getTask().isCancelled​()) {
                    return;
                }
                this.cleanup.getTask().cancel();
                player.getInventory().addItem(inventory.getItem(WINNING_SLOT));
                player.closeInventory();
            }
        }
    }

}