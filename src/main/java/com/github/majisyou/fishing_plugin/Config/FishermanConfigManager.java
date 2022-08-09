package com.github.majisyou.fishing_plugin.Config;

import com.github.majisyou.fishing_plugin.Fishing_plugin;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class FishermanConfigManager {
    //ここの説明はいる？要らないと思う

    private static Fishing_plugin plugin = Fishing_plugin.getInstance();
    private static FileConfiguration config = new CustomConfigSetting(plugin,"fisher_man.yml").getConfig();

    private static List<String> recipes;
    private static String sell;
    private static String sell_type;
    private static Integer sell_amount;
    private static Integer sell_CustomModelData;
    private static List<Integer> sell_enchants;
    private static String sell_DisplayName;

    private static String buy;
    private static String buy_type;
    private static Integer buy_amount;
    private static Integer buy_CustomModelData;
    private static List<Integer> buy_enchants;
    private static String buy_DisplayName;

    public static void reload(){
        config = new CustomConfigSetting(plugin,"fisher_man.yml").getConfig();
    }

    public static void loadFishermanRecipes(){
        recipes = config.getStringList("");
    }

    public static void loadFishermanConfig(String recipe){
        String path = "Villager."+recipe;
        sell_type=config.getString(path+".sell.type");
        sell_amount=config.getInt(path+".sell.amount");
        sell_CustomModelData=config.getInt(path+".sell.CustomModelData");
        sell_enchants=config.getIntegerList(path+".sell.enchants");
        sell_DisplayName = config.getString(path+".sell.DisplayName");
        buy_type=config.getString(path+".buy.type");
        buy_amount=config.getInt(path+".buy.amount");
        buy_CustomModelData=config.getInt(path+".buy.CustomModelData");
        buy_enchants=config.getIntegerList(path+".buy.enchants");
        buy_DisplayName = config.getString(path+".buy.DisplayName");
    }

    public static String getSell_type(){return sell_type;}
    public static Integer getSell_amount(){return sell_amount;}
    public static Integer getSell_CustomModelData(){return sell_CustomModelData;}

    public static String getBuy_type(){return buy_type;}
    public static Integer getBuy_amount(){return buy_amount;}
    public static Integer getBuy_CustomModelData(){return buy_CustomModelData;}

}
