package me.shortbyte.chestopening.managers;

import java.util.ArrayList;

/**
 *
 * @author LeonEnkn
 *
 * Copyright (c) 2015 - 2017 by ShortByte.me to present. All rights reserved.
 */
public class ChestOpeningItem {

    private String material;
    private byte data;
    private String displayName;
    private ArrayList<String> lore;

    private int chance;

    public ChestOpeningItem() {
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public byte getData() {
        return data;
    }

    public void setData(byte data) {
        this.data = data;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public ArrayList<String> getLore() {
        return lore;
    }

    public void setLore(ArrayList<String> lore) {
        this.lore = lore;
    }

    public int getChance() {
        return chance;
    }

    public void setChance(int chance) {
        this.chance = chance;
    }

}
