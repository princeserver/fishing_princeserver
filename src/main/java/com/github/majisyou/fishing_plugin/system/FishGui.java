package com.github.majisyou.fishing_plugin.system;

import com.github.majisyou.fishing_plugin.Config.FishConfigManager;
import com.github.majisyou.fishing_plugin.Fishing_plugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Fish;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FishGui {

    private static Fishing_plugin plugin = Fishing_plugin.getInstance();

    //Playerに売るようのguiを開かせるメソッド
    public static void OpenInventory(Player player){
        Inventory sell_fish =  Bukkit.createInventory(null, 36,"釣りマスター");
        player.openInventory(sell_fish);
    }

    //インベントリ内の魚の値段を判定するメソッド
    public static Integer SellValue(Inventory inventory){
        int inventory_size = 36;
        int sum_value = 0;
        int stack = 1;
        String id = "No.0";
        ItemStack[] item = inventory.getStorageContents();
        if(inventory.getSize()==inventory_size){
            for (ItemStack itemStack : item) {
                if (!(itemStack == null)) {
                    if (itemStack.getType().equals(Material.TROPICAL_FISH)) {
                        if (itemStack.getItemMeta().hasCustomModelData()) {
                            id = itemStack.getLore().get(0).substring(5);
                            try {
                                FishConfigManager.LoadFishConfig(Integer.parseInt(id));
                                stack = itemStack.getAmount();
                                sum_value += Integer.parseInt(FishConfigManager.getSell_price()) * stack;
                            } catch (Exception e) {
                                plugin.getLogger().info("お魚のid間違えてない？" + id);
                            }
                        }
                    }
                }
            }
            return sum_value;
        }
        return sum_value;
    }

    //インベントリに入らないアイテムをその場にドロップするメソッド
    //ただOwnerを設定できていないから、うーんという気分。
    //時間でOwnerが外れるっていうメソッドは作れなかった
    public static List<ItemStack> dropItem(Inventory inventory){
        int inventory_size = 36;
        int stack = 1;

        String id = "No.0";
        ItemStack[] item = inventory.getStorageContents();
        List<ItemStack> drops = new ArrayList<>();

        if(inventory.getSize()==inventory_size){
            for (ItemStack itemStack : item) {
                if (!(itemStack == null)) {
                    if (!(itemStack.getType().equals(Material.TROPICAL_FISH))) {
                        drops.add(itemStack);
                    } else if (!(itemStack.getItemMeta().hasCustomModelData())) {
                        drops.add(itemStack);
                    }
                }
            }
         }
        return drops;
    }

    //使ってない
    public static List<ItemStack> dropAll(Inventory inventory){
        int inventory_size = 36;
        String id = "No.0";
        ItemStack[] item = inventory.getStorageContents();
        List<ItemStack> drops = new ArrayList<>();
        if(inventory.getSize()==inventory_size){
            for (ItemStack itemStack : item) {
                if (!(itemStack == null)) {
                    drops.add(itemStack);
                }
            }
        }
        return drops;
    }

    //得られたお金をどうエメラルドに直すかというメソッド
    public static List<Integer> cal_emerald(Integer sell_value){
        List<Integer> cal_emerald = new ArrayList<>();

        Integer Liquid_emerald = 0;
        Integer emeraldBlock = 0;
        Integer emerald = 0;

        if(sell_value>4096)
            for (int i = sell_value; i >= 4096;){
                Liquid_emerald += 1;
                i -= 4096;
                sell_value = i;
            }


        if(sell_value>64)
            for (int i = sell_value; i >= 64;){
                emeraldBlock += 1;
                i -= 64;
                sell_value = i;
            }

        emerald = sell_value;

        cal_emerald.add(Liquid_emerald);
        cal_emerald.add(emeraldBlock);
        cal_emerald.add(emerald);

        return cal_emerald;
    }

}
