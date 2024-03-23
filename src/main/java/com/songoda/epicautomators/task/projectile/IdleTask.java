package com.songoda.epicautomators.task.projectile;

import com.craftaro.core.compatibility.CompatibleParticleHandler;
import com.songoda.epicautomators.EpicAutomators;
import com.songoda.epicautomators.automator.Automator;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

import java.awt.*;

public class IdleTask extends BukkitRunnable {

    private final EpicAutomators plugin;

    public IdleTask(EpicAutomators plugin) {
        this.plugin = plugin;
        runTaskTimer(plugin, 0L, 20L);
    }

    @Override
    public void run() {
        for (Automator automator : plugin.getAutomatorManager().getAutomators().values()) {
            if (automator.isRunning())
                continue;

            Block block = automator.getLocation().getBlock().getRelative(automator.getDirection());
            Location particleLocation = block.getLocation().add(0.5, 0.5, 0.5);

            // Spawn 2 particles in the center of the block in front of the automator
            Color color = automator.getLevel().getColor();
            int red = color.getRed();
            int green = color.getGreen();
            int blue = color.getBlue();
            CompatibleParticleHandler.redstoneParticles(particleLocation, red, green, blue, 1, 2, 0);

        }
    }
}