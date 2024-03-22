package com.songoda.epicautomators.listeners;

import com.craftaro.core.compatibility.CompatibleHand;
import com.songoda.epicautomators.EpicAutomators;
import com.songoda.epicautomators.automator.Automator;
import com.songoda.epicautomators.settings.Settings;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class BlockListeners implements Listener {
    private final EpicAutomators plugin;

    public BlockListeners(EpicAutomators plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.getBlock().getType() != Settings.AUTOMATOR_ITEM.getMaterial().parseMaterial())
            return;

        int level = plugin.getAutomatorLevel(event.getItemInHand());

        if (level == -1)
            return;

        Player player = event.getPlayer();
        BlockFace direction = getDirectionFacing(player);
        Automator automator = new Automator(event.getBlock().getLocation(), direction, player);
        automator.setLevel(plugin.getLevelManager().getLevel(level));
        plugin.getAutomatorManager().addAutomator(automator);
        automator.save();

        // Create armor stands for the laser effect
        createLaserEffect(event.getBlock().getLocation(), direction);
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onInteract(PlayerInteractEvent event) {
        CompatibleHand hand = CompatibleHand.getHand(event.getHand());
        if (hand != CompatibleHand.MAIN_HAND || event.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;

        Block block = event.getClickedBlock();
        Player player = event.getPlayer();

        Automator automator = plugin.getAutomatorManager().getAutomator(block.getLocation());

        if (automator == null)
            return;

        event.setCancelled(true);

        if (automator.isGuiActive()) {
            player.sendMessage("Automator is already active.");
            return;
        }

        plugin.getGuiManager().showGUI(player, automator.getOverviewGui(player));
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        Automator automator = plugin.getAutomatorManager().getAutomator(block.getLocation());
        if (automator == null)
            return;

        automator.destroy();

        plugin.getAutomatorManager().removeAutomator(automator.getLocation());
    }

    public BlockFace getDirectionFacing(Player player) {
        float yaw = player.getLocation().getYaw();
        if (yaw < 0) {
            yaw += 360;
        }
        if (yaw >= 315 || yaw < 45) {
            return BlockFace.SOUTH;
        } else if (yaw < 135) {
            return BlockFace.WEST;
        } else if (yaw < 225) {
            return BlockFace.NORTH;
        } else if (yaw < 315) {
            return BlockFace.EAST;
        }
        return BlockFace.SOUTH; // Default direction if none match
    }

    private void createLaserEffect(Location location, BlockFace direction) {
        // Calculate the center location of the block
        Location centerLocation = location.clone().add(0.6, 0.6, 0.6);

        centerLocation.getBlock().setType(Settings.AUTOMATOR_ITEM.getMaterial().parseMaterial());
    }
}