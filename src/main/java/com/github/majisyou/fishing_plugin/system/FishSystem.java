package com.github.majisyou.fishing_plugin.system;

import com.github.majisyou.fishing_plugin.Config.BiomeConfigManager;
import com.github.majisyou.fishing_plugin.Config.ConfigManager;
import com.github.majisyou.fishing_plugin.Config.CustomConfigSetting;
import com.github.majisyou.fishing_plugin.Config.FishConfigManager;
import com.github.majisyou.fishing_plugin.Fishing_plugin;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.security.SecureRandom;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FishSystem {
    //メインシステムが入ってる。主な動作はここに集約される

    private static Fishing_plugin plugin = Fishing_plugin.getInstance();
    private static List<String> Biome_list;
    private static int List_Length;

    //config.ymlに設定されているBiomeコンフィグを作成するメソッド
    public static void setup_custom_config(Fishing_plugin plugin) throws Exception {
        //このバイオーム毎の.ymlを作成するメソッド
        if(new CustomConfigSetting(plugin).getConfig().contains("Biome")){
            Biome_list = ConfigManager.getBiome();
            for (List_Length=0;List_Length < Biome_list.size();List_Length++){
                String Config_name = Biome_list.get(List_Length)+".yml";
                new CustomConfigSetting(plugin,Config_name).createConfig();
            }
        }else {
            plugin.getLogger().info("Config.ymlの中のBiomeに何も入力が無いよ");
        }
    }

    //Biomeコンフィグを読み込むメソッドだが動かず
    public static void load_config(){
        //このバイオーム毎の.ymlをロードするメソッド
        Biome_list = ConfigManager.getBiome();
        for (List_Length=1;List_Length < Biome_list.size();List_Length++){
            new CustomConfigSetting(plugin,Biome_list.get(List_Length)+".yml");
        }
    }

    //Fish.ymlから、色々な情報を取り込み、List上で保存する
    public static List<String> Fish(Player player){
        FileConfiguration config = FishSystem.Biomeyml(player);//playerがいるバイオームのコンフィグをロードする
        String rank = FishSystem.SelectRank();//ランクをランダムに選ぶ。偏りを設定して
        BiomeConfigManager.loadBiome(config,rank);//ランクごとにコンフィグをロードする
        String time = FishSystem.PlayerTime(player);

        List<String> Fish = new ArrayList<>();
        List<String> fish_time = new ArrayList<>();
        List<String> fish_lore = new ArrayList<>();

        FishSystem.FishListSetup(Fish);
        Fish.set(0,"which?");
        Integer fish_id = 0;
        int id = 0;

        if(config.contains(rank)){
            int FaileProbability = BiomeConfigManager.getFaileProbability();
            for (int i=0;i < FaileProbability;i++){
                try {
                    BiomeConfigManager.loadBiome(config,rank);
                    id = new SecureRandom().nextInt(BiomeConfigManager.getFish_id().size())+1;
                } catch (Exception e){
                    plugin.getLogger().info(player.getWorld().getBiome(player.getLocation())+"コンフィグの"+rank+".idが設定されてないかな");
                    break;
                }

                try {
                    fish_id = BiomeConfigManager.getFish_id().get(id - 1);
                }catch (Exception e){
                    plugin.getLogger().info(config.getName()+"の中の"+rank+".idが存在していないよ：" + id+"番目");
                    return Fish;
                }

                try {
                    if(!(fish_id==0)){

                        FishConfigManager.LoadFishConfig(fish_id);
                        fish_time = FishConfigManager.getTime();

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

                    if(!(fish_id==0)&&!(fish_time.size()==0)){
                        if (FishSystem.time(time,fish_time)){
                            Fish.set(1,FishConfigManager.getName());
                            Fish.set(2,FishConfigManager.get_cm());
                            Fish.set(3,FishConfigManager.getGetExp());
                            Fish.set(4,FishConfigManager.getSell_price());
                            Fish.set(5,FishConfigManager.getTexture_number());
                            Fish.set(6,fish_lore.get(0));
                            Fish.set(7,fish_lore.get(1));
                            Fish.set(8,fish_lore.get(2));
                            Fish.set(9,fish_lore.get(3));
                            Fish.set(10,rank);
                            Fish.set(11,"何も設定いらないかな、getTimeがStringの時代の名残、消すと色々番号変えないといけないから面倒くさいという気分になった");
                            Fish.set(12,fish_id.toString());
                            Fish.set(0,"Catch!");
                            return Fish;
                        }
                    }

                }catch (Exception e){
                    plugin.getLogger().info("fish.ymlの中のid"+fish_id+"が存在しないかどこか間違えてない？");
                    return Fish;
                }
            }
            Fish.set(0,"Escaped");
            return Fish;
        }else {
            plugin.getLogger().info(player.getWorld().getBiome(player.getLocation())+"コンフィグ中に"+rank+"が無い");
        }
        return Fish;
    }

    public static boolean time(String playertime,List<String> fish_time){
        try {
            if(!(fish_time.size()==0)){
                for (String time : fish_time) {
                    if (playertime.equals(time))
                        return true;
                }
                return fish_time.get(0).equals("all day");
            }
            return false;
        }catch (Exception e){
            plugin.getLogger().info(fish_time+"の中身がないのでは？");
            return false;
        }
    }

    //フィッシングロッドを持っているかどうかを判定するメソッド
    //フィッシングロッドを右手に持っていたらtrue
    //フィッシングロッドを左手に持っていたらtrue
    //フィッシングロッドを両手に持っていて、右手に持っているフィッシングロッドが
    //カスタムモデルデータがあったらtrue
    public static boolean fishing_rod(Player player){
        //customモデルデータを持っていたらtrue、//cutomモデルデータを持っていなかったらfalse
        if(player.getInventory().getItemInMainHand().getType().equals(Material.AIR) && player.getInventory().getItemInOffHand().getType().equals(Material.AIR)) return false;
        ItemStack fishing_rod = player.getInventory().getItemInMainHand();
        ItemStack fishing_rod2 = player.getInventory().getItemInOffHand();
        if(!(fishing_rod.getType().equals(Material.FISHING_ROD) || fishing_rod2.getType().equals(Material.FISHING_ROD)))
            return false;
        try{
            if((fishing_rod.getType().equals(Material.FISHING_ROD) && fishing_rod2.getType().equals(Material.FISHING_ROD)))
                return fishing_rod.getItemMeta().hasCustomModelData();
            if(fishing_rod.getType().equals(Material.FISHING_ROD))
                return fishing_rod.getItemMeta().hasCustomModelData();
            if (fishing_rod2.getType().equals(Material.FISHING_ROD))
                return fishing_rod2.getItemMeta().hasCustomModelData();
        }catch (Exception ignored) {}
        return false;
    }

    //魚のサイズを計算するメソッド、
    //fish.ymlのsizeに正規分布をかけて、大きさによって☆☆☆マークを付ける
    public static List<String> Size_calculate(){
        double base_random = new Random().nextGaussian();
        double abs_random;
        List<String> fish_size = new ArrayList<>();
        abs_random = Math.abs(base_random)+1;
        fish_size.add(String.valueOf(abs_random));
        if(base_random< ConfigManager.getNone()) {
            //68％
            fish_size.add("");
            return fish_size;
        }
        if(base_random<ConfigManager.getOne()) {
            //27％
            fish_size.add("☆");
            return fish_size;
        }
        if(base_random<ConfigManager.getTwo()) {
            //4.7％
            fish_size.add("☆☆");
            return fish_size;
        }
        fish_size.add("☆☆☆");
        return fish_size;
    }

    //現在のマイクラ内の時間を判定するメソッド
    //正直、マイクラ時間は合っているかどうかわからないから、要検討しなければならないところ
    //fish.ymlを変えたから今は使っておらず
    public static boolean fishing_time(String Fishingtime,String fishtime){
        //Fishingtimeは釣れた時間
        //fishingtimeは釣れた魚の釣れる時間帯
        if (fishtime.equals("all day"))return true;
        if (fishtime.equals(Fishingtime)) return true;
        return false;
    }

    //代入する時にset(1,value)がnullが出てくるから、フィッシュをあらかじめ作るメソッド
    public static void FishListSetup(List<String> fish){
        if(fish.size()==0){
           fish.add("setup_capture");
           fish.add("setup_name");
           fish.add("setup_cm");
           fish.add("setup_GetEXP");
           fish.add("setup_SellPrice");
           fish.add("setup_Texture_number");
           fish.add("setup_lore1");
           fish.add("setup_lore2");
           fish.add("setup_lore3");
           fish.add("setup_lore4");
           fish.add("setup_rank");
           fish.add("setup_time");
           fish.add("fish_id");
        }
    }

    //Playerの現在のマイクラ時間を判定するメソッド
    //正直、マイクラ時間は合っているかどうかわからないから、要検討しなければならないところ
    public static String PlayerTime(Player player){
        try{
            Long time = plugin.getServer().getWorld(player.getWorld().getName()).getTime();
            if(time>0 && time<=6000) return ConfigManager.getTime().get(0);
            if(time>6000 && time<=12000) return ConfigManager.getTime().get(1);
            if(time>12000 && time<=18000) return ConfigManager.getTime().get(2);
            if(time>18000 && time<=24000) return ConfigManager.getTime().get(3);
            plugin.getLogger().info(player.getName()+"の現在時刻は"+time);
            return "虚数時間軸";
        }catch (Exception e){
            plugin.getLogger().info("getWorld.getTimeはNullの可能性があるらしい");
            plugin.getLogger().info(player.getName()+"がいるワールドは"+player.getWorld().getName()+"だよ");
            return "Null出力";
        }

    }

    //Fishアイテムを作成するメソッド。
    public static ItemStack MakeFish(List<String> fish,Player player){
        //setDisplayNameが非推奨らしいんですけど
        //これ以外にアイテムの名前を変えられるのを見つけられなかった
        //この非推奨、のメソッド以外でできるやつが欲しい。

        String Fish_name = fish.get(1);
        String cm = fish.get(2);
        String getExp = fish.get(3);
        String sell_price  = fish.get(4);
//        String time = fish.get(11);
        int texture_number = 0;
        List<String> Lore = new ArrayList<>();
        String rank= fish.get(10);
        List<String> size_calculate = FishSystem.Size_calculate();
        String id = fish.get(12);
        try {
            texture_number = Integer.parseInt(fish.get(5));
        }catch (Exception e){
            plugin.getLogger().info(Fish_name+"で"+"id"+id+"のテクスチャ番号が設定されてない");
        }
        double fish_size = (Math.floor((Double.parseDouble(cm) * Double.parseDouble(size_calculate.get(0)))*10))/10;
        String star = size_calculate.get(1); //ここにsize_calculateメソッドで計算した☆の数を代入
        Lore.add(fish.get(6));
        Lore.add(fish.get(7));
        Lore.add(fish.get(8));
        Lore.add(fish.get(9));
        String fishing_time = ZonedDateTime.now().toString().substring(0,10);

        ItemStack Fish_Item = new ItemStack(Material.TROPICAL_FISH,1);
        ItemMeta FishItemMeta = Fish_Item.getItemMeta();

        if(FishItemMeta == null) return Fish_Item;
        if(fish.get(10).equals("rank1"))FishItemMeta.setDisplayName(ChatColor.WHITE+Fish_name+star);
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

    //Biomeコンフィグを作るメソッド
    public static FileConfiguration Biomeyml(Player player){
        //プレイヤーのいる場所のバイオームの名前を取ってきて、バイオームの名前のconfigを読み込む
        //返り値は読み込んだ.ymlファイル
        FileConfiguration config = new CustomConfigSetting(plugin,player.getWorld().getBiome(player.getLocation()).name()+".yml").getConfig();
        if(!(config == null))return config;
        plugin.getLogger().info("Biome.ymlが無かったから普通のconfig.ymlを読み込んだ");
        config = new CustomConfigSetting(plugin).getConfig();
        return config;
    }

    //重みを付けた確率を付けるためのメソッド
    public static int SumRanks(){
        //各rankの出現比を足した値を代入するメソッド.SelectRankメソッドでrankを選択するために使う
        int sum=0;
        for(String key: plugin.getConfig().getConfigurationSection("rank_number.rank").getKeys(false)){
            sum += plugin.getConfig().getInt("rank_number.rank." + key);
        }
        return sum;
    }

    //config.ymlに設定した確率でランクを出すメソッド
    public static String SelectRank(){
        //概要
        //1~出現数MAX(基本は100)のランダムなセレクト番号を生成し、
        //例えば10以下であったらrank0
        //セレクト番号から10を引いて
        //20以下であったらrank1　というようにしている
        //0-10 10-30 30-60 60-100 というように場合わけするのを重みを引くことによって
        //0-10 0-20 0-30 というように場合わけをしている
        int Select_number = new SecureRandom().nextInt(FishSystem.SumRanks()-1)+1;

        if(Select_number < ConfigManager.getRank9()){
            return "rank9";
        }
        Select_number -= ConfigManager.getRank9();

        if(Select_number < ConfigManager.getRank8()){
            return "rank8";
        }
        Select_number -= ConfigManager.getRank8();

        if(Select_number < ConfigManager.getRank7()){
            return "rank7";
        }
        Select_number -= ConfigManager.getRank7();

        if(Select_number < ConfigManager.getRank6()){
            return "rank6";
        }
        Select_number -= ConfigManager.getRank6();

        if(Select_number < ConfigManager.getRank5()){
            return "rank5";
        }
        Select_number -= ConfigManager.getRank5();

        if(Select_number < ConfigManager.getRank4()){
            return "rank4";
        }
        Select_number -= ConfigManager.getRank4();

        if(Select_number < ConfigManager.getRank3()){
            return "rank3";
        }
        Select_number -= ConfigManager.getRank3();

        if(Select_number < ConfigManager.getRank2()){
            return "rank2";
        }
        Select_number -= ConfigManager.getRank2();

        if(Select_number < ConfigManager.getRank1()){
            return "rank1";
        }

        return "rankNull-"+Select_number;
    }

}
