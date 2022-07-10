package com.cahrypt.me.soupsigns.listener;

import com.cahrypt.me.soupsigns.SoupSigns;
import org.bukkit.Bukkit;
import org.bukkit.block.Sign;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class AListener implements Listener {

    public AListener() {
        Bukkit.getPluginManager().registerEvents(this, JavaPlugin.getPlugin(SoupSigns.class));
    }

    protected boolean isSoupSign(Sign sign) {
        for (String line : sign.getLines()) {
            if (line.contains("[Free Soup]")) {
                return true;
            }
        }

        return false;
    }
}
