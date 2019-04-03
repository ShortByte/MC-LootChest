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

    private ItemStack item;
    
    public ItemBuilder(Material material) {
        this.item = new ItemStack(material);
    }
    
    public ItemBuilder(Material material, int amount) {
        this.item = new ItemStack(material, amount);
    }
    
    public ItemBuilder(Material material, int amount, short durability) {
        this.item = new ItemStack(material, amount, durability);
    }
    
    public static ItemBuilder item(Material material) {
        return new ItemBuilder(material);
    }
    
    public static ItemBuilder item(Material material, int amount) {
        return new ItemBuilder(material, amount);
    }
    
    public static ItemBuilder item(Material material, int amount, short durability) {
        return new ItemBuilder(material, amount, durability);
    }
    
    public ItemBuilder setDisplayName(String name) {
        ItemMeta meta = this.item.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        this.item.setItemMeta(meta);
        return this;
    }
    
    public ItemBuilder setAmount(int amount) {
        this.item.setAmount(amount);
        return this;
    }
    
    public ItemBuilder setDurability(short durability) {
        this.item.setDurability(durability);
        return this;
    }
    
    public ItemStack build() {
        return this.item;
    }
}
