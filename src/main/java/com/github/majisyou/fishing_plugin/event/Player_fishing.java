package com.github.majisyou.fishing_plugin.event;

import com.github.majisyou.fishing_plugin.Config.BiomeConfigManager;
import com.github.majisyou.fishing_plugin.Fishing_plugin;
import com.github.majisyou.fishing_plugin.system.FishSystem;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Player_fishing implements Listener {

    Fishing_plugin plugin = Fishing_plugin.getInstance();

    //Fishing_pluginでイベントを呼び出すときのメソッド
    public Player_fishing(Fishing_plugin plugin){ //mainメソッド内でイベントリスナを呼び出すためのコマンド
        plugin.getServer().getPluginManager().registerEvents(this,plugin);
    }

    @EventHandler
    public void Fishing_main (PlayerFishEvent event){
        if (event.getCaught()!= null){//釣ることができた場合
            Player player = event.getPlayer();
            if(!(event.getHook().getHookedEntity()==null)){
                return;
            }

            Item item_entity = (Item) event.getCaught();
            ItemStack item = item_entity.getItemStack();

            if(item.getType().isBlock()){
                return;
            }

            if(!(FishSystem.fishing_rod(player))) return; //右手か左手にカスタムモデルデータがある釣り竿を持っていた場合にtrueを返すmethodを使ってる。例外は少しあるけど

            if(FishSystem.fishing_rod(player)){

                FileConfiguration Biomeconfig = FishSystem.Biomeyml(player);//playerがいるバイオームのコンフィグをロードする

                String rank = FishSystem.SelectRank();//ランクをランダムに選ぶ。偏りを設定して

                BiomeConfigManager.loadBiome(Biomeconfig,rank);//ランクごとにコンフィグをロードする

                List<String> fish;//返り値用の変数
                try {
                    //フィッシュの作成！
                    fish = FishSystem.Fish(Biomeconfig, rank,FishSystem.PlayerTime(player));
                } catch (Exception e) {
                    plugin.getLogger().info("バイオームコンフィグの中のrank.id："+Biomeconfig.getString(rank+".id")+"の"+rank);
                    plugin.getLogger().info(player.getWorld().getBiome(player.getLocation())+".ymlにアクセスできなかったよ");
                    return;
                }

                try {
                    if (fish.get(0).equals("Catch!")) { //釣ることが成功できたら
                        //fishをプレイヤーにdropする。
                        ItemStack FishItem = FishSystem.MakeFish(fish,player); //ItemStackを作成する
                        player.getWorld().dropItem(player.getLocation(),FishItem);
                        event.getCaught().remove();
                        return;
                    }
                    //fish.get(0)がescapeになったらイベントを無くす
                    event.getCaught().remove();
                    player.sendMessage("釣り糸が切れてしまった");
                    if(fish.get(0).equals("setup_capture")) plugin.getLogger().info("fishリストに代入できていない。fish.ymlの中身を確認してみて:setup_captureだったよ！");
                    if(fish.get(0).equals("which?")) plugin.getLogger().info("fishリストに代入できていない。fish.ymlの中身を確認してみて:whichだったよ！");
                    if(!(fish.get(0).equals("escape"))) plugin.getLogger().info(player.getWorld().getBiome(player.getLocation())+".ymlを確認してほしい");
                }catch (Exception e){
                    plugin.getLogger().info(player.getWorld().getBiome(player.getLocation()) + ".ymlファイルの中に魚が設定されていないよ");
                    player.sendMessage("この場所では何も釣れないようだ");
                    event.getCaught().remove();
                    //その結果をキャンセル
                }
            }
        }
    }
}
