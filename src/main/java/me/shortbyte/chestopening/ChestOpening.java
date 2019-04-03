package me.shortbyte.chestopening;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author LeonEnkn
 *
 * Copyright (c) 2015 - 2017 by ShortByte.me to present. All rights reserved.
 */
public class ChestOpening extends JavaPlugin {

    @Override
    public void onEnable() {
        init();
    }

    private void init() {
        
    }   

    public void setMetadata(Block block, String name, Object object) {
        if (block.hasMetadata(name))
            removeMetadata(block, name);
        block.setMetadata(name, new FixedMetadataValue(this, object));
    }

    public void setMetadata(Entity entity, String name, Object object) {
        if (entity.hasMetadata(name))
            entity.removeMetadata(name, this);
        entity.setMetadata(name, new FixedMetadataValue(this, object));
    }

    public void removeMetadata(Block block, String name) {
        if (block.hasMetadata(name))
            block.removeMetadata(name, this);
    }

    public void removeMetadata(Player player, String name) {
        if (player.hasMetadata(name))
            player.removeMetadata(name, this);
    }
}
