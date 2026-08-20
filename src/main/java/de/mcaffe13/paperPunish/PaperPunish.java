package de.mcaffe13.paperPunish;

import org.bukkit.plugin.java.JavaPlugin;

public final class PaperPunish extends JavaPlugin {

    @Override
    public void onEnable() {
        getCommand("punish").setExecutor(new PunishCommand(this));

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
