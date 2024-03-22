package com.songoda.epicautomators.automator;

import com.craftaro.core.compatibility.ServerVersion;
import com.craftaro.core.data.SQLDelete;
import com.craftaro.core.data.SQLInsert;
import com.craftaro.core.data.SavesData;
import com.craftaro.core.hooks.EconomyManager;
import com.craftaro.third_party.com.cryptomorin.xseries.XMaterial;
import com.craftaro.third_party.com.cryptomorin.xseries.XSound;
import com.craftaro.third_party.org.jooq.DSLContext;
import com.songoda.epicautomators.EpicAutomators;
import com.songoda.epicautomators.automator.levels.Level;
import com.songoda.epicautomators.gui.OverviewGui;
import com.songoda.epicautomators.utils.CostType;
import com.songoda.epicautomators.utils.InventorySerializer;
import com.songoda.epicautomators.utils.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.sql.Timestamp;
import java.time.Instant;

public class Automator implements SavesData {

    private int id = -1;

    private final Location location;
    private final OfflinePlayer owner;
    private final BlockFace direction;
    private Level level = EpicAutomators.getInstance().getLevelManager().getLowestLevel();

    private ItemStack[] inventory = new ItemStack[27];
    private boolean running = false;
    private boolean isGuiActive = false;
    private Instant needsFuelAt = Instant.now();

    private int laserDistance = -1;
    private int maxBlocks = -1;
    private boolean autoPickup = true;
    private boolean doesDamage = false;
    private boolean crops = false;
    private boolean replant = true;
    private int ticksSinceLastBlast = 0;
    private boolean autoSmelt = false;

    private OverviewGui opened;
    private boolean wasUpdated = false;

    public Automator(Location location, BlockFace direction, OfflinePlayer owner) {
        this.location = location;
        this.direction = direction;
        this.owner = owner;

        if (laserDistance == -1)
            laserDistance = level.getMaxDistance();

        if (maxBlocks == -1)
            maxBlocks = level.getMaxBlocks();
    }

    public Location getLocation() {
        return location;
    }

    public BlockFace getDirection() {
        return direction;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public OverviewGui getOverviewGui(Player player) {
        return opened = new OverviewGui(this, player);
    }

    public synchronized void addItem(ItemStack next) {
        if (!hasRoomForItem(next)) {
            // Handle the case when there's no room for the item (e.g., drop it on the ground)
            return;
        }

        int maxStackSize = next.getType().getMaxStackSize();
        int remaining = next.getAmount();

        for (int i = 0; i < inventory.length; i++) {
            if (inventory[i] == null) {
                if (remaining <= maxStackSize) {
                    inventory[i] = next;
                    next.setAmount(remaining);
                    updateInventory();
                    return;
                } else {
                    ItemStack newStack = next.clone();
                    newStack.setAmount(maxStackSize);
                    inventory[i] = newStack;
                    remaining -= maxStackSize;
                }
            } else if (inventory[i].isSimilar(next)) {
                int amount = inventory[i].getAmount();
                int newAmount = amount + remaining;

                if (newAmount <= maxStackSize) {
                    inventory[i].setAmount(newAmount);
                    updateInventory();
                    return;
                } else {
                    inventory[i].setAmount(maxStackSize);
                    remaining = newAmount - maxStackSize;
                }
            }
        }

        save("inventory");
        updateInventory();
    }

    public boolean hasRoomForItem(ItemStack item) {
        int maxStackSize = item.getType().getMaxStackSize();
        int remaining = item.getAmount();

        for (ItemStack invItem : inventory) {
            if (invItem == null) {
                remaining -= maxStackSize;
            } else if (invItem.isSimilar(item)) {
                int amount = invItem.getAmount();
                int capacity = maxStackSize - amount;
                remaining -= capacity;
            }

            if (remaining <= 0) {
                return true;
            }
        }

        return false;
    }

    public boolean isGuiActive() {
        return isGuiActive;
    }

    public void setGuiActive(boolean guiActive) {
        isGuiActive = guiActive;
    }

    public boolean isFueled() {
        for (ItemStack item : inventory) {
            if (item != null && ItemUtils.isFuel(item)) {
                return true;
            }
        }
        return false;
    }

    public ItemStack getFirstFuel() {
        for (ItemStack item : inventory)
            if (item != null && ItemUtils.isFuel(item))
                return item;
        return null;
    }

    public ItemStack getFirstItem() {
        for (ItemStack item : inventory)
            if (item != null && !ItemUtils.isFuel(item))
                return item;
        return null;
    }

    public boolean fuel() {
        ItemStack fuel = getFirstFuel();
        // Check if the automator was recently fueled
        if (needsFuelAt != null && needsFuelAt.isAfter(Instant.now()))
            return true;

        if (!isFueled())
            return false;

        needsFuelAt = Instant.now().plusSeconds(ItemUtils.getBurnTime(XMaterial.matchXMaterial(fuel)));
        fuel.setAmount(fuel.getAmount() - 1);

        save("needs_fuel_at", "inventory");

        if (fuel.getAmount() == 0) {
            updateInventory();
            return false;
        }
        updateInventory();

        return true;
    }

    public boolean isInLoadedChunk() {
        return location != null && location.getWorld() != null && location.getWorld().isChunkLoaded(((int) location.getX()) >> 4, ((int) location.getZ()) >> 4);
    }

    public int getLaserDistance() {
        return laserDistance;
    }

    public void setLaserDistance(int laserDistance) {
        this.laserDistance = laserDistance;
    }

    public int getMaxBlocks() {
        return maxBlocks;
    }

    public void setMaxBlocks(int maxBlocks) {
        this.maxBlocks = maxBlocks;
    }

    public boolean isAutoPickup() {
        return autoPickup;
    }

    public void setAutoPickup(boolean autoPickup) {
        this.autoPickup = autoPickup;
    }

    public boolean doesDamage() {
        return doesDamage;
    }

    public void setDoesDamage(boolean doesDamage) {
        this.doesDamage = doesDamage;
    }

    public OfflinePlayer getOwner() {
        return owner;
    }

    public boolean isOwnerLoaded() {
        return owner.isOnline();
    }

    public boolean isAutoHarvest() {
        return crops;
    }

    public void setCrops(boolean crops) {
        this.crops = crops;
    }

    public void upgrade(Player player, CostType type) {
        EpicAutomators plugin = EpicAutomators.getInstance();
        if (!plugin.getLevelManager().getLevels().containsKey(this.level.getLevel() + 1)) {
            return;
        }

        Level level = plugin.getLevelManager().getLevel(this.level.getLevel() + 1);
        int cost = type == CostType.ECONOMY ? level.getCostEconomy() : level.getCostExperience();

        if (type == CostType.ECONOMY) {
            if (!EconomyManager.isEnabled()) {
                player.sendMessage("Economy not enabled.");
                return;
            }
            if (!EconomyManager.hasBalance(player, cost)) {
                plugin.getLocale().getMessage("event.upgrade.cannotafford").sendPrefixedMessage(player);
                return;
            }
            EconomyManager.withdrawBalance(player, cost);
            upgradeFinal(player);
        } else if (type == CostType.EXPERIENCE) {
            if (player.getLevel() >= cost || player.getGameMode() == GameMode.CREATIVE) {
                if (player.getGameMode() != GameMode.CREATIVE) {
                    player.setLevel(player.getLevel() - cost);
                }
                upgradeFinal(player);
            } else {
                plugin.getLocale().getMessage("event.upgrade.cannotafford").sendPrefixedMessage(player);
            }
        }
    }

    private void upgradeFinal(Player player) {
        EpicAutomators plugin = EpicAutomators.getInstance();
        levelUp();
        save("level");
        if (plugin.getLevelManager().getHighestLevel() != this.level) {
            plugin.getLocale().getMessage("event.upgrade.success")
                    .processPlaceholder("level", this.level.getLevel()).sendPrefixedMessage(player);

        } else {
            plugin.getLocale().getMessage("event.upgrade.maxed")
                    .processPlaceholder("level", this.level.getLevel()).sendPrefixedMessage(player);
        }
        Location loc = this.location.clone().add(.5, .5, .5);

        if (!ServerVersion.isServerVersionAtLeast(ServerVersion.V1_12)) {
            return;
        }

        player.getWorld().spawnParticle(org.bukkit.Particle.valueOf(plugin.getConfig().getString("Main.Upgrade Particle Type")), loc, 200, .5, .5, .5);

        if (plugin.getLevelManager().getHighestLevel() != this.level) {
            XSound.ENTITY_PLAYER_LEVELUP.play(player, .6f, 15);
        } else {
            XSound.ENTITY_PLAYER_LEVELUP.play(player, 2, 25);

            if (!ServerVersion.isServerVersionAtLeast(ServerVersion.V1_13)) {
                return;
            }

            XSound.BLOCK_NOTE_BLOCK_CHIME.play(player, 2, 25);
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> XSound.BLOCK_NOTE_BLOCK_CHIME.play(player, 1.2f, 35), 5);
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> XSound.BLOCK_NOTE_BLOCK_CHIME.play(player, 1.8f, 35), 10);
        }
    }

    public void levelUp() {
        EpicAutomators plugin = EpicAutomators.getInstance();
        this.level = plugin.getLevelManager().getLevel(this.level.getLevel() + 1);
    }

    public Level getLevel() {
        return level;
    }

    public int getTicksSinceLastBlast() {
        return ticksSinceLastBlast;
    }

    public void setTicksSinceLastBlast(int ticksSinceLastBlast) {
        this.ticksSinceLastBlast = ticksSinceLastBlast;
    }

    public void updateInventory() {
        wasUpdated = true;
        if (opened != null)
            opened.updateInventory();
    }

    public boolean wasUpdated() {
        return wasUpdated;
    }

    public void setWasUpdated(boolean wasUpdated) {
        this.wasUpdated = wasUpdated;
    }

    public ItemStack[] getInventory() {
        return inventory;
    }

    public void destroy() {
        for (ItemStack item : inventory)
            if (item != null)
                location.getWorld().dropItemNaturally(location, item);
        location.getWorld().dropItemNaturally(location, EpicAutomators.getInstance().createLeveledAutomator(level.getLevel()));
        delete();
    }

    public void removeItem(ItemStack itemToRemove, int amountToRemove) {
        for (int i = 0; i < inventory.length; i++) {
            ItemStack item = inventory[i];
            if (item != null && item.isSimilar(itemToRemove)) {
                if (item.getAmount() > amountToRemove) {
                    item.setAmount(item.getAmount() - amountToRemove);
                    updateInventory();
                    return;
                } else {
                    amountToRemove -= item.getAmount();
                    inventory[i] = null;
                    if (amountToRemove == 0) {
                        updateInventory();
                        return;
                    }
                }
            }
        }
        save("inventory");
        updateInventory();
    }

    public boolean isReplant() {
        return replant;
    }

    public void setReplant(boolean replant) {
        this.replant = replant;
    }

    public boolean isAutoSmelt() {
        return autoSmelt;
    }

    public void setAutoSmelt(boolean autoSmelt) {
        this.autoSmelt = autoSmelt;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setInventory(ItemStack[] inventory) {
        this.inventory = inventory;
    }

    public void setNeedsFuelAt(Instant needsFuelAt) {
        this.needsFuelAt = needsFuelAt;
    }

    @Override
    public void saveImpl(DSLContext ctx, String... columns) {
        SQLInsert.create(ctx).insertInto("automator")
                .withField("id", id, id == -1)
                .withField("world", location.getWorld().getName())
                .withField("x", location.getBlockX())
                .withField("y", location.getBlockY())
                .withField("z", location.getBlockZ())
                .withField("owner", owner.getUniqueId().toString())
                .withField("direction", direction.name())
                .withField("level", level.getLevel())
                .withField("inventory", InventorySerializer.serializeInventory(inventory))
                .withField("running", running)
                .withField("is_gui_active", isGuiActive)
                .withField("needs_fuel_at", needsFuelAt == null ? null : needsFuelAt.toEpochMilli())
                .withField("laser_distance", laserDistance)
                .withField("max_blocks", maxBlocks)
                .withField("auto_pickup", autoPickup)
                .withField("does_damage", doesDamage)
                .withField("crops", crops)
                .withField("replant", replant)
                .withField("auto_smelt", autoSmelt)
                .onDuplicateKeyUpdate(columns)
                .execute();

        if (id == -1)
            this.id = lastInsertedId("automator", ctx);
    }

    @Override
    public void deleteImpl(DSLContext ctx) {
        SQLDelete.create(ctx).delete("automator", "id", id);
    }
}
