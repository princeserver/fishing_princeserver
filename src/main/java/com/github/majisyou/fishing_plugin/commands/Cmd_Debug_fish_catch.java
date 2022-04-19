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
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Cmd_Debug_fish_catch implements CommandExecutor {

    Fishing_plugin plugin = Fishing_plugin.getInstance();

    public Cmd_Debug_fish_catch(Fishing_plugin plugin){plugin.getCommand("debug_Fishing_fish").setExecutor(this);}


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        FileConfiguration Biomeconfig = Debug.Debug_Biomeyml("BEACH");
        String rank = FishSystem.SelectRank();
        BiomeConfigManager.loadBiome(Biomeconfig,rank);
        List<String> fish;
        FishSystem.Size_calculate();
        Player player = (Player) sender;

        try {
            fish = Debug.Fish(player);
        } catch (Exception e) {
            plugin.getLogger().info("rank.id："+Biomeconfig.getString("rank1.id"));
            plugin.getLogger().info("Biome.ymlにアクセスできなかったよ");
            return true;
        }
        try {
            if (fish.get(0).equals("Catch!")) { //釣ることが成功できたら
                //fishをプレイヤーにdropする。
                plugin.getLogger().info(fish.get(0)+"で成功");
                ItemStack FishItem = Debug.MakeFish(fish,player);
                plugin.getLogger().info(fish+"だよ");
                player.getWorld().dropItem(player.getLocation(),FishItem);
                return true;
            }

            //fish.get(0)がescapeになったらイベントを無くす
            if(fish.get(0).equals("setup_capture")) plugin.getLogger().info("fishリストに代入できていない。fish.ymlの中身を確認してみて:setup_captureだったよ！");
            if(fish.get(0).equals("which?")) plugin.getLogger().info("fishリストに代入できていない。fish.ymlの中身を確認してみて:whichだったよ！");

        }catch (Exception e){
            //その結果をキャンセル
            plugin.getLogger().info("null!!");
        }



        return true;
    }
}
