package com.github.majisyou.fishing_plugin.commands;

import com.github.majisyou.fishing_plugin.Config.BiomeConfigManager;
import com.github.majisyou.fishing_plugin.Config.ConfigManager;
import com.github.majisyou.fishing_plugin.Config.CustomConfigSetting;
import com.github.majisyou.fishing_plugin.Config.FishConfigManager;
import com.github.majisyou.fishing_plugin.Fishing_plugin;
import com.github.majisyou.fishing_plugin.system.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Fish;
import org.jetbrains.annotations.NotNull;

public class Cmd_reloadconfig implements CommandExecutor {
    //このコマンド未だに起動せず

    Fishing_plugin plugin = Fishing_plugin.getInstance();

    public Cmd_reloadconfig(Fishing_plugin plugin){plugin.getCommand("fishing_config_reload").setExecutor(this);}

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        //reloadできてないっぽい？
        new CustomConfigSetting(plugin).reloadConfig();
        plugin.getLogger().info("config.ymlを再読み込み");
        new CustomConfigSetting(plugin,"fish.yml").reloadConfig();
        plugin.getLogger().info("fish.ymlを再読み込み");
        FishSystem.load_config();
        plugin.getLogger().info("バイオーム.ymlを再読み込み");

        ConfigManager.loadConfig();

        plugin.getLogger().info(ConfigManager.getServername()+"変更の確認");
        plugin.getLogger().info(FishConfigManager.getConfirm_change()+"変更の確認");

        return true;
    }
}
