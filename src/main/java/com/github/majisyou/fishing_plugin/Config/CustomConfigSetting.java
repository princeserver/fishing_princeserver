package com.github.majisyou.fishing_plugin.Config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;

public class CustomConfigSetting {
    //Bukkitの中のconfig.ymlを操作する手順を模倣している
    //customコンフィグが設定されている。

    private FileConfiguration config = null;
    private final File configFile;
    private final String file;
    private final Plugin plugin;

    public CustomConfigSetting(Plugin plugin){
        //file名が指定されていないときにconfig.ymlを使用するソース
        this(plugin,"config.yml");
    }

    public CustomConfigSetting(Plugin plugin, String fileName){
        //file名のconfigを読み込むソース
        this.plugin = plugin;
        this.file = fileName;
        configFile = new File(plugin.getDataFolder(),file);
    }

    public void saveDefaultConfig(){
        if(!configFile.exists()){
            //コンフィグファイルが存在するならsaveResourceでファイルを保存
            plugin.saveResource(file,false);
            plugin.getLogger().info(file+"がなかったから"+file+"を作成したよ");
        }
    }

    public void createConfig() throws Exception {
        if(!configFile.exists()){
            //Files.createFileには絶対的なPathが必要ということでconfigFileの絶対Pathの作成
            Files.createFile(Path.of(configFile.getAbsolutePath()));
            plugin.getLogger().info("(FP)"+file+"がなかったから"+file+"を作成したよ");
        }
    }

    public void reloadConfig(){
        config = YamlConfiguration.loadConfiguration(configFile);
        final InputStream defConfigStream = plugin.getResource(file);
        if(defConfigStream == null){
            return;
        }
        config.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream, StandardCharsets.UTF_8)));
    }

    public FileConfiguration getConfig(){
        if(config == null){
            reloadConfig();
        }
        return config;
    }

    public void saveConfig(){
        if(config == null){
            return;
        }
        try {
            getConfig().save(configFile);
        } catch(IOException ex){
            plugin.getLogger().log(Level.SEVERE,"(FP)"+"Could not save config to" + configFile, ex);
        }
    }

}
