package com.github.majisyou.fishing_plugin.event;

import com.github.majisyou.fishing_plugin.Config.BiomeConfigManager;
import com.github.majisyou.fishing_plugin.Config.FishConfigManager;
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
            if(!(event.getHook().getHookedEntity()==null)){
                return;
            }
            Player player = event.getPlayer();
            if(FishSystem.fishing_rod(player)){
                Integer rank = FishSystem.SelectRank();
                Integer fishId = FishSystem.getFishId(player,rank);
                ItemStack fish = FishSystem.MakeFish(fishId,player.getName(),rank);
                player.getWorld().dropItem(player.getLocation(),fish);
                if(FishConfigManager.getGetExp()!=0){
                    event.setExpToDrop(FishConfigManager.getGetExp());
                }
                event.getCaught().remove();
            }
        }
    }
}
