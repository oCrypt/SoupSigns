package com.cahrypt.me.soupsigns;

import com.cahrypt.me.soupsigns.data.DataManager;
import com.cahrypt.me.soupsigns.listener.RefillListener;
import com.cahrypt.me.soupsigns.manager.SoupManager;
import com.cahrypt.me.soupsignsapi.SoupSignsAPI;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

public final class SoupSigns extends JavaPlugin implements SoupSignsAPI {
    private DataManager dataManager;
    private SoupManager soupManager;

    @Override
    public void onEnable() {
        this.dataManager = new DataManager();
        this.soupManager = new SoupManager();

        new RefillListener();

        Bukkit.getServicesManager().register(SoupSignsAPI.class, this, this, ServicePriority.High);
        logStatus(true);
    }

    @Override
    public void onDisable() {
        logStatus(false);
    }

    private void logStatus(boolean enabled) {
        Bukkit.getLogger().info("----------------------------------");
        Bukkit.getLogger().info(getDescription().getName() + " version " + getDescription().getVersion() + (enabled ? " enabled " : " disabled ") + "successfully!");
        Bukkit.getLogger().info("Author: " + getDescription().getAuthors());
        Bukkit.getLogger().info("----------------------------------");
    }

    // Overriding methods with object outputs for this project and interface outputs for the API

    @Override
    public DataManager getDataManager() {
        return dataManager;
    }

    @Override
    public SoupManager getSoupManager() {
        return soupManager;
    }
}
