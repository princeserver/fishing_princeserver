package com.github.majisyou.fishing_plugin.commands;

import com.github.majisyou.fishing_plugin.Fishing_plugin;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class Cmd_fishing_rod implements CommandExecutor {

    Fishing_plugin plugin = Fishing_plugin.getInstance();

    public Cmd_fishing_rod(Fishing_plugin plugin){plugin.getCommand("fishing_rod").setExecutor(this);}

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player player)){
            plugin.getLogger().info("このコマンドはコンソールから打てないよ");
            return true;
        }
        if(player.getInventory().getItemInMainHand().getType().equals(Material.FISHING_ROD)){
            ItemStack RightHandItem =  player.getInventory().getItemInMainHand().clone();
            ItemMeta RightHandItemMeta = RightHandItem.getItemMeta();
            RightHandItemMeta.setCustomModelData(1);
            RightHandItem.setItemMeta(RightHandItemMeta);
            if(RightHandItem.getType().equals(Material.FISHING_ROD)){
                player.getInventory().setItem(player.getInventory().getHeldItemSlot(),RightHandItem);
                return true;
            }
            return true;
        }else {
            player.sendMessage("メインハンドは釣り竿を持ってください");
        }
        return false;
    }
}
