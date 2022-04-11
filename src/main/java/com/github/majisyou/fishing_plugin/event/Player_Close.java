package com.github.majisyou.fishing_plugin.event;

import com.github.majisyou.fishing_plugin.Fishing_plugin;
import com.github.majisyou.fishing_plugin.system.FishGui;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.net.http.WebSocket;
import java.util.List;

public class Player_Close implements Listener {

    private static Fishing_plugin plugin = Fishing_plugin.getInstance();
    public Player_Close(Fishing_plugin plugin){ //mainメソッド内でイベントリスナを呼び出すためのコマンド
        plugin.getServer().getPluginManager().registerEvents(this,plugin);
    }


    @EventHandler

    public static void invenory_close(InventoryCloseEvent event){
        String  name = event.getView().getTitle();
        Player player = (Player) event.getPlayer();
        //addItemとかいうコマンドが会ったんだが
        //使える？

        if(name.equals("釣りマスター")){
            Inventory inventory = event.getInventory();
            Integer sellvalue = FishGui.SellValue(inventory);
            List<Integer> emerald_value = FishGui.cal_emerald(sellvalue);
            List<ItemStack> drop_item = FishGui.dropItem(inventory);

            Inventory playerInventory = player.getInventory();
            ItemStack[] item = playerInventory.getStorageContents();

            ItemStack emerald = new ItemStack(Material.EMERALD,emerald_value.get(2));
            ItemStack emeraldBlock = new ItemStack(Material.EMERALD_BLOCK,emerald_value.get(1));
            ItemStack Liquid_emerald = new ItemStack(Material.REPEATING_COMMAND_BLOCK,emerald_value.get(0));
            ItemMeta Liquid_emerald_Meta = Liquid_emerald.getItemMeta();
            Liquid_emerald_Meta.setCustomModelData(1);

            int drop_counter = 0;

            for(int i=0; i<item.length; i++){
                if (item[i]==null){
                    if(!(emerald_value.get(0)==0)){
                        player.getInventory().setItem(i,Liquid_emerald);
                        emerald_value.set(0,0);
                    }
                    else if(!(emerald_value.get(1)==0)){
                        player.getInventory().setItem(i,emeraldBlock);
                        emerald_value.set(1,0);
                    }
                    else if(!(emerald_value.get(2)==0)){
                        player.getInventory().setItem(i,emerald);
                        emerald_value.set(2,0);
                    }else{
                        if(drop_counter < drop_item.size()){
                            player.getInventory().setItem(i,drop_item.get(drop_counter));
                            drop_counter += 1;
                        }else {
                            break;
                        }
                    }
                }
            }
            for (;drop_counter<drop_item.size(); drop_counter++){
                player.getWorld().dropItem(player.getLocation(),drop_item.get(drop_counter));
            }
            if(!(sellvalue==0)){
                player.sendMessage(sellvalue+"E手に入ったよ");
            }else {
                player.sendMessage("お魚を入れてね");
            }

        }
    }
}
