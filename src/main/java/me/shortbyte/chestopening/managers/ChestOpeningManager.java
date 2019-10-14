package me.shortbyte.chestopening.managers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import me.shortbyte.chestopening.ChestOpening;
import me.shortbyte.chestopening.tasks.Opening;
import me.shortbyte.chestopening.utils.ItemBuilder;
import me.shortbyte.chestopening.utils.RandomChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.DyeColor;
import org.bukkit.ChatColor;
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

    private final static Random RANDOM = new Random(System.currentTimeMillis());
    private final static String CHEST_NAME =  String.format("%s%sChestOpening", ChatColor.YELLOW, ChatColor.BOLD);
    private final static String METADATA_CHESTOPENING = "chestopening";
    private final ChestOpening plugin;
    private final List<ItemStack> items;


    public ChestOpeningManager(ChestOpening plugin, int itemCount) {
        this.plugin = plugin;
        this.plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.items = new ArrayList<>(itemCount);

        for (int i = 0; i != itemCount; i++) {
          final Material material = Material.values()[RANDOM.nextInt(Material.values().length)];
          final String displayName = RandomChatColor.getRandomChatColor(RANDOM) + getRandomString(15);
          items.add(ItemBuilder.item(material).setDisplayName(displayName).build());
        }
    }

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final ItemStack stack = ItemBuilder.item(Material.CHEST).setDisplayName(CHEST_NAME).build();
        player.getInventory().setItem(0, stack);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        final Player player = event.getPlayer();

        if (player.getItemInHand() == null)
            return;
        if (player.getItemInHand().getType() == null)
            return;
        if (!player.getItemInHand().hasItemMeta())
            return;
        if (!player.getItemInHand().getType().equals(Material.CHEST))
            return;
        if (!player.getItemInHand().getItemMeta().hasDisplayName())
            return;
        if (!player.getItemInHand().getItemMeta().getDisplayName().equals(CHEST_NAME)) {
            return;
        }
        player.openInventory(getDefaultInventory());
        event.setCancelled(true);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        final Player player = (Player) event.getWhoClicked();

        if (event.getClickedInventory() == null)
            return;
        if (event.getClickedInventory().getTitle() == null)
            return;
        if (event.getClickedInventory().getTitle().isEmpty())
            return;
        if (!event.getClickedInventory().getTitle().equals(CHEST_NAME))
            return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        final Player player = (Player) event.getPlayer();

        if (event.getInventory() == null)
            return;
        if (event.getInventory().getTitle() == null)
            return;
        if (event.getInventory().getTitle().isEmpty())
            return;
        if (!event.getInventory().getTitle().equals(CHEST_NAME))
            return;
        final Opening chestOpening = new Opening(plugin, player, event.getInventory(), items);
        plugin.setMetadata(player, METADATA_CHESTOPENING, chestOpening);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        final Player player = (Player) event.getPlayer();

        if (event.getInventory() == null)
            return;
        if (event.getInventory().getTitle() == null)
            return;
        if (event.getInventory().getTitle().isEmpty())
            return;
        if (!event.getInventory().getTitle().equals(CHEST_NAME))
            return;

        if (player.hasMetadata(METADATA_CHESTOPENING)) {
            final Opening chestOpening = (Opening) player.getMetadata(METADATA_CHESTOPENING).get(0).value();
            chestOpening.cancel();
        }
    }

    private Inventory getDefaultInventory() {
        final int inventoryWidth = 9;
        final int inventoryHeight = 3;
        final Inventory inventory = Bukkit.createInventory(null, inventoryWidth * inventoryHeight, CHEST_NAME);

        /**
          x  x  x  x  o  x  x  x  x
          -  -  -  -  -  -  -  -  -
          x  x  x  x  o  x  x  x  x
        */

        for (int row = 0; row < inventoryHeight; row += 2) {
          for (int col = 0; col < inventoryWidth; col++) {
            final int inventroyIndex = (row * inventoryWidth) + col;
            final short durability = col == 4 ? DyeColor.PURPLE.getDyeData() : DyeColor.ORANGE.getDyeData();

            inventory.setItem(inventroyIndex,
              ItemBuilder.item(Material.STAINED_GLASS_PANE, 1, durability)
                .setDisplayName(" ").build()
            );
          }
        }

        return inventory;
    }

    public String getRandomString(int maxSize) {
        return UUID.randomUUID().toString().substring(0, maxSize).replaceAll("-", "");
    }
}
