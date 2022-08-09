package com.github.majisyou.fishing_plugin.commands;

import com.github.majisyou.fishing_plugin.Config.*;
import com.github.majisyou.fishing_plugin.Fishing_plugin;
import com.github.majisyou.fishing_plugin.system.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Fish;
import org.jetbrains.annotations.NotNull;

public class Cmd_reloadconfig implements CommandExecutor {
    //このコマンド未だに起動せず

    Fishing_plugin plugin = Fishing_plugin.getInstance();

    public Cmd_reloadconfig(Fishing_plugin plugin){plugin.getCommand("fishing_config_reload").setExecutor(this);}

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        ConfigManager.reload();
        FishConfigManager.reload();
        FishermanConfigManager.reload();
        BiomeConfigManager.reload();
        return true;
    }
}
