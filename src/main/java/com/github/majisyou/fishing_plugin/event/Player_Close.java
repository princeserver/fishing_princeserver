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
    //インベントリを閉じたときに計算される

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

        if(name.equals("釣りマスター：\"売りたい魚を入れな\"")){
            Inventory inventory = event.getInventory(); //クローズしたインベントリをゲット
            Integer sellvalue = FishGui.SellValue(inventory); //売れたお金の計算
            FishGui.dropItem(inventory,(Player) event.getPlayer());
            List<Integer> emerald_value = FishGui.cal_emerald(sellvalue); //リキッドエメラルドとエメラルドブロックとエメラルドの数をリスト上に設定
            Integer liquidEmerald = emerald_value.get(0);
            ItemStack Liquid_emerald = new ItemStack(Material.REPEATING_COMMAND_BLOCK,64);
            ItemMeta Liquid_emerald_Meta = Liquid_emerald.getItemMeta(); //リキッドエメラルド用のアイテムメタを取得する！
            Liquid_emerald_Meta.setCustomModelData(1000); //リッキドエメラルドのカスタムモデルデータの設定ここを弄る必要あり！！
            Liquid_emerald.setItemMeta(Liquid_emerald_Meta);

            while (liquidEmerald > 64){
                //リッキドエメラルドの設定
                FishGui.AddItemPlayerInventory(Liquid_emerald,(Player) event.getPlayer());
                liquidEmerald -= 64;
            }

            Liquid_emerald.setAmount(liquidEmerald);
            ItemStack emerald = new ItemStack(Material.EMERALD,emerald_value.get(2)); //エメラルドの数の設定
            ItemStack emeraldBlock = new ItemStack(Material.EMERALD_BLOCK,emerald_value.get(1)); //エメラルドブロックの設定

            FishGui.AddItemPlayerInventory(Liquid_emerald,(Player) event.getPlayer());
            FishGui.AddItemPlayerInventory(emeraldBlock,(Player) event.getPlayer());
            FishGui.AddItemPlayerInventory(emerald,(Player) event.getPlayer());

            if(!(sellvalue==0)){
                player.sendMessage(sellvalue+"E手に入ったよ");
            }else {
                player.sendMessage("お魚を入れてね");
            }
        }
    }
}
