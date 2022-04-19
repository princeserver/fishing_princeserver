package com.github.majisyou.fishing_plugin.commands;

import com.github.majisyou.fishing_plugin.Config.BiomeConfigManager;
import com.github.majisyou.fishing_plugin.Config.ConfigManager;
import com.github.majisyou.fishing_plugin.Config.CustomConfigSetting;
import com.github.majisyou.fishing_plugin.Config.FishermanConfigManager;
import com.github.majisyou.fishing_plugin.Fishing_plugin;
import com.github.majisyou.fishing_plugin.system.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.StructureType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fish;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class Cmd_test implements CommandExecutor {

    private static Fishing_plugin plugin = Fishing_plugin.getInstance();
    private static FileConfiguration config = new CustomConfigSetting(plugin,"fisher_man.yml").getConfig();

    public Cmd_test(Fishing_plugin plugin){plugin.getCommand("test").setExecutor(this);}

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        //ここも今はつかわない
        Player player = (Player) sender;
        try{
            Long time = plugin.getServer().getWorld(player.getWorld().getName()).getTime();
            if(time>0 && time<=6000) plugin.getLogger().info("morning");
            if(time>6000 && time<=12000) plugin.getLogger().info("day");
            if(time>12000 && time<=18000) plugin.getLogger().info("midnight");
            if(time>18000 && time<=24000) plugin.getLogger().info("night");
            plugin.getLogger().info(player.getName()+"の現在時刻は"+time);
            return true;
        }catch (Exception e){
            plugin.getLogger().info("getWorld.getTimeはNullの可能性があるらしい");
            plugin.getLogger().info(player.getName()+"がいるワールドは"+player.getWorld().getName()+"だよ");
            return true;
        }
    }
}
