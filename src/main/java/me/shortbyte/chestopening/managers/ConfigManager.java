package me.shortbyte.chestopening.managers;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Location;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author LeonEnkn
 *
 * Copyright (c) 2015 - 2017 by ShortByte.de to present. All rights reserved.
 */
public class ConfigManager {

    private JSONObject json;
    private LoadingCache<String, Object> cache;

    public ConfigManager(String path) {
        initCache();

        try {
            Logger.getLogger(ConfigManager.class.getName()).log(Level.INFO, "Loading config {0}", path);
            json = (JSONObject) new JSONParser().parse(new InputStreamReader(new FileInputStream(path), "UTF-8"));
            //      json = (JSONObject) new JSONParser().parse(new FileReader(path));
        } catch (IOException | ParseException ex) {
            Logger.getLogger(ConfigManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //<editor-fold defaultstate="collapsed" desc="initCache">
    private void initCache() {
        
        this.cache = CacheBuilder.newBuilder().maximumSize(1000).expireAfterAccess(30, TimeUnit.SECONDS).build(new CacheLoader<String, Object>() {

            @Override
            public Object load(String key) throws Exception {
                String[] p = key.split("\\.");
                Object out = json;

                for (String p1 : p)
                    out = ((Map) out).get(p1);
                return out;
            }
        });
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="get">
    public Object get(String path) {
        try {
            return cache.get(path);
        } catch (ExecutionException ex) {
            return null;
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getString">
    public String getString(String path) {
        return (String) get(path);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getInt">
    public int getInt(String path) {
        return (int) getLong(path);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getLong">
    public long getLong(String path) {
        return (long) get(path);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getBoolean">
    public boolean getBoolean(String path) {
        return (boolean) get(path);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getFloat">
    public float getFloat(String path) {
        return (float) getDouble(path);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getDouble">
    public double getDouble(String path) {
        return (double) get(path);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getMap">
    public Map getMap(String path) {
        return (Map) get(path);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getList">
    public List getList(String path) {
        return (List) get(path);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getLocation">
    public Location getLocation(String path) {
        try {
            return Location.deserialize((Map) get(path));
        } catch (Exception e) {
            Logger.getLogger(ConfigManager.class.getName()).log(Level.INFO, "Path {0}", path);
            return null;
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getLocations">
    public List<Location> getLocations(String path) {
        List<Map> l = getList(path);
        ArrayList<Location> out = new ArrayList<>();

        l.stream().forEach((m) -> {
            out.add(Location.deserialize(m));
        });
        return out;
    }
    //</editor-fold>

}
