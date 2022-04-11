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

    private static Fishing_plugin plugin = Fishing_plugin.getInstance();
    private static List<String> Biome_list;
    private static int List_Length;

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

    public static void load_config(){
        //このバイオーム毎の.ymlをロードするメソッド
        Biome_list = ConfigManager.getBiome();
        for (List_Length=1;List_Length < Biome_list.size();List_Length++){
            new CustomConfigSetting(plugin,Biome_list.get(List_Length)+".yml");
        }
    }

    public static List<String> Fish(FileConfiguration config, String rank,String time){
        List<String> Fish = new ArrayList<>();
        FishSystem.FishListSetup(Fish);
        Fish.set(0,"which?");
        if(config.contains(rank)){
            int FaileProbability = config.getInt(rank+".FaileProbability");
            for (int i=0;i <= FaileProbability;i++){
                int id = new SecureRandom().nextInt(BiomeConfigManager.getFish_id().size())+1;
                try {
                    Integer fish_id = config.getIntegerList(rank+".id").get(id-1);
                    FishConfigManager.LoadFishConfig(fish_id);
                    if (FishSystem.fishing_time(time, FishConfigManager.getTime())){
                        Fish.set(1,FishConfigManager.getName());
                        Fish.set(2,FishConfigManager.get_cm());
                        Fish.set(3,FishConfigManager.getGetExp());
                        Fish.set(4,FishConfigManager.getSell_price());
                        Fish.set(5,FishConfigManager.getTexture_number());
                        Fish.set(6,FishConfigManager.getLore().get(0));
                        Fish.set(7,FishConfigManager.getLore().get(1));
                        Fish.set(8,FishConfigManager.getLore().get(2));
                        Fish.set(9,FishConfigManager.getLore().get(3));
                        Fish.set(10,rank);
                        Fish.set(11,FishConfigManager.getTime());
                        Fish.set(12,fish_id.toString());
                        Fish.set(0,"Catch!");
                        return Fish;
                    }
                }catch (Exception e){
                    plugin.getLogger().info("プラグインの中のrank1.idが存在していないよ：→番目："+id);
                    return Fish;
                }
            }
            Fish.set(0,"Escaped");
            return Fish;
        }
        plugin.getLogger().info("プレイヤーのいるバイオームのコンフィグ中に何も設定されていないと思う");
        return Fish;
    }

    public static List<String> Fish_Size(List<String> fish){
        List<String> fish_size = new ArrayList<>();
        return  fish;
    }

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

    public static boolean fishing_time(String Fishingtime,String fishtime){
        //Fishingtimeは釣れた時間
        //fishingtimeは釣れた魚の釣れる時間帯
        if (fishtime.equals("all day"))return true;
        if (fishtime.equals(Fishingtime)) return true;
        return false;
    }

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

    public static String PlayerTime(Player player){
        try{
            Long time = plugin.getServer().getWorld(player.getWorld().getName()).getTime();
            if(time>0 && time<=60000) return ConfigManager.getTime().get(0);
            if(time>60000 && time<=120000) return ConfigManager.getTime().get(1);
            if(time>120000 && time<=180000) return ConfigManager.getTime().get(2);
            if(time>180000 && time<=240000) return ConfigManager.getTime().get(3);
            plugin.getLogger().info(player.getName()+"の現在時刻は"+time);
            return "虚数時間軸";
        }catch (Exception e){
            plugin.getLogger().info("getWorld.getTimeはNullの可能性があるらしい");
            plugin.getLogger().info(player.getName()+"がいるワールドは"+player.getWorld().getName()+"だよ");
            return "Null出力";
        }

    }

    public static ItemStack MakeFish(List<String> fish,Player player){
        //setDisplayNameが非推奨らしいんですけど
        //これ以外にアイテムの名前を変えられるのを見つけられなかった
        //この非推奨、のメソッド以外でできるやつが欲しい。

        String Fish_name = fish.get(1);
        String cm = fish.get(2);
        String getExp = fish.get(3);
        String sell_price  = fish.get(4);
        String time = fish.get(11);
        Integer texture_number= Integer.parseInt(fish.get(5));
        List<String> Lore = new ArrayList<>();
        String rank= fish.get(10);
        List<String> size_calculate = FishSystem.Size_calculate();
        String id = fish.get(12);
        double fish_size = (Math.floor((Double.parseDouble(cm) * Double.parseDouble(size_calculate.get(0)))*10))/10;
        String star = size_calculate.get(1);
        Lore.add(fish.get(6));
        Lore.add(fish.get(7));
        Lore.add(fish.get(8));
        Lore.add(fish.get(9));
        String fishing_time = ZonedDateTime.now().toString().substring(0,10);


        ItemStack Fish_Item = new ItemStack(Material.COD,1);
        ItemMeta FishItemMeta = Fish_Item.getItemMeta();

        if(FishItemMeta == null) return Fish_Item;
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

    public static FileConfiguration Biomeyml(Player player){
        //プレイヤーのいる場所のバイオームの名前を取ってきて、バイオームの名前のconfigを読み込む
        //返り値は読み込んだ.ymlファイル
        FileConfiguration config = new CustomConfigSetting(plugin,player.getWorld().getBiome(player.getLocation()).name()+".yml").getConfig();
        if(!(config == null))return config;
        plugin.getLogger().info("Biome.ymlが無かったから普通のconfig.ymlを読み込んだ");
        config = new CustomConfigSetting(plugin).getConfig();
        return config;
    }

    public static int SumRanks(){
        //各rankの出現比を足した値を代入するメソッド.SelectRankメソッドでrankを選択するために使う
        int sum=0;
        for(String key: plugin.getConfig().getConfigurationSection("rank_number.rank").getKeys(false)){
            sum += plugin.getConfig().getInt("rank_number.rank." + key);
        }
        return sum;
    }

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

    public static void sell_fish(Inventory inventory){

    }

}
