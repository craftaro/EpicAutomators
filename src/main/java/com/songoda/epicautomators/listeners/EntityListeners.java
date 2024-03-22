package com.songoda.epicautomators.listeners;

import com.songoda.epicautomators.automator.Automator;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;

public class EntityListeners implements Listener {

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();

        // Check if the entity has the "KilledByAutomator" metadata
        if (entity.hasMetadata("KilledByAutomator")) {
            // Get the automator responsible for the kill from the metadata
            Automator automator = (Automator) entity.getMetadata("KilledByAutomator").get(0).value();

            // Collect the entity's drops and add them to the automator's storage
            Collection<ItemStack> drops = event.getDrops();
            if (!drops.isEmpty()) {
                for (ItemStack drop : drops) {
                    automator.addItem(drop);
                }
                // Clear the drops from the event to prevent them from dropping on the ground
                event.getDrops().clear();
            }
        }
    }

}
