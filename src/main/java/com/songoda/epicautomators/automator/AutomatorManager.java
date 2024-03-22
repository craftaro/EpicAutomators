package com.songoda.epicautomators.automator;

import com.craftaro.core.data.LoadsData;
import com.craftaro.core.data.SQLSelect;
import com.craftaro.third_party.org.jooq.DSLContext;
import com.craftaro.third_party.org.jooq.impl.DSL;
import com.craftaro.third_party.org.jooq.impl.SQLDataType;
import com.songoda.epicautomators.EpicAutomators;
import com.songoda.epicautomators.utils.InventorySerializer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AutomatorManager implements LoadsData {

    private final Map<Location, Automator> automators = new HashMap<>();

    public void addAutomator(Automator automator) {
        automators.put(automator.getLocation(), automator);
    }

    public void removeAutomator(Location location) {
        automators.remove(location);
    }

    public Automator getAutomator(Location location) {
        return automators.get(location);
    }

    public Map<Location, Automator> getAutomators() {
        return Collections.unmodifiableMap(automators);
    }

    @Override
    public void loadDataImpl(DSLContext ctx) {
        SQLSelect.create(ctx).select("id", "world", "x", "y", "z", "owner", "direction", "level", "inventory",
                        "running", "is_gui_active", "needs_fuel_at", "laser_distance", "max_blocks", "auto_pickup",
                        "does_damage", "crops", "replant", "auto_smelt")
                .from("automator", result -> {
                    int id = result.get("id").asInt();
                    World world = Bukkit.getWorld(result.get("world").asString());
                    int x = result.get("x").asInt();
                    int y = result.get("y").asInt();
                    int z = result.get("z").asInt();
                    Location location = new Location(world, x, y, z);
                    System.out.println("uuid: " + result.get("owner").asString());
                    OfflinePlayer owner = Bukkit.getOfflinePlayer(UUID.fromString(result.get("owner").asString()));
                    BlockFace direction = BlockFace.valueOf(result.get("direction").asString());
                    int level = result.get("level").asInt();
                    ItemStack[] inventory = InventorySerializer.deserializeInventory(result.get("inventory").asBytes());
                    boolean running = result.get("running").asBoolean();
                    boolean isGuiActive = result.get("is_gui_active").asBoolean();
                    Instant needsFuelAt = result.get("needs_fuel_at").asInstant();
                    int laserDistance = result.get("laser_distance").asInt();
                    int maxBlocks = result.get("max_blocks").asInt();
                    boolean autoPickup = result.get("auto_pickup").asBoolean();
                    boolean doesDamage = result.get("does_damage").asBoolean();
                    boolean crops = result.get("crops").asBoolean();
                    boolean replant = result.get("replant").asBoolean();
                    boolean autoSmelt = result.get("auto_smelt").asBoolean();

                    Automator automator = new Automator(location, direction, owner);
                    automator.setId(id);
                    automator.setLevel(EpicAutomators.getInstance().getLevelManager().getLevel(level));
                    automator.setInventory(inventory);
                    automator.setRunning(running);
                    automator.setGuiActive(isGuiActive);
                    automator.setNeedsFuelAt(needsFuelAt);
                    automator.setLaserDistance(laserDistance);
                    automator.setMaxBlocks(maxBlocks);
                    automator.setAutoPickup(autoPickup);
                    automator.setDoesDamage(doesDamage);
                    automator.setCrops(crops);
                    automator.setReplant(replant);
                    automator.setAutoSmelt(autoSmelt);

                    addAutomator(automator);
                });
    }

    @Override
    public void setupTables(DSLContext ctx) {
        ctx.createTableIfNotExists("automator")
                .column("id", SQLDataType.INTEGER.nullable(false).identity(true))
                .column("world", SQLDataType.VARCHAR(36).nullable(false))
                .column("x", SQLDataType.INTEGER.nullable(false))
                .column("y", SQLDataType.INTEGER.nullable(false))
                .column("z", SQLDataType.INTEGER.nullable(false))
                .column("owner", SQLDataType.VARCHAR(36).nullable(false))
                .column("direction", SQLDataType.VARCHAR(10).nullable(false))
                .column("level", SQLDataType.INTEGER.nullable(false))
                .column("inventory", SQLDataType.BLOB.nullable(false))
                .column("running", SQLDataType.BOOLEAN.nullable(false))
                .column("is_gui_active", SQLDataType.BOOLEAN.nullable(false))
                .column("needs_fuel_at", SQLDataType.BIGINT.nullable(true))
                .column("laser_distance", SQLDataType.INTEGER.nullable(false))
                .column("max_blocks", SQLDataType.INTEGER.nullable(false))
                .column("auto_pickup", SQLDataType.BOOLEAN.nullable(false))
                .column("does_damage", SQLDataType.BOOLEAN.nullable(false))
                .column("crops", SQLDataType.BOOLEAN.nullable(false))
                .column("replant", SQLDataType.BOOLEAN.nullable(false))
                .column("auto_smelt", SQLDataType.BOOLEAN.nullable(false))
                .constraint(DSL.constraint().primaryKey("id"))
                .execute();
    }
}