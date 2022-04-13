package com.github.majisyou.fishing_plugin.commands;

import com.github.majisyou.fishing_plugin.Config.BiomeConfigManager;
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
        FileConfiguration Biomeconfig = FishSystem.Biomeyml(player);//playerがいるバイオームのコンフィグをロードする

        String rank = FishSystem.SelectRank();//ランクをランダムに選ぶ。偏りを設定して

        BiomeConfigManager.loadBiome(Biomeconfig,rank);//ランクごとにコンフィグをロードする

        List<String> fish;//返り値用の変数

        try {
            //フィッシュの作成！
            fish = Debug.Debug_Fish(Biomeconfig, rank,FishSystem.PlayerTime(player));
        } catch (Exception e) {
            plugin.getLogger().info("バイオームコンフィグの中のrank.id："+Biomeconfig.getString(rank+".id")+"の"+rank);
            plugin.getLogger().info(player.getWorld().getBiome(player.getLocation())+".ymlにアクセスできなかったよ");
            return true;
        }

        try {
            if (fish.get(0).equals("Catch!")) { //釣ることが成功できたら
                //fishをプレイヤーにdropする。
                player.getWorld().dropItem(player.getLocation(),Debug.MakeFish(fish,player.getName()));
                return true;
            }
            //fish.get(0)がescapeになったらイベントを無くす
            player.sendMessage("釣り糸が切れてしまった");
            if(fish.get(0).equals("setup_capture")) plugin.getLogger().info("fishリストに代入できていない。fish.ymlの中身を確認してみて:setup_captureだったよ！");
            if(fish.get(0).equals("which?")) plugin.getLogger().info("fishリストに代入できていない。fish.ymlの中身を確認してみて:whichだったよ！");
            plugin.getLogger().info(player.getWorld().getBiome(player.getLocation()) + ".ymlファイルの中に魚が設定されていないかもしれない");
        }catch (Exception e){
            plugin.getLogger().info(player.getWorld().getBiome(player.getLocation()) + ".ymlファイルの中に魚が設定されていないよ");
            player.sendMessage("この場所では何も釣れないようだ");
            //その結果をキャンセル
        }
//
//        Enchantment sell_enchant = Enchantment.getByName("DURABILITY");
//        plugin.getLogger().info(sell_enchant+"だよ");

        return true;
    }
}
