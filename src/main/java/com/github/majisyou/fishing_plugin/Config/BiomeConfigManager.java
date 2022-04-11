package com.github.majisyou.fishing_plugin.Config;

import com.github.majisyou.fishing_plugin.Fishing_plugin;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class BiomeConfigManager {
    //ここも説明しない！

    private static Fishing_plugin plugin = Fishing_plugin.getInstance();

    private static Integer FaileProbability;
    private static List<Integer> fish_id;

    public static void loadBiome(FileConfiguration config,String rank){
        FaileProbability= config.getInt(rank+".FaileProbability");
        fish_id = config.getIntegerList(rank+".id");
    }

    public static Integer getFaileProbability(){return FaileProbability;}
    public static List<Integer> getFish_id(){return fish_id;}

}
