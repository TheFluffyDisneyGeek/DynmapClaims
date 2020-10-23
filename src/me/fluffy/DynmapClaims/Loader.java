package me.fluffy.DynmapClaims;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Loader extends JavaPlugin {

    @Override
    public void onEnable(){
        ClaimsCommands cmds = new ClaimsCommands(this);
        getCommand("claim").setExecutor(cmds);
        getCommand("deleteclaim").setExecutor(cmds);
        Bukkit.getLogger().info("DynmapClaims Loaded!");
    }

}
