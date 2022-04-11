package com.github.majisyou.fishing_plugin.commands;

import com.github.majisyou.fishing_plugin.Fishing_plugin;
import com.github.majisyou.fishing_plugin.system.FishGui;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

public class Cmd_fishshop implements CommandExecutor {
    //このコマンドどうしようかなという気分
    //正直、今は要らないコマンドだよ

    Fishing_plugin plugin = Fishing_plugin.getInstance();

    public Cmd_fishshop(Fishing_plugin plugin){plugin.getCommand("fish_shop").setExecutor(this);}


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player)) {
            plugin.getLogger().info("コンソールからは打てません");
            return true;
        }
        Player player = (Player) sender;
        FishGui.OpenInventory(player);

        return true;
    }
}
