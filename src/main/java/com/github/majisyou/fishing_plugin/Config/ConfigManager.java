package com.github.majisyou.fishing_plugin.Config;

import com.github.majisyou.fishing_plugin.Fishing_plugin;
import org.bukkit.configuration.Configuration;

import java.util.List;

public class ConfigManager {
    //ここも説明しない！

    private static Fishing_plugin plugin = Fishing_plugin.getInstance();
    private static Configuration config = plugin.getConfig();
    private static String servername;
    private static Integer MaxRank;
    private static List<String> Biome;
    private static List<String> Time;

    private static double none;
    private static double one;
    private static double two;


    private static Integer rank1;
    private static Integer rank2;
    private static Integer rank3;
    private static Integer rank4;
    private static Integer rank5;
    private static Integer rank6;
    private static Integer rank7;
    private static Integer rank8;
    private static Integer rank9;


    public static void loadConfig(){
        servername = config.getString("servername");
        Biome = config.getStringList("Biome");
        Time = config.getStringList("Time");

        none = config.getDouble("star.none");
        one = config.getDouble("star.one");
        two = config.getDouble("star.two");

        rank1 = config.getInt("rank_number.rank.1");
        rank2 = config.getInt("rank_number.rank.2");
        rank3 = config.getInt("rank_number.rank.3");
        rank4 = config.getInt("rank_number.rank.4");
        rank5 = config.getInt("rank_number.rank.5");
        rank6 = config.getInt("rank_number.rank.6");
        rank7 = config.getInt("rank_number.rank.7");
        rank8 = config.getInt("rank_number.rank.8");
        rank9 = config.getInt("rank_number.rank.9");

    }

    public static String getServername(){
        return servername;
    }

    public static List<String> getBiome(){
        return Biome;
    }
    public static List<String> getTime(){
        return Time;
    }

    public static double getNone(){return none;}
    public static double getOne(){return one;}
    public static double getTwo(){return two;}

    public static Integer getRank1(){return rank1;}
    public static Integer getRank2(){return rank2;}
    public static Integer getRank3(){return rank3;}
    public static Integer getRank4(){return rank4;}
    public static Integer getRank5(){return rank5;}
    public static Integer getRank6(){return rank6;}
    public static Integer getRank7(){return rank7;}
    public static Integer getRank8(){return rank8;}
    public static Integer getRank9(){return rank9;}

//    public static int getTest(){return rank1;}


}
