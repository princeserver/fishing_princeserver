package com.github.majisyou.fishing_plugin.commands;

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
//
//        Enchantment sell_enchant = Enchantment.getByName("DURABILITY");
//        plugin.getLogger().info(sell_enchant+"だよ");

        return true;
    }
}
