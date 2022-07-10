package com.cahrypt.me.soupsigns.listener;

import com.cahrypt.me.soupsigns.SoupSigns;
import com.cahrypt.me.soupsigns.data.DataManager;
import com.cahrypt.me.soupsigns.manager.SoupManager;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.WallSign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class RefillListener extends AListener {
    private final DataManager dataManager;
    private final SoupManager soupManager;

    public RefillListener() {
        super();

        SoupSigns main = JavaPlugin.getPlugin(SoupSigns.class);
        this.dataManager = main.getDataManager();
        this.soupManager = main.getSoupManager();
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();

        if (block == null) {
            return;
        }

        BlockData state = block.getBlockData();

        if (!(state instanceof WallSign)) {
            return;
        }

        Sign sign = (Sign) block.getState();

        if (!isSoupSign(sign)) {
            return;
        }

        Player player = event.getPlayer();
        Inventory playerInventory = player.getInventory();

        if (soupManager.hasRefillCooldown(player)) {
            player.sendMessage(dataManager.getCooldownMessage().formatted(Math.round(soupManager.getRefillCooldownTime(player))));
            return;
        }

        if (soupManager.hasIntervalDelay(player)) {
            return;
        }

        if (playerInventory.firstEmpty() == -1) {
            player.sendMessage(dataManager.getFullInventoryMessage());
            return;
        }

        if (soupManager.hasRefillCooldownDelay(player)) {
            soupManager.resetRefillCooldownDelay(player, sign);
        } else {
            soupManager.addRefillCooldownDelay(player, sign);
        }

        soupManager.addIntervalDelay(player);
        playerInventory.addItem(new ItemStack(Material.MUSHROOM_STEW));
        player.playSound(player, dataManager.getRefillSound(), 1, 1);
    }
}
