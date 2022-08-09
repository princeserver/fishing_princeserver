package com.github.majisyou.fishing_plugin.system;

import com.github.majisyou.fishing_plugin.Fishing_plugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class FishGui {

    private static Fishing_plugin plugin = Fishing_plugin.getInstance();

    //Playerに売るようのguiを開かせるメソッド
    public static void OpenInventory(Player player){
        Inventory sell_fish =  Bukkit.createInventory(null, 36,"釣りマスター：\"売りたい魚を入れな\"");
        player.openInventory(sell_fish);
    }

    //インベントリ内の魚の値段を判定するメソッド
    public static Integer SellValue(Inventory inventory){
        int sum_value = 0;
        int stack;
        ItemStack[] items = inventory.getStorageContents();
        for (int i = 0; i < inventory.getSize(); i++){
            ItemStack itemStack = items[i];
            if(itemStack != null){
                if(itemStack.getType().equals(Material.TROPICAL_FISH)){
                    ItemMeta itemMeta = itemStack.getItemMeta();
                    NamespacedKey key = new NamespacedKey(plugin,"sellPrice");
                    PersistentDataContainer pdc = itemMeta.getPersistentDataContainer();
                    if(pdc.has(key)){
                        int sellPrice = 0;
                        try {
                            sellPrice = pdc.get(key, PersistentDataType.INTEGER);
                        }catch (Exception e){
                            plugin.getLogger().info("(FP)"+"error occurred"+e+":sellpriceのエラー");
                            sellPrice = 1;
                        }
                        stack = itemStack.getAmount();
                        if(sum_value + sellPrice * stack < 0){
                            break;
                        }
                        sum_value += sellPrice * stack;
                        inventory.setItem(i,new ItemStack(Material.AIR));
                    }
                }
                ItemMeta itemMeta = itemStack.getItemMeta();
                if(itemMeta instanceof BlockStateMeta blockStateMeta){
                    if(blockStateMeta.getBlockState() instanceof ShulkerBox){
                        ShulkerBox shulkerBox = (ShulkerBox) blockStateMeta.getBlockState();
                        Inventory shulkerInventory = shulkerBox.getInventory();
                        Integer sellprice = SellShulker(shulkerInventory,sum_value);
                        if(sum_value + sellprice > 4096000){
                            break;
                        }
                        sum_value += sellprice;
                        blockStateMeta.setBlockState(shulkerBox);
                        itemStack.setItemMeta(blockStateMeta);

                    }
                }
            }
        }
        return sum_value;
    }
//
//    public static Integer SellPrice(Inventory inventory){
//        int sum_value = 0;
//        int stack;
//        ItemStack[] items = inventory.getStorageContents();
//        for (int i = 0; i < inventory.getSize(); i++){
//            ItemStack itemStack = items[i];
//            if(itemStack.getType().equals(Material.TROPICAL_FISH)){
//                ItemMeta itemMeta = itemStack.getItemMeta();
//                NamespacedKey key = new NamespacedKey(plugin,"sellPrice");
//                PersistentDataContainer pdc = itemMeta.getPersistentDataContainer();
//                if(pdc.has(key)){
//                    int sellPrice = 0;
//                    try {
//                        sellPrice = pdc.get(key, PersistentDataType.INTEGER);
//                    }catch (Exception e){
//                        plugin.getLogger().info("(FP)"+"error occurred"+e+":sellpriceのエラー");
//                        sellPrice = 1;
//                    }
//                    stack = itemStack.getAmount();
//                    sum_value += sellPrice * stack;
//                }
//            }
//
//        }
//        return sum_value;
//    }

    public static Integer SellShulker(Inventory inventory,Integer sum_value){
        int stack;
        ItemStack[] items = inventory.getStorageContents();
        for (int i = 0; i < inventory.getSize(); i++){
            ItemStack itemStack = items[i];
            if(itemStack != null) {
                if (itemStack.getType().equals(Material.TROPICAL_FISH)) {
                    ItemMeta itemMeta = itemStack.getItemMeta();
                    NamespacedKey key = new NamespacedKey(plugin, "sellPrice");
                    PersistentDataContainer pdc = itemMeta.getPersistentDataContainer();
                    if (pdc.has(key)) {
                        int sellPrice = 0;
                        try {
                            sellPrice = pdc.get(key, PersistentDataType.INTEGER);
                        } catch (Exception e) {
                            plugin.getLogger().info("(FP)" + "error occurred" + e + ":sellpriceのエラー");
                            sellPrice = 1;
                        }
                        stack = itemStack.getAmount();
                        if(sum_value + sellPrice * stack > 4096000){
                            break;
                        }
                        sum_value += sellPrice * stack;
                        inventory.setItem(i, new ItemStack(Material.AIR));
                    }
                }
            }
        }
        return sum_value;
    }

    //インベントリに入らないアイテムをその場にドロップするメソッド
    //ただOwnerを設定できていないから、うーんという気分。
    //時間でOwnerが外れるっていうメソッドは作れなかった
    public static void dropItem(Inventory inventory,Player player){
        ItemStack[] items = inventory.getContents();
        Inventory playerInventory = player.getInventory();
        for (ItemStack item : items){
            if(item != null){
                if(playerInventory.firstEmpty() != -1){
                    playerInventory.addItem(item);
                }else {
                    player.getWorld().dropItem(player.getLocation(),item);
                }
            }
        }
    }

    //得られたお金をどうエメラルドに直すかというメソッド
    public static List<Integer> cal_emerald(Integer Value){
        List<Integer> cal_emerald = new ArrayList<>();
        int sellValue = Value;
        int Liquid_emerald = 0;
        int emeraldBlock = 0;

        if(sellValue > 4096){
            while (sellValue > 4096){
                Liquid_emerald += 1;
                sellValue -= 4096;
            }
        }

        if(sellValue > 64){
            while (sellValue > 64){
                emeraldBlock += 1;
                sellValue -= 64;
            }
        }

        cal_emerald.add(Liquid_emerald);
        cal_emerald.add(emeraldBlock);
        cal_emerald.add(sellValue);

        return cal_emerald;
    }

    public static void AddItemPlayerInventory(ItemStack itemStack,Player player){
        Inventory playerInventory = player.getInventory();
        if(playerInventory.firstEmpty() != -1){
            playerInventory.addItem(itemStack);
        }else {
            player.getWorld().dropItem(player.getLocation(),itemStack);
        }
    }

}
