package com.cahrypt.me.soupsigns.timer;

import com.cahrypt.me.soupsigns.SoupSigns;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.function.Consumer;

public class Timer {
    private double time;
    private final BukkitTask timerTask;

    public Timer(Player player, double timerSeconds, Consumer<Player> onFinishConsumer) {
        this.time = timerSeconds;
        this.timerTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (time <= 0) {
                    onFinishConsumer.accept(player);
                    cancel();
                    return;
                }

                time -= 0.1;
            }
        }.runTaskTimer(JavaPlugin.getPlugin(SoupSigns.class), 0, 2);
    }

    public boolean isCancelled() {
        return timerTask.isCancelled();
    }

    public double getTime() {
        return time;
    }

    public void cancel() {
        timerTask.cancel();
    }
}
