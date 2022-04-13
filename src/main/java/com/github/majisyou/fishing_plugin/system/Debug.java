package com.github.majisyou.fishing_plugin.system;

import com.github.majisyou.fishing_plugin.Config.*;
import com.github.majisyou.fishing_plugin.Fishing_plugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Debug {
    //色々デバッグするためのメソッド、正直全部いらないｗ

    private static Fishing_plugin plugin = Fishing_plugin.getInstance();
    private static List<String> Biome_list;
    private static int List_Length;
    private static FileConfiguration config = new  CustomConfigSetting(plugin,"fisher_man.yml").getConfig();

    public static List<String> Debug_Fish(FileConfiguration config, String rank, String time) {
        List<String> Fish = new ArrayList<>();
        FishSystem.FishListSetup(Fish);
        Fish.set(0, "which?");
        Integer fish_id = 0;
        plugin.getLogger().info(Fish.get(0) + "を代入した");
        if (config.contains(rank)) {
            int FaileProbability = config.getInt(rank + ".FaileProbability");
            plugin.getLogger().info("FaileProbabilityを代入した");
            for (int i = 0; i < FaileProbability; i++) {
                try {
                    BiomeConfigManager.loadBiome(config,rank);
                    int id = new SecureRandom().nextInt(BiomeConfigManager.getFish_id().size()) + 1;
                    plugin.getLogger().info("idを決めたよ" + id);

                    try {
                        fish_id = config.getIntegerList(rank + ".id").get(id - 1);
                    }catch (Exception e){
                        plugin.getLogger().info(config.getName()+"の中の"+rank+".idが存在していないよ：" + id+"番目");
                        return Fish;
                    }

                    plugin.getLogger().info(rank+"からidをもらった" + fish_id);

                    try {
                        //よほどのことがない限りここはnullにならない
                        FishConfigManager.LoadFishConfig(fish_id);
                    }catch (Exception e){
                        plugin.getLogger().info("fish.ymlの中のid"+fish_id+"が存在しないかどこか間違えてない？");
                        return Fish;
                    }
                    plugin.getLogger().info("fishを呼び出したよ");
                    if (FishSystem.time(time, FishConfigManager.getTime())) {
                        plugin.getLogger().info("タイムが合致したー");
                        Fish.set(1, FishConfigManager.getName());
                        plugin.getLogger().info(Fish.get(1) + "を代入した！");
                        Fish.set(2, FishConfigManager.get_cm());
                        plugin.getLogger().info(Fish.get(2) + "を代入した！");
                        Fish.set(3, FishConfigManager.getGetExp());
                        plugin.getLogger().info(Fish.get(3) + "を代入した！");
                        Fish.set(4, FishConfigManager.getSell_price());
                        plugin.getLogger().info(Fish.get(4) + "を代入した！");
                        Fish.set(5, FishConfigManager.getTexture_number());
                        plugin.getLogger().info(Fish.get(5) + "を代入した！");
                        Fish.set(6, FishConfigManager.getLore().get(0));
                        plugin.getLogger().info(Fish.get(6) + "を代入した！");
                        Fish.set(7, FishConfigManager.getLore().get(1));
                        plugin.getLogger().info(Fish.get(7) + "を代入した！");
                        Fish.set(8, FishConfigManager.getLore().get(2));
                        plugin.getLogger().info(Fish.get(8) + "を代入した！");
                        Fish.set(9, FishConfigManager.getLore().get(3));
                        plugin.getLogger().info(Fish.get(9) + "を代入した！");
                        Fish.set(10, rank);
                        plugin.getLogger().info(Fish.get(10) + "を代入した！");
                        Fish.set(11, "何も設定いらないかな、getTimeがStringの時代の名残、消すと色々番号変えないといけないから面倒くさいという気分になった");
                        plugin.getLogger().info(Fish.get(11) + "を代入した！");
                        Fish.set(12, fish_id.toString());
                        plugin.getLogger().info(Fish.get(12) + "を代入した！");
                        Fish.set(0, "Catch!");
                        plugin.getLogger().info(Fish.get(0) + "を代入した！");
                        return Fish;
                    }
                }catch (Exception e){
                    plugin.getLogger().info("idを決めるときにエラー");
                }

            }
            plugin.getLogger().info("合致するタイムが見つからなかった");
            Fish.set(0, "Escaped");
            return Fish;
        }
        plugin.getLogger().info("プレイヤーのいるバイオームのコンフィグ中に何も設定されていないと思う");
        return Fish;
    }

    public static FileConfiguration Debug_Biomeyml(String biome){
        FileConfiguration config = new CustomConfigSetting(plugin,biome+".yml").getConfig();
        if(!(config == null))return config;
        plugin.getLogger().info("Biome.ymlが無かったから普通のconfig.ymlを読み込んだ");
        config = new CustomConfigSetting(plugin).getConfig();
        return config;
    }

    public static boolean fishing_rod(Player player) {
        //customモデルデータを持っていたらtrue、e//cutomモデルデータを持っていなかったらfalse
        if (player.getInventory().getItemInMainHand().getType().equals(Material.AIR) && player.getInventory().getItemInOffHand().getType().equals(Material.AIR))
            return false;
        plugin.getLogger().info(player.getInventory().getItemInMainHand() + "だったよ mainhand");
        plugin.getLogger().info(player.getInventory().getItemInOffHand() + "だったよ offhand");
        ItemStack fishing_rod = player.getInventory().getItemInMainHand();
        plugin.getLogger().info(fishing_rod + "だったよ mainfish");
        ItemStack fishing_rod2 = player.getInventory().getItemInOffHand();
        plugin.getLogger().info(fishing_rod2 + "だったよ offfish");
        if (!(fishing_rod.getType().equals(Material.FISHING_ROD) || fishing_rod2.getType().equals(Material.FISHING_ROD))) {
            plugin.getLogger().info("どっちもfising_rodじゃないため。");
            return false;
        }
        try {
            if ((fishing_rod.getType().equals(Material.FISHING_ROD) && fishing_rod2.getType().equals(Material.FISHING_ROD)))
                return fishing_rod.getItemMeta().hasCustomModelData();

            return fishing_rod.getItemMeta().hasCustomModelData() || fishing_rod2.getItemMeta().hasCustomModelData();
        } catch (Exception e) {
            plugin.getLogger().info("釣り竿にカスタムモデルデータが無い");
            return false;
        }
    }

    public static void RealTime() {
        System.out.println(ZonedDateTime.now());
    }

    public static void inventory_test(Player player) {
        Inventory test = Bukkit.createInventory(null, 63);
        player.openInventory(test);
    }

    public static ItemStack MakeFish(List<String> fish,String player_name) {
        //setDisplayNameが非推奨らしいんですけど
        //これ以外にアイテムの名前を変えられるのを見つけられなかった
        //この非推奨、のメソッド以外でできるやつが欲しい。

        ItemStack FishItem = new ItemStack(Material.COD, 1);
        ItemMeta FishMeta = FishItem.getItemMeta();
        List<String> size_calculate = Debug.Size_calculate();
        plugin.getLogger().info(size_calculate.get(0) + size_calculate.get(1) + "ここに結果");
        double fish_size = Double.parseDouble(fish.get(2)) * Double.parseDouble(size_calculate.get(0));
        fish_size = (Math.floor(fish_size * 10)) / 10;
        String fishing_time = ZonedDateTime.now().toString().substring(0,10);

        plugin.getLogger().info("魚の大きさ計算" + fish_size);

        if (FishMeta == null) return FishItem;
        if (fish.get(10).equals("rank1")) FishMeta.setDisplayName(fish.get(1) + size_calculate.get(1));
        if (fish.get(10).equals("rank2"))
            FishMeta.setDisplayName(ChatColor.YELLOW + fish.get(1) + size_calculate.get(1));
        if (fish.get(10).equals("rank3")) FishMeta.setDisplayName(ChatColor.AQUA + fish.get(1) + size_calculate.get(1));
        if (fish.get(10).equals("rank4"))
            FishMeta.setDisplayName(ChatColor.LIGHT_PURPLE + fish.get(1) + size_calculate.get(1));
        if (fish.get(10).equals("rank5"))
            FishMeta.setDisplayName(ChatColor.GREEN + fish.get(1) + size_calculate.get(1));
        if (!FishMeta.hasDisplayName()) FishMeta.setDisplayName(ChatColor.GOLD + fish.get(1) + size_calculate.get(1));

        List<String> FishLore = new ArrayList<>();

        FishLore.add(ChatColor.WHITE + fish.get(6));
        plugin.getLogger().info(ChatColor.WHITE + fish.get(6));
        FishLore.add(ChatColor.WHITE + fish.get(7));
        plugin.getLogger().info(ChatColor.WHITE + fish.get(7));
        FishLore.add(ChatColor.WHITE + fish.get(8));
        plugin.getLogger().info(ChatColor.WHITE + fish.get(8));
        FishLore.add(ChatColor.WHITE + fish.get(9));
        plugin.getLogger().info(ChatColor.WHITE + fish.get(9));
        FishLore.add("");
        FishLore.add(ChatColor.WHITE + "  大きさ：" + fish_size + "cm");
        plugin.getLogger().info(ChatColor.WHITE + "  大きさ：" + fish_size + "cm");
        FishLore.add(ChatColor.WHITE + "  時間  ：" + fishing_time);
        plugin.getLogger().info(ChatColor.WHITE + "  時間  ：" + fish.get(11));
        FishLore.add(ChatColor.WHITE + "  by " + player_name);
        plugin.getLogger().info(ChatColor.WHITE + "  by " + player_name);
        FishLore.add(ChatColor.WHITE + "  " + fish.get(10));
        plugin.getLogger().info(ChatColor.WHITE + "  by " + player_name);

        FishMeta.setLore(FishLore);
        FishMeta.setCustomModelData(Integer.parseInt(fish.get(5)));
        FishItem.setItemMeta(FishMeta);

        return FishItem;
    }

    public static List<MerchantRecipe> MakeRecipes(){
        List<MerchantRecipe> recipes = new ArrayList<>();

        for (String key : config.getConfigurationSection("Villager").getKeys(false)){
            FishermanConfigManager.loadFishermanConfig(key);

            Material sell_material = Material.getMaterial(FishermanConfigManager.getSell_type());
            plugin.getLogger().info("sellMaterial："+sell_material);
            Material buy_material = Material.getMaterial(FishermanConfigManager.getBuy_type());
            plugin.getLogger().info("buyMaterial："+buy_material);
            Integer sell_amount = FishermanConfigManager.getSell_amount();
            plugin.getLogger().info("sell_amount："+sell_amount);
            Integer buy_amount = FishermanConfigManager.getBuy_amount();
            plugin.getLogger().info("buy_amount："+buy_amount);

            if(!(sell_material == null)){
                if(!(buy_material == null)){
                    if(!(sell_amount == null) && sell_amount < 65){
                        if(!(buy_amount == null) && buy_amount < 65){
                            ItemStack SellItem = new ItemStack(sell_material,sell_amount);
                            plugin.getLogger().info("SellItem："+SellItem);
                            ItemStack BuyItem = new ItemStack(buy_material,buy_amount);
                            plugin.getLogger().info("BuyItem："+BuyItem);
                            Integer Sell_CMD = FishermanConfigManager.getSell_CustomModelData();
                            plugin.getLogger().info("Sell_CMD："+Sell_CMD);
                            Integer Buy_CMD = FishermanConfigManager.getBuy_CustomModelData();
                            plugin.getLogger().info("Buy_CMD："+Buy_CMD);
                            ItemMeta SellItem_Meta = SellItem.getItemMeta();
                            ItemMeta BuyItem_Meta = BuyItem.getItemMeta();

                            if(!(Sell_CMD==0)){
                                plugin.getLogger().info("Sell_CMDはnullじゃない");
                                SellItem_Meta.setCustomModelData(Sell_CMD);
                            }
                            if(!(Buy_CMD==0)){
                                BuyItem_Meta.setCustomModelData(Buy_CMD);
                                plugin.getLogger().info("Buy_CMDはnullじゃない");
                            }

                            //エンチャントを考える設定するやつ

                            SellItem.setItemMeta(SellItem_Meta);
                            BuyItem.setItemMeta(BuyItem_Meta);

                            MerchantRecipe recipe = new MerchantRecipe(SellItem,2000000);
                            recipe.addIngredient(BuyItem);

                            recipes.add(recipe);
                        }else {
                            plugin.getLogger().info(key+"のbuy_amountが空か65以上だよ");
                        }
                    }else {
                        plugin.getLogger().info(key+"のsell_amountが空か65以上だよ");
                    }
                }else {
                    plugin.getLogger().info(key+"buyItemのTypeが空だよ");
                }
            }else {
                plugin.getLogger().info(key+"sellItemのTypeが空だよ");
            }
        }

        return recipes;
    }

    public static String PlayerTime(Player player){
        try{
            List<String> game_time = ConfigManager.getTime();
            Long time = plugin.getServer().getWorld(player.getWorld().getName()).getTime();
            plugin.getLogger().info(time+"の時間を持っているよ");
            if(time>0 && time<=60000){
                plugin.getLogger().info(game_time.get(0));
                return game_time.get(0);
            }
            if(time>60000 && time<=120000) {
                plugin.getLogger().info(game_time.get(1));
                return game_time.get(1);
            }
            if(time>120000 && time<=180000){
                plugin.getLogger().info(game_time.get(2));
                return game_time.get(2);
            }
            if(time>180000 && time<=240000){
                plugin.getLogger().info(game_time.get(3));
                return game_time.get(3);
            }
            plugin.getLogger().info(player.getName()+"の現在時刻は"+time);
            plugin.getLogger().info("虚数時間軸");
            return "虚数時間軸";
        }catch (Exception e){
            plugin.getLogger().info("getWorld.getTimeはNullの可能性があるらしい");
            plugin.getLogger().info(player.getName()+"がいるワールドは"+player.getWorld().getName()+"だよ");
            return "Null出力";
        }

    }

    public static List<String> Size_calculate(){
        double base_random = new Random().nextGaussian();
        double abs_random;
        List<String> fish_size = new ArrayList<>();
        abs_random = Math.abs(base_random)+1;
        plugin.getLogger().info(base_random+"が基準");
        plugin.getLogger().info(abs_random+"が係数");
        fish_size.add(String.valueOf(abs_random));
        if(base_random < ConfigManager.getNone()) {
            plugin.getLogger().info(ConfigManager.getNone()+"ここはどう？");
            //68％
            plugin.getLogger().info("☆無し");
            fish_size.add("");
            return fish_size;
        }
        if(base_random<ConfigManager.getOne()) {
            //27％
            plugin.getLogger().info(ConfigManager.getOne()+"ここはどう？");
            plugin.getLogger().info("☆");
            fish_size.add("☆");
            return fish_size;
        }
        if(base_random<ConfigManager.getTwo()) {
            //4.7％
            plugin.getLogger().info(ConfigManager.getTwo()+"ここはどう？");
            plugin.getLogger().info("☆☆");
            fish_size.add("☆☆");
            return fish_size;
        }
        plugin.getLogger().info("☆☆☆");
        fish_size.add("☆☆☆");
        return fish_size;
    }
}
