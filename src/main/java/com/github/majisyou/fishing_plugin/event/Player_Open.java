package com.github.majisyou.fishing_plugin.event;

import com.github.majisyou.fishing_plugin.Fishing_plugin;
import com.github.majisyou.fishing_plugin.system.FishGui;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.Inventory;

public class Player_Open implements Listener {

    private static Fishing_plugin plugin = Fishing_plugin.getInstance();
    public Player_Open(Fishing_plugin plugin){ //mainメソッド内でイベントリスナを呼び出すためのコマンド
        plugin.getServer().getPluginManager().registerEvents(this,plugin);
    }

    @EventHandler
    public static void Player_Open_event(InventoryOpenEvent event){
        Player player = (Player) event.getPlayer();
        if(event.getInventory().getType().equals(InventoryType.MERCHANT)){
            if(event.getView().getTitle().equals(ChatColor.GREEN+"釣りマスター")){
                if(player.isSneaking()){
                    event.setCancelled(true);
                    FishGui.OpenInventory(player);
                }
            }
        }
   }
}
