package com.github.majisyou.fishing_plugin.Config;

import com.github.majisyou.fishing_plugin.Fishing_plugin;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class FishConfigManager {
    //ここの説明いる？

    private static Fishing_plugin plugin = Fishing_plugin.getInstance();
    private static FileConfiguration config = new CustomConfigSetting(plugin,"fish.yml").getConfig();

    private static String Fish_name;
    private static Double cm;
    private static Integer getExp;
    private static Integer sell_price;
    private static List<String> lore;
    private static Integer texture_number;
    private static Integer rank;

    public static void reload(){
        config = new CustomConfigSetting(plugin,"fish.yml").getConfig();
    }

    public static void LoadFishConfig(int id){
        String file_path = "id."+id+".";
        Fish_name = config.getString(file_path+"name");
        cm = config.getDouble(file_path+"cm");
        getExp = config.getInt(file_path+"getExp");
        sell_price = config.getInt(file_path+"sell_price");
        lore = config.getStringList(file_path+"lore");
        texture_number = config.getInt(file_path+"texture_number");
        rank = config.getInt(file_path+"rank");
    }

    public static Double get_cm(){return cm;}
    public static String getName(){return Fish_name;}
    public static Integer getGetExp(){return getExp;}
    public static Integer getSell_price(){return sell_price;}
    public static List<String> getLore(){return lore;}
    public static Integer getTexture_number(){return texture_number;}
    public static Integer getRank(){return rank;}

}
