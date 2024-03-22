package com.songoda.epicautomators.task;

import com.craftaro.third_party.com.cryptomorin.xseries.XMaterial;
import com.songoda.epicautomators.EpicAutomators;
import com.songoda.epicautomators.automator.Automator;
import com.songoda.epicautomators.automator.AutomatorManager;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Hopper;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class HopperTask extends BukkitRunnable {
    private final AutomatorManager manager;

    public HopperTask(EpicAutomators plugin) {
        this.manager = plugin.getAutomatorManager();

        runTaskTimer(plugin, 0, 8);
    }

    @Override
    public void run() {
        for (Automator automator : this.manager.getAutomators().values()) {
            if (!automator.isInLoadedChunk())
                continue;

            Block block = automator.getLocation().getBlock().getRelative(BlockFace.DOWN);

            if (block.getType() != Material.HOPPER)
                continue;

            Inventory hopperInventory = ((Hopper) block.getState()).getInventory();

            ItemStack item = automator.getFirstItem();
            if (item == null || item.getType() == XMaterial.BONE_MEAL.parseMaterial()) continue;

            ItemStack toMove = item.clone();
            toMove.setAmount(1);

            if (canHop(hopperInventory, toMove)) {
                automator.removeItem(item, 1);
                automator.updateInventory();
                hopperInventory.addItem(toMove);
            }
        }
    }

    private boolean canHop(Inventory inventory, ItemStack item) {
        if (inventory.firstEmpty() != -1) {
            return true;
        }

        for (ItemStack it : inventory.getContents()) {
            if (it == null || it.isSimilar(item) && (it.getAmount() + item.getAmount()) <= it.getMaxStackSize()) {
                return true;
            }
        }

        return false;
    }
}
