package com.cahrypt.me.soupsigns.manager;

import com.cahrypt.me.soupsigns.SoupSigns;
import com.cahrypt.me.soupsigns.data.DataManager;
import com.cahrypt.me.soupsigns.timer.Timer;
import com.cahrypt.me.soupsignsapi.manager.ISoupManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.block.data.type.WallSign;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class SoupManager implements ISoupManager {
    private final DataManager dataManager;

    private final Map<Player, Timer> intervalDelayMap;
    private final Map<Player, Timer> delayBeforeRefillCooldownMap;
    private final Map<Player, Timer> refillCooldownMap;

    public SoupManager() {
        this.dataManager = JavaPlugin.getPlugin(SoupSigns.class).getDataManager();

        this.intervalDelayMap = new HashMap<>();
        this.delayBeforeRefillCooldownMap = new HashMap<>();
        this.refillCooldownMap = new HashMap<>();
    }

    private Block getSupportingBlock(Sign sign) {
        Block signBlock = sign.getBlock();
        WallSign wallSign = (WallSign) sign.getBlockData();

        switch (wallSign.getFacing()) {
            case WEST -> {
                return signBlock.getRelative(BlockFace.EAST);
            }

            case EAST -> {
                return signBlock.getRelative(BlockFace.WEST);
            }

            case SOUTH -> {
                return signBlock.getRelative(BlockFace.NORTH);
            }

            case NORTH -> {
                return signBlock.getRelative(BlockFace.SOUTH);
            }

            default -> {
                return null;
            }
        }
    }

    private void handleIndicatorAction(Player player, Block supportingBlock, Material change, Map<Player, Timer> cooldownMap) {
        if (!dataManager.canChangeIndicator()) {
            return;
        }

        cooldownMap.remove(player);
        sendIndicatorChange(player, supportingBlock, change);
    }

    @Override
    public boolean hasIntervalDelay(Player player) {
        return intervalDelayMap.containsKey(player);
    }

    @Override
    public boolean hasRefillCooldownDelay(Player player) {
        return delayBeforeRefillCooldownMap.containsKey(player);
    }

    @Override
    public boolean hasRefillCooldown(Player player) {
        return refillCooldownMap.containsKey(player);
    }

    @Override
    public double getRefillCooldownTime(Player player) {
        return refillCooldownMap.get(player).getTime();
    }

    @Override
    public void addIntervalDelay(Player player) {
        intervalDelayMap.put(player, new Timer(player, dataManager.getDelayPerSoup(), intervalDelayMap::remove));
    }

    @Override
    public void addRefillCooldownDelay(Player player, Sign sign) {
        Block supportingBlock = getSupportingBlock(sign);

        delayBeforeRefillCooldownMap.put(player, new Timer(player, dataManager.getDelayBeforeRefillCooldown(), cooldownPlayer -> {
            handleIndicatorAction(cooldownPlayer, supportingBlock, dataManager.getCooldownIndicatorMaterial(), delayBeforeRefillCooldownMap);
            refillCooldownMap.put(cooldownPlayer, new Timer(cooldownPlayer, dataManager.getRefillCooldown(), cooldownElapsedPlayer -> handleIndicatorAction(cooldownElapsedPlayer, supportingBlock, dataManager.getIndicatorMaterial(), refillCooldownMap)));
        }));
    }

    @Override
    public void resetRefillCooldownDelay(Player player, Sign sign) {
        delayBeforeRefillCooldownMap.get(player).cancel();
        addRefillCooldownDelay(player, sign);
    }

    @Override
    public void sendIndicatorChange(Player player, Block supportingBlock, Material change) {
        Location indicatorBlockLocation = new Location(
                supportingBlock.getWorld(),
                supportingBlock.getX() + dataManager.getIndicatorX(),
                supportingBlock.getY() + dataManager.getIndicatorY(),
                supportingBlock.getZ() + dataManager.getIndicatorZ()
        );

        player.sendBlockChange(indicatorBlockLocation, change.createBlockData());
    }

}
