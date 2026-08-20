package de.mcaffe13.paperPunish;

import org.bukkit.Bukkit;
import org.bukkit.BanList;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.util.Date;

public class PunishCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender p, Command cmd, String label, String[] args) {
        if (args.length < 3) {
            p.sendMessage(ChatColor.RED + "Usage: /punish <player> <reason> <duration>");
            return true;
        }

        String inputTime = args[2];
        if (inputTime.length() < 2) {
            p.sendMessage(ChatColor.RED + "Invalid duration format.");
            return true;
        }

        char u = inputTime.charAt(inputTime.length() - 1);
        String rawNum = inputTime.substring(0, inputTime.length() - 1);

        long mult = 0;
        if (u == 's') mult = 1000L;
        if (u == 'm') mult = 60000L;
        if (u == 'h') mult = 3600000L;
        if (u == 'd') mult = 86400000L;

        if (mult == 0) {
            p.sendMessage(ChatColor.RED + "Unknown unit! Use s, m, h or d.");
            return true;
        }

        long finalMs;
        try {
            finalMs = Long.parseLong(rawNum) * mult;
        } catch (Exception ex) {
            p.sendMessage(ChatColor.RED + "Duration must be a number before the unit.");
            return true;
        }

        String pName = args[0];
        String reason = args[1];

        String msg = ChatColor.RED + "You have been banned!\n" +
                ChatColor.YELLOW + "Reason: " + reason + "\n" +
                ChatColor.YELLOW + "Duration: " + inputTime;

        Bukkit.getBanList(BanList.Type.NAME).addBan(pName, msg, new Date(System.currentTimeMillis() + finalMs), p.getName());

        Player t = Bukkit.getPlayer(pName);
        if (t != null) {
            t.kickPlayer(msg);
        }

        p.sendMessage(ChatColor.GREEN + "Successfully banned " + pName);
        return true;
    }
}
