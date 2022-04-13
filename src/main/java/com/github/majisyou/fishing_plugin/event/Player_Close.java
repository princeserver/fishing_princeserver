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

        if(name.equals("釣りマスター")){
            Inventory inventory = event.getInventory(); //クローズしたインベントリをゲット
            Integer sellvalue = FishGui.SellValue(inventory); //売れたお金の計算
            List<Integer> emerald_value = FishGui.cal_emerald(sellvalue); //リキッドエメラルドとエメラルドブロックとエメラルドの数をリスト上に設定
            List<ItemStack> drop_item = FishGui.dropItem(inventory); //魚以外のアイテムがインベントリ内に合ったら、返すやつ

            Inventory playerInventory = player.getInventory(); //魚以外のアイテムを返す用のインベントリを取得
            ItemStack[] item = playerInventory.getStorageContents(); //ItemStackのリストを作成、中身がnullだと変換する

            ItemStack emerald = new ItemStack(Material.EMERALD,emerald_value.get(2)); //エメラルドの数の設定
            ItemStack emeraldBlock = new ItemStack(Material.EMERALD_BLOCK,emerald_value.get(1)); //エメラルドブロックの設定
            ItemStack Liquid_emerald = new ItemStack(Material.REPEATING_COMMAND_BLOCK,emerald_value.get(0)); //リッキドエメラルドの設定
            ItemMeta Liquid_emerald_Meta = Liquid_emerald.getItemMeta(); //リキッドエメラルド用のアイテムメタを取得する！
            Liquid_emerald_Meta.setCustomModelData(1000); //リッキドエメラルドのカスタムモデルデータの設定ここを弄る必要あり！！

            int drop_counter = 0;

            for(int i=0; i<item.length; i++){
                if (item[i]==null){
                    if(!(emerald_value.get(0)==0)){
                        player.getInventory().setItem(i,Liquid_emerald);
                        emerald_value.set(0,0);
                        //エメラルドをインベントリに入れる
                    }
                    else if(!(emerald_value.get(1)==0)){
                        player.getInventory().setItem(i,emeraldBlock);
                        emerald_value.set(1,0);
                        //エメラルドブロックをインベントリに入れる
                    }
                    else if(!(emerald_value.get(2)==0)){
                        player.getInventory().setItem(i,emerald);
                        emerald_value.set(2,0);
                        //リキッドエメラルドをインベントリに入れる
                    }else{
                        if(drop_counter < drop_item.size()){
                            player.getInventory().setItem(i,drop_item.get(drop_counter));
                            drop_counter += 1;
                            //ドロップアイテムを空いてるインベントリに入れる
                        }else {
                            break;
                        }
                    }
                }
            }
            for (;drop_counter<drop_item.size(); drop_counter++){
                //空いてるインベントリが無かったらアイテムがドロップする
                //ここさ、エメラルド関係はドロップしないからさ、売った後にインベントリを満杯にするとエメラルドが
                //消えるっていうバグというか仕様になっている。変更はできると思う
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
