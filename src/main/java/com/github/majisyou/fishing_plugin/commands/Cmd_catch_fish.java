package com.github.majisyou.fishing_plugin.commands;

import com.github.majisyou.fishing_plugin.Config.FishConfigManager;
import com.github.majisyou.fishing_plugin.Fishing_plugin;
import com.github.majisyou.fishing_plugin.system.FishSystem;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Cmd_catch_fish implements CommandExecutor {

    Fishing_plugin plugin = Fishing_plugin.getInstance();
    public Cmd_catch_fish(Fishing_plugin plugin){plugin.getCommand("make_fish").setExecutor(this);}

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player){//釣ることができた場合
            Integer rank = 0;
            Integer id = 0;
            if(args.length==0){
                rank = FishSystem.SelectRank();
                id = FishSystem.getFishId(player,rank);
            }
            if(args.length == 1){
                if(args[0].matches("-?\\d+")){
                    id = Integer.parseInt(args[0]);
                    FishConfigManager.LoadFishConfig(id);
                    rank = FishConfigManager.getRank();
                }else {
                    player.sendMessage("<引数>は数字にしてください");
                    return true;
                }
            }
            ItemStack fish = FishSystem.MakeFish(id,player.getName(),rank);
            player.getWorld().dropItem(player.getLocation(),fish);
        }
        return true;
    }
}
