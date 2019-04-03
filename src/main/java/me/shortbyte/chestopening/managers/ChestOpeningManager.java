package me.shortbyte.chestopening.managers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import me.shortbyte.chestopening.ChestOpening;
import me.shortbyte.chestopening.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

/**
 *
 * @author LeonEnkn
 *
 * Copyright (c) 2015 - 2017 by ShortByte.me to present. All rights reserved.
 */
public class ChestOpeningManager implements Listener {

    private final ChestOpening plugin;

    private Random random = new Random();
    private List<ItemStack> items = new ArrayList<>();

    public ChestOpeningManager(ChestOpening plugin) {
        this.plugin = plugin;
        this.plugin.getServer().getPluginManager().registerEvents(this, plugin);
        init();
    }

    private void init() {
        for (int i = 0; i != 54; i++) {
            Material material = Material.values()[random.nextInt(Material.values().length)];
            String displayName = getRandomChatColor() + getRandomString(15);
            System.out.println(material.name() + "   " + displayName);
            items.add(ItemBuilder.item(material).setDisplayName(displayName).build());
        }
    }

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.getInventory().setItem(0, ItemBuilder.item(Material.CHEST).setDisplayName("§e§lChestOpening").build());
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (player.getItemInHand() == null)
            return;
        if (player.getItemInHand().getType() == null)
            return;
        if (!player.getItemInHand().hasItemMeta())
            return;
        if (!player.getItemInHand().getItemMeta().hasDisplayName())
            return;
        if (player.getItemInHand().getType().equals(Material.CHEST)
                && player.getItemInHand().getItemMeta().getDisplayName().contains("ChestOpening")) {

            player.openInventory(getDefaultInventory());
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if (event.getClickedInventory() == null)
            return;
        if (event.getClickedInventory().getTitle() == null)
            return;
        if (event.getClickedInventory().getTitle().isEmpty())
            return;
        if (event.getClickedInventory().getTitle().contains("§e§lChestOpening"))
            event.setCancelled(true);
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        Player player = (Player) event.getPlayer();

        if (event.getInventory() == null)
            return;
        if (event.getInventory().getTitle() == null)
            return;
        if (event.getInventory().getTitle().isEmpty())
            return;
        if (event.getInventory().getTitle().contains("§e§lChestOpening")) {
            Opening chestOpening = new Opening(plugin, player, event.getInventory(), items);
            plugin.setMetadata(player, "chestOpening", chestOpening);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();

        if (event.getInventory() == null)
            return;
        if (event.getInventory().getTitle() == null)
            return;
        if (event.getInventory().getTitle().isEmpty())
            return;
        if (event.getInventory().getTitle().contains("§e§lChestOpening"))
            if (player.hasMetadata("chestOpening")) {
                Opening chestOpening = (Opening) player.getMetadata("chestOpening").get(0).value();
                chestOpening.getTask().cancel();
                if (chestOpening.getWonTask() != null)
                    chestOpening.getWonTask().cancel();
            }
    }

    private Inventory getDefaultInventory() {
        Inventory inventory = Bukkit.createInventory(null, 27, "§e§lChestOpening");

        for (int i = 0; i != 9; i++)
            inventory.setItem(i, ItemBuilder.item(Material.STAINED_GLASS_PANE, 1, (short) (i == 4 ? 5 : 15))
                    .setDisplayName(" ").build());
        for (int i = 18; i != 27; i++)
            inventory.setItem(i, ItemBuilder.item(Material.STAINED_GLASS_PANE, 1, (short) (i == 22 ? 5 : 15))
                    .setDisplayName(" ").build());
        return inventory;
    }

    public String getRandomChatColor() {

        switch (random.nextInt(16)) {
            case 0:
                return "&a";
            case 1:
                return "&b";
            case 2:
                return "&c";
            case 3:
                return "&d";
            case 4:
                return "&e";
            case 5:
                return "&f";
            case 6:
                return "&0";
            case 7:
                return "&1";
            case 8:
                return "&2";
            case 9:
                return "&3";
            case 10:
                return "&4";
            case 11:
                return "&5";
            case 12:
                return "&6";
            case 13:
                return "&7";
            case 14:
                return "&8";
            case 15:
                return "&9";
            default:
                return "&f";
        }
    }

    public String getRandomString(int maxSize) {
        return UUID.randomUUID().toString().substring(0, maxSize).replaceAll("-", "");
    }

    public class Opening implements Runnable {

        private final ChestOpening plugin;
        private final Player player;
        private final Inventory inventory;
        private final List<ItemStack> list;

        private final Random random = new Random();

        private int[] slots = {9, 10, 11, 12, 13, 14, 15, 16, 17};

        private int step = 0;

        private boolean stop = false;

        private BukkitTask task, wonTask;

        public Opening(ChestOpening plugin, Player player, Inventory inventory, List<ItemStack> list) {
            this.plugin = plugin;
            this.player = player;
            this.inventory = inventory;
            this.list = list;
            this.task = this.plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, this, 0L, 1L);
        }

        @Override
        public void run() {

            if (stop)
                Thread.currentThread().interrupt();

            for (int slot : slots) {
                if (slot == 9)
                    continue;
                if (inventory.getItem(slot) == null)
                    continue;
                inventory.setItem(slot - 1, inventory.getItem(slot));
            }
            inventory.setItem(17, list.get(random.nextInt(list.size())));
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 10F, 10F);
            step++;

            if (step == 60) {
                this.task.cancel();
                this.task = this.plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, this, 0L, 2L);
            }

            if (step == 90) {
                this.task.cancel();
                this.task = this.plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, this, 0L, 4L);
            }

            if (step == 105) {
                this.task.cancel();
                this.task = this.plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, this, 0L, 10L);
            }

            if (step == 110) {
                this.task.cancel();
                this.task = this.plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, this, 0L, 20L);
            }

            if (step == 113) {
                this.task.cancel();
                if (inventory.getItem(13) == null) {
                    player.sendMessage("Item not found!");
                    player.closeInventory();
                    return;
                }

                player.sendMessage("Won item : " + inventory.getItem(13).getItemMeta().getDisplayName());

                wonTask = plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, () -> {

                    if (inventory.getItem(12) != null || inventory.getItem(14) != null) {
                        if (inventory.getItem(12) != null)
                            inventory.setItem(12, new ItemStack(Material.AIR));
                        if (inventory.getItem(14) != null)
                            inventory.setItem(14, new ItemStack(Material.AIR));
                        return;
                    }

                    if (inventory.getItem(11) != null || inventory.getItem(15) != null) {
                        if (inventory.getItem(11) != null)
                            inventory.setItem(11, new ItemStack(Material.AIR));
                        if (inventory.getItem(15) != null)
                            inventory.setItem(15, new ItemStack(Material.AIR));
                        return;
                    }

                    if (inventory.getItem(10) != null || inventory.getItem(16) != null) {
                        if (inventory.getItem(10) != null)
                            inventory.setItem(10, new ItemStack(Material.AIR));
                        if (inventory.getItem(16) != null)
                            inventory.setItem(16, new ItemStack(Material.AIR));
                        return;
                    }

                    if (inventory.getItem(9) != null || inventory.getItem(17) != null) {
                        if (inventory.getItem(9) != null)
                            inventory.setItem(9, new ItemStack(Material.AIR));
                        if (inventory.getItem(17) != null)
                            inventory.setItem(17, new ItemStack(Material.AIR));
                    } else {
                        player.getInventory().addItem(inventory.getItem(13));
                        player.closeInventory();
                        wonTask.cancel();
                    }
                }, 0L, 20L);
            }
        }

        public BukkitTask getTask() {
            return task;
        }

        public BukkitTask getWonTask() {
            return wonTask;
        }

    }
}
