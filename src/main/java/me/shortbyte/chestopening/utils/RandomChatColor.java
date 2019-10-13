package me.shortbyte.chestopening.utils;

import org.bukkit.ChatColor;
import java.util.Random;

public interface RandomChatColor {

  public static String getRandomChatColor(Random random) {
    return ChatColor.values()[random.nextInt(16)].toString();
  }
}