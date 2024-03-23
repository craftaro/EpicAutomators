package com.songoda.epicautomators.task.projectile;

import com.craftaro.core.compatibility.ServerVersion;
import com.craftaro.third_party.com.cryptomorin.xseries.particles.ParticleDisplay;
import com.songoda.epicautomators.EpicAutomators;
import com.songoda.epicautomators.automator.Automator;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

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
            if (ServerVersion.isServerVersionAtLeast(ServerVersion.V1_12))
                ParticleDisplay.colored(particleLocation, automator.getLevel().getColor(), 2).spawn();
            else
                particleLocation.getWorld().spawnParticle(Particle.FLAME, particleLocation, 1, 0, 0, 0, 0);

        }
    }
}