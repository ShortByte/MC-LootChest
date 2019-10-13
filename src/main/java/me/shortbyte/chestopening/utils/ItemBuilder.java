package me.shortbyte.chestopening.utils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author LeonEnkn
 *
 * Copyright (c) 2015 - 2017 by ShortByte.de to present. All rights reserved.
 */
public class ItemBuilder {

    private final ItemStack item;

    public ItemBuilder(Material material, int amount) {
        this.item = new ItemStack(material, amount);
    }

    public static ItemBuilder item(Material material) {
        return item(material, 1);
    }

    public static ItemBuilder item(Material material, int amount) {
        return new ItemBuilder(material, amount);
    }

    public static ItemBuilder item(Material material, int amount, short damage) {
        final ItemBuilder builder = new ItemBuilder(material, amount);
        return builder.setDurability(damage);
    }

    public ItemBuilder setDisplayName(String name) {
        final ItemMeta meta = this.item.getItemMeta();
        if (meta != null) {
          meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
          this.item.setItemMeta(meta);
        }
        return this;
    }

    public ItemBuilder setAmount(int amount) {
        this.item.setAmount(amount);
        return this;
    }

    public ItemBuilder setDurability(short damage) {
        this.item.setDurability(damage);
        return this;
    }

    public ItemStack build() {
        return this.item;
    }
}
