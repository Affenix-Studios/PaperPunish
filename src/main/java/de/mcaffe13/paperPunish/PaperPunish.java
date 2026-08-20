package de.mcaffe13.paperPunish;

import org.bukkit.plugin.java.JavaPlugin;

public final class PaperPunish extends JavaPlugin {

    @Override
    public void onEnable() {
        getCommand("punish").setExecutor(new PunishCommand());
        getServer().getPluginManager().registerEvents(new VpnChecker(), this);
        getLogger().info("PaperPunish has been enabled!");

    }

    @Override
    public void onDisable() {
        getLogger().info("PaperPunish has been disabled!");
    }
}