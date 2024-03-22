package com.songoda.epicautomators.task;

import com.songoda.epicautomators.EpicAutomators;
import com.songoda.epicautomators.automator.Automator;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.BlockFace;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.Random;

public class IdleTask extends BukkitRunnable {
    private final EpicAutomators plugin;
    private final Random random = new Random();

    public IdleTask(EpicAutomators plugin) {
        this.plugin = plugin;
        runTaskTimer(plugin, 0L, 8L);
    }

    @Override
    public void run() {
        if (random.nextInt(100) < 30)
            return;

        for (Automator automator : plugin.getAutomatorManager().getAutomators().values()) {
            if (!automator.isInLoadedChunk())
                continue;

            if (!automator.isRunning())
                    automator.blast();
        }
    }
}