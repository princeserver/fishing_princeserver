package com.github.majisyou.fishing_plugin.system;

import com.github.majisyou.fishing_plugin.Config.CustomConfigSetting;
import com.github.majisyou.fishing_plugin.Config.FishermanConfigManager;
import com.github.majisyou.fishing_plugin.Fishing_plugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Villager {
    //このクラスは、フィッシュを買い取ってくれる人と、釣り竿を売ってくれる村人を召喚するためのメソッドがある
    //メインのメソッドは村人の取引をfisher_man.ymlから読み取って代入するコマンド

    private static Fishing_plugin plugin = Fishing_plugin.getInstance();
    private static FileConfiguration config = new CustomConfigSetting(plugin,"fisher_man.yml").getConfig();

    public static List<MerchantRecipe> MakeRecipes(){
        List<MerchantRecipe> recipes = new ArrayList<>();

        for (String key : config.getConfigurationSection("Villager").getKeys(false)){
            FishermanConfigManager.loadFishermanConfig(key);
            Material sell_material = Material.getMaterial(FishermanConfigManager.getSell_type());
            Material buy_material = Material.getMaterial(FishermanConfigManager.getBuy_type());
            Integer sell_amount = FishermanConfigManager.getSell_amount();
            Integer buy_amount = FishermanConfigManager.getBuy_amount();

            if(!(sell_material == null)){
                if(!(buy_material == null)){
                    if(!(sell_amount == null) && sell_amount < 65){
                        if(!(buy_amount == null) && buy_amount < 65){
                            ItemStack SellItem = new ItemStack(sell_material,sell_amount);
                            ItemStack BuyItem = new ItemStack(buy_material,buy_amount);
                            Integer Sell_CMD = FishermanConfigManager.getSell_CustomModelData();
                            Integer Buy_CMD = FishermanConfigManager.getBuy_CustomModelData();
                            ItemMeta SellItem_Meta = SellItem.getItemMeta();
                            ItemMeta BuyItem_Meta = BuyItem.getItemMeta();
                            if(!(Sell_CMD==null||Sell_CMD==0)){
                                SellItem_Meta.setCustomModelData(Sell_CMD);
                            }
                            if(!(Buy_CMD==null||Buy_CMD==0)){
                                BuyItem_Meta.setCustomModelData(Buy_CMD);
                            }
                            String sell_path = "Villager."+key+".sell.enchants";
                            String buy_path = "Villager."+key+".buy.enchants";
                            int sell_enchant_value;
                            int buy_enchant_value;
                            //ここnullが出るからtryで囲む必要がありか？
                            try{
                                for (String enchant_Key : config.getConfigurationSection(sell_path).getKeys(false)) {
                                    Enchantment sell_enchant = Enchantment.getByName(enchant_Key);
                                    sell_enchant_value = config.getInt(sell_path + "." + enchant_Key);
                                    if (!(sell_enchant == null)&&!(sell_enchant_value==0)) {
                                        SellItem_Meta.addEnchant(sell_enchant, sell_enchant_value, true);
                                    }
                                }
                            }catch (Exception e){
                                plugin.getLogger().info("(FP)"+key+"のsellのエンチャントの中身が無いかな。noneに設定しないとnullが出るよ");
                            }

                            try {
                                for (String enchant_Key2 : config.getConfigurationSection(buy_path).getKeys(false)) {
                                    plugin.getLogger().info("(FP)"+enchant_Key2+"を作成");
                                    Enchantment buy_enchant = Enchantment.getByName(enchant_Key2);
                                    buy_enchant_value = config.getInt(buy_path+"." + enchant_Key2);
                                    if (!((buy_enchant == null)||buy_enchant_value==0)) {
                                        BuyItem_Meta.addEnchant(buy_enchant, buy_enchant_value, true);
                                    }
                                }
                            }catch (Exception e){
                                plugin.getLogger().info("(FP)"+key+"のbuyのエンチャントの中身が無いかな。noneに設定しないとnullが出るよ");
                            }

                            SellItem.setItemMeta(SellItem_Meta);
                            BuyItem.setItemMeta(BuyItem_Meta);
                            MerchantRecipe recipe = new MerchantRecipe(SellItem,2000000);
                            recipe.addIngredient(BuyItem);
                            recipes.add(recipe);
                        }else {
                            plugin.getLogger().info("(FP)"+key+"のbuy_amountが空か65以上だよ");
                        }
                    }else {
                        plugin.getLogger().info("(FP)"+key+"のsell_amountが空か65以上だよ");
                    }
                }else {
                    plugin.getLogger().info("(FP)"+key+"buyItemのTypeが空だよ");
                }
            }else {
                plugin.getLogger().info("(FP)"+key+"sellItemのTypeが空だよ");
            }
        }
        return recipes;
    }
}
