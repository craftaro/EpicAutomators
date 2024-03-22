package com.songoda.epicautomators.task.projectile;

import com.songoda.epicautomators.EpicAutomators;
import com.songoda.epicautomators.automator.Automator;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;

public class ProjectileTask extends BukkitRunnable {
    private final EpicAutomators plugin;
    private final Set<Projectile> projectiles = new HashSet<>();

    public ProjectileTask(EpicAutomators plugin) {
        this.plugin = plugin;

        runTaskTimer(plugin, 0L, 1L);
    }

    public void add(Automator automator) {
        Projectile projectile = new Projectile(automator);
        projectiles.add(projectile);
    }

    @Override
    public void run() {
        Set<Projectile> toRemove = new HashSet<>();
        for (Projectile projectile : projectiles) {
            if (!projectile.isActive())
                toRemove.add(projectile);
            else
                projectile.tick();
        }
        projectiles.removeAll(toRemove);
    }
}