package com.cahrypt.me.soupsigns.data;

import com.cahrypt.me.soupsigns.SoupSigns;
import com.cahrypt.me.soupsignsapi.data.IDataManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataManager implements IDataManager {
    private final SoupSigns main;
    private final Pattern hexPattern;
    private FileConfiguration config;

    private boolean indicatorChange;

    private double delayPerSoup;

    private double delayBeforeRefillCooldown;
    private double refillCooldown;

    private int indicatorX;
    private int indicatorY;
    private int indicatorZ;

    private String cooldownMessage;
    private String fullInventoryMessage;

    private Material indicatorMaterial;
    private Material cooldownIndicatorMaterial;

    private Sound refillSound;

    public DataManager() {
        this.main = JavaPlugin.getPlugin(SoupSigns.class);
        this.hexPattern = Pattern.compile("#[a-fA-f0-9]{6}");
        reloadConfig();
    }

    @Override
    public void reloadConfig() {
        main.reloadConfig();
        main.getConfig().options().copyDefaults();
        main.saveDefaultConfig();

        config = main.getConfig();
        extractConfigVals();
    }

    private void extractConfigVals() {
        indicatorChange = config.getBoolean("do-indicator-change");

        delayPerSoup = config.getDouble("seconds-delay-per-click");

        delayBeforeRefillCooldown = config.getDouble("seconds-delay-before-refill-cooldown");
        refillCooldown = config.getDouble("seconds-refill-cooldown");

        indicatorX = config.getInt("indicator-loc-from-supporting-block.x");
        indicatorY = config.getInt("indicator-loc-from-supporting-block.y");
        indicatorZ = config.getInt("indicator-loc-from-supporting-block.z");

        cooldownMessage = translateCodes(config.getString("put-on-cooldown-message"));
        fullInventoryMessage = translateCodes(config.getString("full-inventory-message"));

        indicatorMaterial = getMaterialFromConfig(config.getString("indicator-block"), Material.LIME_WOOL);
        cooldownIndicatorMaterial = getMaterialFromConfig(config.getString("cooldown-indicator-block"), Material.RED_WOOL);

        refillSound = getSoundFromConfig(config.getString("refill-sound"), Sound.BLOCK_NOTE_BLOCK_BANJO);
    }

    private Material getMaterialFromConfig(String materialString, Material defaultMaterial) {
        Material material = Material.getMaterial(materialString);

        if (material == null) {
            Bukkit.getLogger().info("No such material as " + materialString + "! Using default material, " + defaultMaterial);
            return defaultMaterial;
        }

        return material;
    }

    private Sound getSoundFromConfig(String soundString, Sound defaultSound) {
        try {
            return Sound.valueOf(soundString);
        } catch (IllegalArgumentException exception) {
            Bukkit.getLogger().info("No such sound as " + soundString + "! Using default sound, " + defaultSound);
            return defaultSound;
        }
    }

    @Override
    public void setChangeIndicator(boolean change) {
        indicatorChange = change;
    }

    @Override
    public void setDelayPerSoup(double delay) {
        delayPerSoup = delay;
    }

    @Override
    public void setDelayBeforeRefillCooldown(double delay) {
        delayBeforeRefillCooldown = delay;
    }

    @Override
    public void setRefillCooldown(double delay) {
        refillCooldown = delay;
    }

    @Override
    public void setIndicatorX(int i) {
        indicatorX = i;
    }

    @Override
    public void setIndicatorY(int i) {
        indicatorY = i;
    }

    @Override
    public void setIndicatorZ(int i) {
        indicatorZ = i;
    }

    @Override
    public String translateCodes(String msg) {
        Matcher matcher = hexPattern.matcher(msg);
        while(matcher.find()) {
            String color = msg.substring(matcher.start(), matcher.end());
            msg = msg.replace(color, ChatColor.of(color) + "");
            matcher = hexPattern.matcher(msg);
        }
        return ChatColor.translateAlternateColorCodes('&', msg) + ChatColor.RESET;
    }

    @Override
    public void setCooldownMessage(String message) {
        cooldownMessage = message;
    }

    @Override
    public void setFullInventoryMessage(String message) {
        fullInventoryMessage = message;
    }

    @Override
    public void setIndicatorMaterial(Material material) {
        indicatorMaterial = material;
    }

    @Override
    public void setCooldownIndicatorMaterial(Material material) {
        cooldownIndicatorMaterial = material;
    }

    @Override
    public void setRefillSound(Sound sound) {
        refillSound = sound;
    }

    @Override
    public boolean canChangeIndicator() {
        return indicatorChange;
    }

    @Override
    public double getDelayPerSoup() {
        return delayPerSoup;
    }

    @Override
    public double getDelayBeforeRefillCooldown() {
        return delayBeforeRefillCooldown;
    }

    @Override
    public double getRefillCooldown() {
        return refillCooldown;
    }

    @Override
    public int getIndicatorX() {
        return indicatorX;
    }

    @Override
    public int getIndicatorY() {
        return indicatorY;
    }

    @Override
    public int getIndicatorZ() {
        return indicatorZ;
    }

    @Override
    public String getCooldownMessage() {
        return cooldownMessage;
    }

    @Override
    public String getFullInventoryMessage() {
        return fullInventoryMessage;
    }

    @Override
    public Material getIndicatorMaterial() {
        return indicatorMaterial;
    }

    @Override
    public Material getCooldownIndicatorMaterial() {
        return cooldownIndicatorMaterial;
    }

    @Override
    public Sound getRefillSound() {
        return refillSound;
    }
}
