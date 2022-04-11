package com.github.majisyou.fishing_plugin.commands;

import com.github.majisyou.fishing_plugin.Config.BiomeConfigManager;
import com.github.majisyou.fishing_plugin.Fishing_plugin;
import com.github.majisyou.fishing_plugin.system.Debug;
import com.github.majisyou.fishing_plugin.system.FishSystem;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Cmd_Debug_fish_catch implements CommandExecutor {

    Fishing_plugin plugin = Fishing_plugin.getInstance();

    public Cmd_Debug_fish_catch(Fishing_plugin plugin){plugin.getCommand("debug_Fishing_fish").setExecutor(this);}


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;
        FileConfiguration Biomeconfig = FishSystem.Biomeyml(player);
        String rank = FishSystem.SelectRank();
        BiomeConfigManager.loadBiome(Biomeconfig,rank);
        List<String> fish;
        FishSystem.Size_calculate();
        try {
            fish = Debug.Debug_Fish(Biomeconfig, rank,FishSystem.PlayerTime(player));
        } catch (Exception e) {
            plugin.getLogger().info("バイオームコンフィグの中のrank.id："+Biomeconfig.getString("rank1.id"));
            plugin.getLogger().info("Biome.ymlにアクセスできなかったよ");
            return true;
        }
        return true;
    }
}
