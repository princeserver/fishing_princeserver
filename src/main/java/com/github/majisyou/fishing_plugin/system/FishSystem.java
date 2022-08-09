package com.github.majisyou.fishing_plugin.system;

import com.github.majisyou.fishing_plugin.Config.BiomeConfigManager;
import com.github.majisyou.fishing_plugin.Config.ConfigManager;
import com.github.majisyou.fishing_plugin.Config.CustomConfigSetting;
import com.github.majisyou.fishing_plugin.Config.FishConfigManager;
import com.github.majisyou.fishing_plugin.Fishing_plugin;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

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
    
    //Fish.ymlから、色々な情報を取り込み、List上で保存する
    public static ItemStack MakeFish(Integer id,String playerName,Integer SelectRank){
        ItemStack fish = new ItemStack(Material.TROPICAL_FISH);
        ItemMeta fishMeta = fish.getItemMeta();
        FishConfigManager.LoadFishConfig(id);
        String fishName = FishConfigManager.getName();
        double cm = FishConfigManager.get_cm();
        Integer rank = FishConfigManager.getRank();
        List<String> Lore = new ArrayList<>();
        List<String> note = FishConfigManager.getLore();
        ZonedDateTime NowTime = ZonedDateTime.now();
        String time = NowTime.getYear()+":"+NowTime.getMonthValue()+"/"+NowTime.getDayOfMonth()+"["+NowTime.getHour()+":"+NowTime.getMinute()+"]";
        List<String> size_calculate = FishSystem.Size_calculate();
        double fish_size = (Math.floor((cm * Double.parseDouble(size_calculate.get(0)))*10))/10;
        String star = "§f"+size_calculate.get(1);
        Lore.add(ChatColor.WHITE+"No."+id);
        Lore.addAll(note);
        Lore.add("");
        Lore.add(ChatColor.WHITE+"  大きさ："+fish_size+"cm");
        Lore.add(ChatColor.WHITE+"  日付 ："+time);
        Lore.add(ChatColor.WHITE+"  by "+playerName);
        Lore.add(ChatColor.WHITE+"  rank"+rank);
        RankNameSetFish(fishMeta,rank,fishName+star);
        fishMeta.setCustomModelData(FishConfigManager.getTexture_number());
        fishMeta.setLore(Lore);
        PersistentDataContainer pdc = fishMeta.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(plugin,"sellPrice");
        pdc.set(key, PersistentDataType.INTEGER,FishConfigManager.getSell_price());

        fish.setItemMeta(fishMeta);
        if(!rank.equals(SelectRank)){
            plugin.getLogger().info("(FP)"+"釣った時のランクと釣れた魚のランクが一致していない");
            plugin.getLogger().info("(FP)"+"Id:"+id+" name:"+fishName+" rank:rank"+rank);
            plugin.getLogger().info("(FP)"+"釣れた時のrank:rank"+SelectRank);
        }
        return fish;
    }

    public static void RankNameSetFish(ItemMeta itemMeta,Integer rank,String fishName){
        switch (rank){
            case 0->{
                itemMeta.setDisplayName("§7"+fishName);
            }
            case 1->{
                itemMeta.setDisplayName("§f"+fishName);
            }
            case 2->{
                itemMeta.setDisplayName("§e"+fishName);
            }
            case 3->{
                itemMeta.setDisplayName("§b"+fishName);
            }
            case 4->{
                itemMeta.setDisplayName("§6"+fishName);
            }
            case 5->{
                itemMeta.setDisplayName("§a"+fishName);
            }
            case 6->{
                itemMeta.setDisplayName("§c"+fishName);
            }
            case 7->{
                itemMeta.setDisplayName("§g"+fishName);
            }
            case 8->{
                itemMeta.setDisplayName("§5"+fishName);
            }
            case 9->{
                itemMeta.setDisplayName("§4"+fishName);
            }
            default->{
                itemMeta.setDisplayName("§3"+fishName);
            }

        }

    }

    public static Integer getFishId(Player player,Integer rank){
        String biome = player.getWorld().getBiome(player.getLocation()).name();
        String time = PlayerTime(player);
        BiomeConfigManager.loadBiome(biome,time,rank);
        List<Integer> fishIdList = BiomeConfigManager.getFish_id();
        int index = new Random().nextInt(fishIdList.size());
        return fishIdList.get(index);
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

    //Playerの現在のマイクラ時間を判定するメソッド
    //正直、マイクラ時間は合っているかどうかわからないから、要検討しなければならないところ
    public static String PlayerTime(Player player){
        try{
            long time = plugin.getServer().getWorld(player.getWorld().getName()).getTime();
            if(time>0 && time<=6000) return ConfigManager.getTime().get(0);
            if(time>6000 && time<=12000) return ConfigManager.getTime().get(1);
            if(time>12000 && time<=18000) return ConfigManager.getTime().get(2);
            if(time>18000 && time<=24000) return ConfigManager.getTime().get(3);
            plugin.getLogger().info(player.getName()+"の現在時刻は"+time);
            return "虚数時間軸";
        }catch (Exception e){
            plugin.getLogger().info("(FP)"+"getWorld.getTimeはNullの可能性があるらしい");
            plugin.getLogger().info("(FP)"+player.getName()+"がいるワールドは"+player.getWorld().getName()+"だよ");
            return "Null出力";
        }

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
    public static Integer SelectRank(){
        //概要
        //1~出現数MAX(基本は100)のランダムなセレクト番号を生成し、
        //例えば10以下であったらrank0
        //セレクト番号から10を引いて
        //20以下であったらrank1　というようにしている
        //0-10 10-30 30-60 60-100 というように場合わけするのを重みを引くことによって
        //0-10 0-20 0-30 というように場合わけをしている
        int Select_number = new SecureRandom().nextInt(FishSystem.SumRanks()-1)+1;

        if(Select_number < ConfigManager.getRank9()){
            return 9;
        }
        Select_number -= ConfigManager.getRank9();

        if(Select_number < ConfigManager.getRank8()){
            return 8;
        }
        Select_number -= ConfigManager.getRank8();

        if(Select_number < ConfigManager.getRank7()){
            return 7;
        }
        Select_number -= ConfigManager.getRank7();

        if(Select_number < ConfigManager.getRank6()){
            return 6;
        }
        Select_number -= ConfigManager.getRank6();

        if(Select_number < ConfigManager.getRank5()){
            return 5;
        }
        Select_number -= ConfigManager.getRank5();

        if(Select_number < ConfigManager.getRank4()){
            return 4;
        }
        Select_number -= ConfigManager.getRank4();

        if(Select_number < ConfigManager.getRank3()){
            return 3;
        }
        Select_number -= ConfigManager.getRank3();

        if(Select_number < ConfigManager.getRank2()){
            return 2;
        }
        Select_number -= ConfigManager.getRank2();

        if(Select_number < ConfigManager.getRank1()){
            return 1;
        }

        return Select_number;
    }


}
