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

    public static void OpenInventory(Player player){
        Inventory sell_fish =  Bukkit.createInventory(null, 36,"釣りマスター");
        player.openInventory(sell_fish);
    }

    public static Integer SellValue(Inventory inventory){
        int inventory_size = 36;
        int sum_value = 0;
        int stack = 1;
        String id = "No.0";
        ItemStack[] item = inventory.getStorageContents();
        if(inventory.getSize()==inventory_size){
            for (ItemStack itemStack : item) {
                if (!(itemStack == null)) {
                    if (itemStack.getType().equals(Material.COD)) {
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

    public static List<ItemStack> dropItem(Inventory inventory){
        int inventory_size = 36;
        int stack = 1;

        String id = "No.0";
        ItemStack[] item = inventory.getStorageContents();
        List<ItemStack> drops = new ArrayList<>();

        if(inventory.getSize()==inventory_size){
            for (ItemStack itemStack : item) {
                if (!(itemStack == null)) {
                    if (!(itemStack.getType().equals(Material.COD))) {
                        drops.add(itemStack);
                    } else if (!(itemStack.getItemMeta().hasCustomModelData())) {
                        drops.add(itemStack);
                    }
                }
            }
         }
        return drops;
    }

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

//    public static List<Integer> display_digits(Integer sell_price){
//        List<Integer> digit = new ArrayList<>();
//        Integer cal_digit = sell_price;
//
//        if(cal_digit>100000000){
//            return digit;
//        }
//
//        digit.add(cal_digit/10000000);
//        cal_digit = cal_digit%10000000;
//        digit.add(cal_digit/1000000);
//        cal_digit = cal_digit%1000000;
//        digit.add(cal_digit/100000);
//        cal_digit = cal_digit%100000;
//        digit.add(cal_digit/10000);
//        cal_digit = cal_digit%10000;
//        digit.add(cal_digit/1000);
//        cal_digit = cal_digit%1000;
//        digit.add(cal_digit/100);
//        cal_digit = cal_digit%100;
//        digit.add(cal_digit/10);
//        cal_digit = cal_digit%10;
//        digit.add(cal_digit);
//
//        return digit;
//    }

//    public static Inventory display_inventory(Inventory inventory,Integer sell_price){
//        if(!(inventory==null)){
//            if(inventory.getSize()==36){
//                ItemStack[] items = inventory.getContents();
//                List<Integer> display_emerald = FishGui.display_digits(sell_price);
//
//                if(display_emerald.size()==8){
//                    inventory.setItem(46,FishGui.Number_plate(display_emerald.get(0)));
//                    inventory.setItem(47,FishGui.Number_plate(display_emerald.get(1)));
//                    inventory.setItem(48,FishGui.Number_plate(display_emerald.get(2)));
//                    inventory.setItem(49,FishGui.Number_plate(display_emerald.get(3)));
//                    inventory.setItem(50,FishGui.Number_plate(display_emerald.get(4)));
//                    inventory.setItem(51,FishGui.Number_plate(display_emerald.get(5)));
//                    inventory.setItem(52,FishGui.Number_plate(display_emerald.get(6)));
//                    inventory.setItem(53,FishGui.Number_plate(display_emerald.get(7)));
//                }
//            }else {
//                plugin.getLogger().info("インベントリの大きさが合っていないよ");
//            }
//            return inventory;
//        }
//        plugin.getLogger().info("インベントリがnullですね");
//        return null;
//    }

//    public static ItemStack Number_plate(Integer integer){
//        ItemStack number_plate = new ItemStack(Material.WHITE_STAINED_GLASS_PANE,1);
//        ItemMeta number_meta = number_plate.getItemMeta();
//
//        if(integer==0)
//            number_meta.setCustomModelData(9999);
//        if (integer==1)
//            number_meta.setCustomModelData(9999);
//        if (integer==2)
//            number_meta.setCustomModelData(9999);
//        if (integer==3)
//            number_meta.setCustomModelData(9999);
//        if (integer==4)
//            number_meta.setCustomModelData(9999);
//        if (integer==5)
//            number_meta.setCustomModelData(9999);
//        if (integer==6)
//            number_meta.setCustomModelData(9999);
//        if (integer==7)
//            number_meta.setCustomModelData(9999);
//        if (integer==8)
//            number_meta.setCustomModelData(9999);
//        if (integer==9)
//            number_meta.setCustomModelData(9999);
//
//        number_plate.setItemMeta(number_meta);
//
//        return number_plate;
//    }

}


////拡張forループになる前のやつ
// for(int i=0; i<item.length; i++){
//        if(item[i].getType().equals(Material.COD)){
//        if (item[i].getItemMeta().hasCustomModelData()){
//        id = item[i].getLore().get(0).substring(3);
//        try {
//        if(!(id=="0")){
//        FishConfigManager.LoadFishConfig(Integer.parseInt(id));
//        sum_value += Integer.parseInt(FishConfigManager.getSell_price());
//        }
//        }catch (Exception e){
//        plugin.getLogger().info(id);
//        }
//        }
//        }
//        }
