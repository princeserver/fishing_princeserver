package com.github.majisyou.fishing_plugin.Config;

import com.github.majisyou.fishing_plugin.Fishing_plugin;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class FishConfigManager {
    //ここの説明いる？

    private static Fishing_plugin plugin = Fishing_plugin.getInstance();
    private static FileConfiguration config = new CustomConfigSetting(plugin,"fish.yml").getConfig();

    private static String Fish_name;
    private static String cm;
    private static String getExp;
    private static String sell_price;
    private static List<String> lore;
    private static List<String> time;
    private static String texture_number;

    public static void LoadFishConfig(int id){
        String file_path = "id."+id+".";
        Fish_name = config.getString(file_path+"name");
        cm = config.getString(file_path+"cm");
        getExp = config.getString(file_path+"getExp");
        sell_price = config.getString(file_path+"sell_price");
        lore = config.getStringList(file_path+"lore");
        time = config.getStringList(file_path+"time");
        texture_number = config.getString(file_path+"texture_number");
    }

    public static String get_cm(){return cm;}
    public static String getName(){return Fish_name;}
    public static String getGetExp(){return getExp;}
    public static String getSell_price(){return sell_price;}
    public static List<String> getLore(){return lore;}
    public static List<String> getTime(){return time;}
    public static String getTexture_number(){return texture_number;}

}
