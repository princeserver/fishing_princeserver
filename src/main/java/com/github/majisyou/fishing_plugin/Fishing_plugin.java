package com.github.majisyou.fishing_plugin;

import com.github.majisyou.fishing_plugin.Config.ConfigManager;
import com.github.majisyou.fishing_plugin.Config.CustomConfigSetting;
import com.github.majisyou.fishing_plugin.commands.*;
import com.github.majisyou.fishing_plugin.event.Player_Close;
import com.github.majisyou.fishing_plugin.event.Player_Open;
import com.github.majisyou.fishing_plugin.event.Player_click;
import com.github.majisyou.fishing_plugin.event.Player_fishing;
import com.github.majisyou.fishing_plugin.system.FishSystem;
import org.bukkit.plugin.java.JavaPlugin;

public final class Fishing_plugin extends JavaPlugin {

    //instanceの作成
    private static Fishing_plugin instance;
    public Fishing_plugin(){instance = this;}
    public static Fishing_plugin getInstance(){
        return instance;
    }
    //----------------------

//    private static ConfigManager config;
//    private static ConfigManager fishing_config;
//    private static ConfigManager fishing_rod_config;

    @Override
    public void onEnable() {
        // Plugin startup logic

        //Event処理
        new Player_fishing(this);
        new Player_Close(this);
        new Player_Open(this);


        //command処理
        new Cmd_test(this);
        new Cmd_reloadconfig(this);
        new Cmd_Debug_fish_catch(this);
        new Cmd_fishing_rod(this);
        new Cmd_fishshop(this);
        new Cmd_summon_villager(this);


        //configの設定
        new CustomConfigSetting(this).saveDefaultConfig();
        ConfigManager.loadConfig();
        new CustomConfigSetting(this,"Biome_template.yml").saveDefaultConfig();
        new CustomConfigSetting(this,"fish.yml").saveDefaultConfig();
        new CustomConfigSetting(this,"fisher_man.yml").saveDefaultConfig();
        try {FishSystem.setup_custom_config(this);
            FishSystem.load_config();
        } catch (Exception e) {getLogger().info("biomeコンフィグを読み込んでないかも");}


        getLogger().info("Fishing_pluginだよ");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

        getLogger().info("Fishing_pluginよBYEBYE");
    }

}
