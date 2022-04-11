package com.github.majisyou.fishing_plugin.event;

import com.github.majisyou.fishing_plugin.Fishing_plugin;
import com.github.majisyou.fishing_plugin.system.Debug;
import com.github.majisyou.fishing_plugin.system.FishGui;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fish;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Player_click implements Listener {
    //このイベント使わない！！ww

    private static Fishing_plugin plugin = Fishing_plugin.getInstance();
    public Player_click(Fishing_plugin plugin){ //mainメソッド内でイベントリスナを呼び出すためのコマンド
        plugin.getServer().getPluginManager().registerEvents(this,plugin);
    }

    @EventHandler
    public static void Player_RightClick_event(PlayerInteractEntityEvent event){
//        Player player = event.getPlayer();
//        Entity entity = event.getRightClicked();
//
////        if(player.isSneaking()){
////            if (entity instanceof Villager){
////                Villager villager = (Villager) entity;
//                if (villager.getCustomName().equals("釣りマスター"))
//                    event.setCancelled(true);
//
////                    FishGui.OpenInventory(player);
//            }
//        }
        //このクリックイベント要らない！w
//        Player player = (Player) event.getWhoClicked();
//        Inventory inventory = event.getClickedInventory();
//        ItemStack currentItem = event.getCurrentItem();
//        String name = event.getView().getTitle();
//        List<ItemStack> dropItems = new ArrayList<>();
//
//
//        if(name.equals("釣りスト")){
//            if(event.getCurrentItem()==null)
//                return;
//            if(event.getCurrentItem().getType().equals(Material.WHITE_STAINED_GLASS_PANE))
//                event.setCancelled(true);
//
//        }
    }
}
