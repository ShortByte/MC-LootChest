package me.shortbyte.chestopening;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.Metadatable;
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

    public void setMetadata(Metadatable metadatable, String name, Object object) {
        if (metadatable.hasMetadata(name)) {
          metadatable.removeMetadata(name, this);
        }
        metadatable.setMetadata(name, new FixedMetadataValue(this, object));
    }

    public void removeMetadata(Metadatable metadatable, String name) {
        if (metadatable.hasMetadata(name)) {
          metadatable.removeMetadata(name, this);
        }
    }
}
