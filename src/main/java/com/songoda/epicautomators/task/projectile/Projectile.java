package com.songoda.epicautomators.task.projectile;

import com.craftaro.core.compatibility.CompatibleParticleHandler;
import com.craftaro.core.compatibility.crops.CompatibleCrop;
import com.craftaro.core.hooks.ProtectionManager;
import com.craftaro.core.nms.Nms;
import com.craftaro.third_party.com.cryptomorin.xseries.XSound;
import com.songoda.epicautomators.EpicAutomators;
import com.songoda.epicautomators.automator.Automator;
import com.songoda.epicautomators.settings.Settings;
import com.songoda.epicautomators.utils.ItemUtils;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Rotatable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import java.awt.Color;
import java.util.Collection;

public class Projectile {
    private final Automator automator;
    private Location location;
    private Vector direction;
    private final double stepSize;
    private int blocksTravelled = 0;
    private boolean active = true;
    private int blocksDestroyed = 0;
    private Collection<Entity> nearbyEntities;

    public Projectile(Automator automator) {
        this.automator = automator;
        this.location = automator.getLocation().clone().add(0.5, 0.5, 0.5);
        this.direction = convertBlockFaceToVector(automator.getDirection()).normalize().multiply(0.1);
        this.stepSize = 0.1;
        this.nearbyEntities = location.getWorld().getNearbyEntities(location, 100, 100, 100);
    }

    public void tick() {
        Color color = automator.getLevel().getColor();
        int red = color.getRed();
        int green = color.getGreen();
        int blue = color.getBlue();
        CompatibleParticleHandler.redstoneParticles(location, red, green, blue, 1, 2, 0);

        for (int i = 0; i < 5; i++) {
            Location previousLocation = location.clone();
            location.add(direction);

            if (blocksTravelled > automator.getLaserDistance()) {
                active = false;
                break;
            }

            if (automator.doesDamage())
                checkEntityCollisions();

            Block block = location.getBlock();
            Block previousBlock = previousLocation.getBlock();

            if (block.equals(previousBlock))
                continue;

            if (block.equals(automator.getLocation().getBlock())) {
                active = false;
                break;
            }

            boolean isNotCrop = !isCrop(block) || isCropFullyGrown(block);

            blocksTravelled++;

            if (blocksTravelled % 5 == 0)
                nearbyEntities = location.getWorld().getNearbyEntities(location, 100, 100, 100);

            if (block.getState() instanceof Sign) {
                // The projectile has hit a sign, calculate the bounce direction
                direction = calculateBounceDirection(block);
            } else if (block.getType().name().contains("BUTTON")) {
                // Play a sound effect when the button is pressed
                XSound.BLOCK_STONE_BUTTON_CLICK_ON.play(location, 1.0F, 1.0F);

                // Create a particle effect at the button's location
                Location particleLocation = block.getLocation().add(0.5, 0.5, 0.5);
                CompatibleParticleHandler.spawnParticles(CompatibleParticleHandler.ParticleType.CRIT, particleLocation, 10, 0.3, 0.3, 0.3, 0.1);
                Nms.getImplementations().getWorld().pressButton(block);

                active = false;
            } else if (block.getType() == Material.LEVER) {
                // Toggle the lever
                Nms.getImplementations().getWorld().toggleLever(block);

                // Play a sound effect when the lever is toggled
                XSound.BLOCK_LEVER_CLICK.play(location, 1.0F, 1.0F);

                // Create a particle effect at the lever's location
                Location particleLocation = block.getLocation().add(0.5, 0.5, 0.5);
                CompatibleParticleHandler.spawnParticles(CompatibleParticleHandler.ParticleType.CRIT, particleLocation, 10, 0.3, 0.3, 0.3, 0.1);

                active = false;
            } else if (!block.getType().isAir() && isNotCrop) {
                if (blocksDestroyed < automator.getMaxBlocks()) {
                    blocksDestroyed++;
                } else {
                    active = false;
                    break;
                }
                breakBlock(automator, location);
            }
        }
    }

    private Vector convertBlockFaceToVector(BlockFace blockFace) {
        return new Vector(blockFace.getModX(), blockFace.getModY(), blockFace.getModZ());
    }

    private Vector calculateBounceDirection(Block block) {
        BlockData blockData = block.getBlockData();

        if (blockData instanceof Rotatable) {
            Rotatable rotatable = (Rotatable) blockData;
            BlockFace rotation = rotatable.getRotation();

            if (block.getState() instanceof Sign) {
                Sign sign = (Sign) block.getState();
                String[] lines = sign.getLines();

                boolean moveUp = false;
                boolean moveDown = false;

                for (String line : lines) {
                    if (line.contains("^") && Settings.ALLOW_UP.getBoolean()) {
                        moveUp = true;
                    } else if (line.contains("v") && Settings.ALLOW_DOWN.getBoolean()) {
                        moveDown = true;
                    } else if (line.contains("x") && Settings.ALLOW_STOP.getBoolean()) {
                        active = false;
                        break;
                    }
                }

                if (moveUp && !moveDown) {
                    // Move the projectile up one block level
                    location.add(0, 1, 0);
                } else if (!moveUp && moveDown) {
                    // Move the projectile down one block level
                    location.subtract(0, 1, 0);
                }
            }

            // Calculate the reflection vector based on the sign's rotation and the incoming direction
            Vector reflection = getReflectionFromRotationAndDirection(rotation, direction);

            // Calculate the bounce direction by reflecting the current direction across the reflection vector
            Vector bounceDirection = direction.clone().subtract(reflection.multiply(2 * direction.dot(reflection))).normalize().multiply(0.1);

            return bounceDirection;
        }

        return direction;
    }

    private Vector getReflectionFromRotationAndDirection(BlockFace rotation, Vector direction) {
        // Normalize the direction vector
        Vector normalizedDirection = direction.clone().normalize();

        // Calculate the dot product between the direction vector and the rotation's normal vector
        double dotProduct = normalizedDirection.dot(rotation.getDirection());

        // If the dot product is positive, the projectile is hitting the front face of the sign
        if (dotProduct > 0) {
            // Return the normal vector of the sign's rotation
            return rotation.getDirection();
        } else {
            // Otherwise, the projectile is hitting the back face of the sign
            // Return the negated normal vector of the sign's rotation
            return rotation.getDirection().multiply(-1);
        }
    }

    private void checkEntityCollisions() {
        for (Entity entity : nearbyEntities) {
            if (entity instanceof LivingEntity && entity.getLocation().distanceSquared(location) <= 1) {
                LivingEntity livingEntity = (LivingEntity) entity;
                if (livingEntity.getNoDamageTicks() == 0) {
                    livingEntity.setFireTicks(40); // Set the entity on fire for 2 seconds (40 ticks)

                    double damage = automator.getLevel().getDamage();
                    livingEntity.damage(damage);

                    // Play a fire-themed sound effect when the entity is hit
                    XSound.BLOCK_FIRE_AMBIENT.play(location, 1.0F, 1.0F);
                    XSound.ENTITY_GENERIC_HURT.play(location, 1.0F, 1.0F);

                    // Create a particle effect at the entity's location
                    Location particleLocation = livingEntity.getLocation().add(0, livingEntity.getHeight() / 2, 0);
                    CompatibleParticleHandler.spawnParticles(CompatibleParticleHandler.ParticleType.FLAME, particleLocation, 10, 0.3, 0.3, 0.3, 0.1);

                    // Set metadata on the entity to track the automator responsible for the kill
                    livingEntity.setMetadata("KilledByAutomator", new FixedMetadataValue(EpicAutomators.getInstance(), automator));
                }
            }
        }
    }

    private boolean breakBlock(Automator automator, Location location) {
        Block blockToBreak = location.getBlock();

        if (blockToBreak.getType().isAir())
            return false;

        OfflinePlayer player = automator.getOwner(); // Get the player associated with the automator

        if (automator.isOwnerLoaded() && !ProtectionManager.canBreak(player.getPlayer(), location) || Settings.MATERIAL_BLACKLIST.getStringList()
                .contains(blockToBreak.getType().name())) {
            // If the event is cancelled, don't break the block
            active = false;
            return false;
        }

        if (EpicAutomators.getInstance().getAutomatorManager().getAutomator(blockToBreak.getLocation()) != null) {
            // If the block is an automator, don't break it
            active = false;
            return false;
        }

        if (automator.isAutoSmelt()) {
            ItemStack smeltedItem = ItemUtils.getSmeltedItem(blockToBreak.getType());
            if (smeltedItem != null) {
                if (automator.isAutoPickup()) {
                    if (!automator.hasRoomForItem(smeltedItem)) {
                        active = false;
                        automator.setRunning(false);
                        return false;
                    }
                    automator.addItem(smeltedItem);
                    blockToBreak.setType(Material.AIR);
                } else {
                    blockToBreak.setType(Material.AIR);
                    location.getWorld().dropItemNaturally(location, smeltedItem);
                }
                return true;
            }
        }

        if (isCrop(blockToBreak)) {
            if (!automator.isAutoHarvest())
                return false;
            if (isCropFullyGrown(blockToBreak)) {
                // Harvest the ripe crop
                Collection<ItemStack> drops = blockToBreak.getDrops();

                for (ItemStack drop : drops) {
                    if (!automator.hasRoomForItem(drop)) {
                        active = false;
                        return false;
                    }
                }

                // Play the block break sound with a higher pitch and volume
                XSound.BLOCK_CROP_BREAK.play(location, 1.5F, 1.2F);

                // Play a zap sound effect
                XSound.ENTITY_ENDERMAN_TELEPORT.play(location, 0.8F, 1.5F);

                if (automator.isAutoPickup()) {
                    drops.forEach(automator::addItem);
                    if (automator.isReplant()) {
                        CompatibleCrop.resetCropAge(blockToBreak);

                        // Create a particle effect at the block location
                        Location particleLocation = blockToBreak.getLocation().add(0.5, 0.5, 0.5);
                        location.getWorld().playEffect(particleLocation, Effect.VILLAGER_PLANT_GROW, 0);
                    } else {
                        blockToBreak.setType(Material.AIR);
                    }
                } else {
                    CompatibleCrop.resetCropAge(blockToBreak);
                    for (ItemStack itemToDrop : drops) {
                        blockToBreak.getWorld().dropItemNaturally(blockToBreak.getLocation(), itemToDrop);
                    }
                }
                return true;
            } else {
                // Ignore the crop if it's not fully grown
                return false;
            }
        } else {
            // Break the block normally
            Collection<ItemStack> drops = blockToBreak.getDrops();

            // Check if the block is an inventory container
            if (blockToBreak.getState() instanceof InventoryHolder) {
                InventoryHolder inventoryHolder = (InventoryHolder) blockToBreak.getState();
                Inventory inventory = inventoryHolder.getInventory();

                // Drop the contents of the inventory onto the ground
                for (ItemStack item : inventory.getContents())
                    if (item != null)
                        location.getWorld().dropItemNaturally(location, item);
            }

            for (ItemStack drop : drops) {
                if (!automator.hasRoomForItem(drop)) {
                    active = false;
                    automator.setRunning(false);
                    return false;
                }
            }

            // Play the block break sound with a higher pitch and volume
            XSound.BLOCK_STONE_BREAK.play(location, 1.5F, 1.2F);

            // Play a zap sound effect
            XSound.ENTITY_ENDERMAN_TELEPORT.play(location, 0.8F, 1.5F);

            if (automator.isAutoPickup()) {
                if (!drops.isEmpty())
                    automator.addItem(drops.iterator().next());
                blockToBreak.setType(Material.AIR);

                // Create a particle effect at the block location
                Location particleLocation = blockToBreak.getLocation().add(0.5, 0.5, 0.5);
                location.getWorld().playEffect(particleLocation, Effect.ENDER_SIGNAL, 0);
            } else {
                blockToBreak.breakNaturally();
            }
            return true;
        }
    }

    private boolean isCrop(Block block) {
        return CompatibleCrop.isCrop(block);
    }

    private boolean isCropFullyGrown(Block block) {
        return CompatibleCrop.isCropFullyGrown(block);
    }

    public boolean isActive() {
        return active;
    }
}
