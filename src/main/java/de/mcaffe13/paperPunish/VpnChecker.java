package de.mcaffe13.paperPunish;

import com.google.gson.*;
import org.bukkit.ChatColor;
import org.bukkit.event.*;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import java.io.*;
import java.net.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class VpnChecker implements Listener {

    private final Map<String, Boolean> vpnCache = new ConcurrentHashMap<>();

    @EventHandler
    public void onPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        String ip = event.getAddress().getHostAddress();

        if (ip.equals("127.0.0.1") || ip.equals("0:0:0:0:0:0:0:1") || ip.startsWith("192.168.")) {
            return;
        }

        if (vpnCache.containsKey(ip)) {
            if (vpnCache.get(ip)) kickPlayer(event);
            return;
        }

        try {
            String fullPath = String.format("http://ip-api.com", ip);
            URL url = new URL(fullPath);

            HttpURLConnection apiConnection = (HttpURLConnection) url.openConnection();
            apiConnection.setRequestMethod("GET");
            apiConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)");
            apiConnection.setConnectTimeout(5000);
            apiConnection.setReadTimeout(5000);

            if (apiConnection.getResponseCode() != 200) {
                System.out.println("[PaperPunish] API returned HTTP code: " + apiConnection.getResponseCode());
                return;
            }

            InputStreamReader reader = new InputStreamReader(apiConnection.getInputStream());
            JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
            reader.close();

            if (!json.has("status") || !"success".equals(json.get("status").getAsString())) {
                return;
            }

            boolean isProxy = json.has("proxy") && json.get("proxy").getAsBoolean();
            boolean isHosting = json.has("hosting") && json.get("hosting").getAsBoolean();
            boolean blockConnection = isProxy || isHosting;

            vpnCache.put(ip, blockConnection);

            if (blockConnection) {
                kickPlayer(event);
            }

        } catch (Exception e) {
            System.out.println("[PaperPunish] Connection failed: " + e.getMessage());
        }
    }

    private void kickPlayer(AsyncPlayerPreLoginEvent event) {
        event.disallow(
                AsyncPlayerPreLoginEvent.Result.KICK_BANNED,
                ChatColor.RED + "Please deaktivate the VPN"
        );
    }
}
