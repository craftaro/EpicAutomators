package com.songoda.epicautomators.task;

import com.craftaro.third_party.com.cryptomorin.xseries.XSound;
import com.songoda.epicautomators.EpicAutomators;
import com.songoda.epicautomators.automator.Automator;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

public class AutomationTask extends BukkitRunnable {

    private final EpicAutomators plugin;
    private final int defaultTicks = 20;

    public AutomationTask(EpicAutomators plugin) {
        this.plugin = plugin;

        runTaskTimer(plugin, 0L, 5L);
    }

    @Override
    public void run() {
        for (Automator automator : plugin.getAutomatorManager().getAutomators().values()) {
            automator.setTicksSinceLastBlast(automator.getTicksSinceLastBlast() + 1);
            if (automator.getTicksSinceLastBlast() > defaultTicks / automator.getLevel().getSpeedMultiplier()) {
                if (!automator.isInLoadedChunk())
                    continue;

                Location location = automator.getLocation();

                if (automator.isRunning() && automator.fuel()) {
                    if (automator.getLocation().getBlock().isBlockPowered())
                        continue;
                    plugin.getProjectileTask().add(automator);
                    XSound.ENTITY_AXOLOTL_DEATH.play(location, 1.5F, 1.2F);
                } else if (automator.isRunning() && !automator.fuel()) {
                    automator.setRunning(false);
                    XSound.BLOCK_BEACON_DEACTIVATE.play(location, 1.5F, 1.2F);
                }
                automator.setTicksSinceLastBlast(0);
            }
        }
    }
}
