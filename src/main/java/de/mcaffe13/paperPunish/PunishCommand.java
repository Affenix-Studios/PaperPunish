package de.mcaffe13.paperPunish;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PunishCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage("You need to follow all arguments (player and reason) to use this command!");
        String playerName = args[0];
        String reason = args[1];

        return false;
    }
}