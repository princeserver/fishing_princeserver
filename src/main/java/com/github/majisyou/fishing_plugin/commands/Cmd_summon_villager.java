package com.github.majisyou.fishing_plugin.commands;

import com.github.majisyou.fishing_plugin.Fishing_plugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Cmd_summon_villager implements CommandExecutor {
    //フィッシングロッドや、魚を売る人を召喚するコマンド

    private  static Fishing_plugin plugin = Fishing_plugin.getInstance();
    public Cmd_summon_villager(Fishing_plugin plugin){plugin.getCommand("summon_fisherman").setExecutor(this);}

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if(sender instanceof Player){
            List<MerchantRecipe> recipes = new ArrayList<>();

            try{
                recipes = com.github.majisyou.fishing_plugin.system.Villager.MakeRecipes();
            }catch (Exception e){
                plugin.getLogger().info("(FP)"+"fisherman.ymlを読み込んだレシピが作成できなかったよ");
            }

            Player player = (Player) sender;

            if(args.length == 0){
                Entity Fisher_man = player.getWorld().spawnEntity(player.getLocation(),EntityType.VILLAGER);
                Villager villager = (Villager) Fisher_man;
                Fisher_man.setInvulnerable(true);
                villager.setProfession(Villager.Profession.FISHERMAN);
                villager.setVillagerLevel(5);
                villager.setCustomName(ChatColor.GREEN+"釣りマスター");
                villager.setCustomNameVisible(true);

                ArmorStand ArmorStand = (ArmorStand) player.getWorld().spawnEntity(player.getLocation(),EntityType.ARMOR_STAND);
                ArmorStand.setInvisible(true);
                ArmorStand.setMarker(true);
                ArmorStand.addPassenger(Fisher_man);

                try {
                    villager.setRecipes(recipes);

                }catch (Exception e){
                    plugin.getLogger().info("(FP)"+recipes+"を代入できなかった");
                }

            }

            if (args.length==1){
                Entity entity = Bukkit.getEntity(UUID.fromString(args[0]));
                if(entity == null){
                    plugin.getLogger().info("(FP)"+"まずUUIDが違う");
                    return true;
                }

                if (entity.getType().equals(EntityType.VILLAGER)){
                    Villager villager = (Villager) entity;
                    try {
                        villager.setRecipes(recipes);
                        plugin.getLogger().info("(FP)"+"フィッシャーマンを変更したよ");
                    }catch (Exception e){
                        plugin.getLogger().info("(FP)"+recipes+"を代入できなかった");
                    }
                }else {
                    plugin.getLogger().info("(FP)"+"村人がいなかった");
                }
            }
        }else {
            plugin.getLogger().info("これはコンソールからは打てないよ");
        }
        return true;
    }

}
