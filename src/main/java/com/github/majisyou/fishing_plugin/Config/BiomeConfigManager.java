package com.github.majisyou.fishing_plugin.Config;

import com.github.majisyou.fishing_plugin.Fishing_plugin;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class BiomeConfigManager {

    private static Fishing_plugin plugin = Fishing_plugin.getInstance();
    private static FileConfiguration config = new CustomConfigSetting(plugin,"Biome.yml").getConfig();

    private static List<Integer> fish_id;

    public static void reload(){
        config = new CustomConfigSetting(plugin,"Biome.yml").getConfig();
    }

    public static void loadBiome(String biome,String time,Integer rank){
        String filepath = "Biome."+biome+".time."+time+"."+rank;
        fish_id = config.getIntegerList(filepath);
    }

    public static List<Integer> getFish_id(){return fish_id;}

}
