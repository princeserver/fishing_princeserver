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

    public static List<String> Fish(Player player){
        FileConfiguration config = FishSystem.Biomeyml(player);//playerがいるバイオームのコンフィグをロードする
        plugin.getLogger().info(config+"を読み込んだ");
        String rank = FishSystem.SelectRank();//ランクをランダムに選ぶ。偏りを設定して
        plugin.getLogger().info(rank+"を読み込んだ");
        BiomeConfigManager.loadBiome(config,rank);//ランクごとにコンフィグをロードする
        String time = FishSystem.PlayerTime(player);
        plugin.getLogger().info(time+"であった");

        List<String> Fish = new ArrayList<>();
        List<String> fish_time = new ArrayList<>();
        List<String> fish_lore = new ArrayList<>();

        FishSystem.FishListSetup(Fish);
        plugin.getLogger().info(Fish+"を作成した");
        Fish.set(0,"which?");
        Integer fish_id = 0;
        int id = 0;
        if(config.contains(rank)){
            int FaileProbability = BiomeConfigManager.getFaileProbability();
            for (int i=0;i < FaileProbability;i++){
                try {
                    BiomeConfigManager.loadBiome(config,rank);
                    plugin.getLogger().info("バイオームコンフィグの中を読み込んだ");
                    id = new SecureRandom().nextInt(BiomeConfigManager.getFish_id().size())+1;
                    plugin.getLogger().info("idを"+id+"に設定した");
                } catch (Exception e){
                    plugin.getLogger().info(player.getWorld().getBiome(player.getLocation())+"コンフィグの"+rank+".idが設定されてないかな");
                    break;
                }

                try {
                    fish_id = BiomeConfigManager.getFish_id().get(id - 1);
                    plugin.getLogger().info(rank+"の"+id+"番目の魚のidは"+fish_id);
                }catch (Exception e){
                    plugin.getLogger().info(config.getName()+"の中の"+rank+".idが存在していないよ：" + id+"番目");
                    return Fish;
                }

                try {
                    //よほどのことがない限りここはnullにならない
                    if(!(fish_id==0)){
                        FishConfigManager.LoadFishConfig(fish_id);
                        fish_time = FishConfigManager.getTime();
                        plugin.getLogger().info(fish_time+"を読み込んだ");
                        if(fish_time.size()==0){
                            plugin.getLogger().info("idが"+fish_id+"のtimeが無いよ");
                        }
                        if(FishConfigManager.getLore().size()==4){
                            fish_lore = FishConfigManager.getLore();
                        }else {
                            fish_lore.add("プラグイン上でバグが出てる");
                            fish_lore.add("fishのidと");
                            fish_lore.add("自分の現在地のバイオームを");
                            fish_lore.add("サーバ主に教えて上げてね");
                            plugin.getLogger().info("id"+fish_id+"の魚のloreが設定されていない");
                        }

                    }else {
                        plugin.getLogger().info("指定された魚がidが0の魚だよ");
                    }

                }catch (Exception e){
                    plugin.getLogger().info("fish.ymlの中のid"+fish_id+"が存在しないかどこか間違えてない？");
                    return Fish;
                }
                if(!(fish_id==0)&&!(fish_time.size()==0)){
                    plugin.getLogger().info("ここまでは読み込んでる");
                    if (FishSystem.time(time,fish_time)){
                        Fish.set(1,FishConfigManager.getName());
                        plugin.getLogger().info(Fish.get(1));
                        Fish.set(2,FishConfigManager.get_cm());
                        plugin.getLogger().info(Fish.get(2));
                        Fish.set(3,FishConfigManager.getGetExp());
                        plugin.getLogger().info(Fish.get(3));
                        Fish.set(4,FishConfigManager.getSell_price());
                        plugin.getLogger().info(Fish.get(4));
                        Fish.set(5,FishConfigManager.getTexture_number());
                        plugin.getLogger().info(Fish.get(5));
                        //loreが設定されていなかったらnullが出てそうな気分
                        Fish.set(6,fish_lore.get(0));
                        plugin.getLogger().info(Fish.get(6));
                        Fish.set(7,fish_lore.get(1));
                        plugin.getLogger().info(Fish.get(7));
                        Fish.set(8,fish_lore.get(2));
                        plugin.getLogger().info(Fish.get(8));
                        Fish.set(9,fish_lore.get(3));
                        plugin.getLogger().info(Fish.get(9));
                        Fish.set(10,rank);
                        plugin.getLogger().info(Fish.get(10));
                        Fish.set(11,"何も設定いらないかな、getTimeがStringの時代の名残、消すと色々番号変えないといけないから面倒くさいという気分になった");
                        plugin.getLogger().info(Fish.get(11));
                        Fish.set(12,fish_id.toString());
                        plugin.getLogger().info(Fish.get(12));
                        Fish.set(0,"Catch!");
                        plugin.getLogger().info(Fish.get(0));
                        return Fish;
                    }
                }
            }
            Fish.set(0,"Escaped");
            plugin.getLogger().info(Fish.get(0));
            return Fish;
        }else {
            plugin.getLogger().info(player.getWorld().getBiome(player.getLocation())+"コンフィグ中に"+rank+"が無い");
        }
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

    public static void inventory_test(Player player) {
        Inventory test = Bukkit.createInventory(null, 63);
        player.openInventory(test);
    }

    public static ItemStack MakeFish(List<String> fish,Player player){
        //setDisplayNameが非推奨らしいんですけど
        //これ以外にアイテムの名前を変えられるのを見つけられなかった
        //この非推奨、のメソッド以外でできるやつが欲しい。
        plugin.getLogger().info("MakeFishを呼び出した");
        String Fish_name = fish.get(1);
        plugin.getLogger().info(Fish_name+"の魚");
        String cm = fish.get(2);
        plugin.getLogger().info(cm+"cmの魚");
        String getExp = fish.get(3);
        plugin.getLogger().info(getExp+"もらえて");
        String sell_price  = fish.get(4);
        plugin.getLogger().info(sell_price+"Eもらえて");
//        String time = fish.get(11);
        Integer texture_number = 0;//ここでnullが発生する
        List<String> Lore = new ArrayList<>();
        String rank= fish.get(10);
        plugin.getLogger().info(rank+"の魚");

        //sizeの計算
        List<String> size_calculate = FishSystem.Size_calculate();
        String id = fish.get(12);
        try {
            texture_number = Integer.parseInt(fish.get(5));
        }catch (Exception e){
            plugin.getLogger().info(Fish_name+"の"+"id"+id+"のテクスチャ番号が設定されてない");
        }

        plugin.getLogger().info(texture_number+"のカスタムモデルデータ");


        double fish_size = (Math.floor((Double.parseDouble(cm) * Double.parseDouble(size_calculate.get(0)))*10))/10;
        plugin.getLogger().info(fish_size+"cmの大きさ");
        String star = size_calculate.get(1); //ここにsize_calculateメソッドで計算した☆の数を代入
        plugin.getLogger().info(star+"の大きさ");
        Lore.add(fish.get(6));
        Lore.add(fish.get(7));
        Lore.add(fish.get(8));
        Lore.add(fish.get(9));
        plugin.getLogger().info(Lore+"のロアー");
        //時間の判別
        String fishing_time = ZonedDateTime.now().toString().substring(0,10);
        plugin.getLogger().info(fishing_time);

        ItemStack Fish_Item = new ItemStack(Material.TROPICAL_FISH,1);
        ItemMeta FishItemMeta = Fish_Item.getItemMeta();

        plugin.getLogger().info("FishItemを作る所までは呼び出した");

        if(fish.get(10).equals("rank1"))FishItemMeta.setDisplayName(Fish_name+star);
        if(fish.get(10).equals("rank2"))FishItemMeta.setDisplayName(ChatColor.YELLOW+Fish_name+star);
        if(fish.get(10).equals("rank3"))FishItemMeta.setDisplayName(ChatColor.AQUA+Fish_name+star);
        if(fish.get(10).equals("rank4"))FishItemMeta.setDisplayName(ChatColor.LIGHT_PURPLE+Fish_name+star);
        if(fish.get(10).equals("rank5"))FishItemMeta.setDisplayName(ChatColor.GREEN+Fish_name+star);
        if(!FishItemMeta.hasDisplayName()) FishItemMeta.setDisplayName(ChatColor.GOLD+Fish_name+star);

        List<String> FishLore = new ArrayList<>();

        FishLore.add(ChatColor.WHITE+"No."+id);
        FishLore.add(ChatColor.WHITE+Lore.get(0));
        FishLore.add(ChatColor.WHITE+Lore.get(1));
        FishLore.add(ChatColor.WHITE+Lore.get(2));
        FishLore.add(ChatColor.WHITE+Lore.get(3));
        FishLore.add("");
        FishLore.add(ChatColor.WHITE+"  大きさ："+fish_size+"cm");
        FishLore.add(ChatColor.WHITE+"  日付 ："+fishing_time);
        FishLore.add(ChatColor.WHITE+"  by "+player.getName());
        FishLore.add(ChatColor.WHITE+"  "+rank);
        FishItemMeta.setLore(FishLore);

        FishItemMeta.setCustomModelData(texture_number);

        Fish_Item.setItemMeta(FishItemMeta);

        return Fish_Item;
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
