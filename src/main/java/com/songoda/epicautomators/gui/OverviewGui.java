package com.songoda.epicautomators.gui;

import com.craftaro.core.gui.CustomizableGui;
import com.craftaro.core.gui.GuiUtils;
import com.craftaro.core.utils.NumberUtils;
import com.craftaro.third_party.com.cryptomorin.xseries.XMaterial;
import com.songoda.epicautomators.EpicAutomators;
import com.songoda.epicautomators.automator.Automator;
import com.songoda.epicautomators.automator.levels.Level;
import com.songoda.epicautomators.settings.Settings;
import com.songoda.epicautomators.utils.CostType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class OverviewGui extends CustomizableGui {
    private final EpicAutomators plugin;
    private final Automator automator;
    private final Player player;

    private int task;
    static int[][] infoIconOrder = new int[][]{{22}, {21, 23}, {21, 22, 23}, {12, 21, 22, 23}, {12, 14, 21, 22, 23}, {3, 12, 14, 21, 22, 23}, {3, 5, 12, 14, 21, 22, 23}};

    public OverviewGui(Automator automator, Player player) {
        super(EpicAutomators.getPlugin(EpicAutomators.class), "main");
        this.plugin = EpicAutomators.getPlugin(EpicAutomators.class);

        this.automator = automator;
        this.player = player;

        showPage();
        setRows(6);
        setUnlockedRange(3, 0, 5, 8);

        setOnOpen((event) -> {
            updateInventory();
            automator.setGuiActive(true);
        });
        setDefaultAction((event) ->
                Bukkit.getScheduler().runTaskLater(this.plugin, this::updateAutomator, 0L));
        runTask();
        setOnClose((event) -> {
            updateAutomator();
            automator.setGuiActive(false);
            Bukkit.getScheduler().cancelTask(this.task);
        });
        setDefaultItem(null);

        setTitle(plugin.formatName(automator.getLevel().getLevel()));
        setAcceptsItems(true);
    }

    private void showPage() {
        ItemStack glass1 = GuiUtils.getBorderItem(Settings.GLASS_TYPE_1.getMaterial());
        ItemStack glass2 = GuiUtils.getBorderItem(Settings.GLASS_TYPE_2.getMaterial());
        ItemStack glass3 = GuiUtils.getBorderItem(Settings.GLASS_TYPE_3.getMaterial());


        mirrorFill("mirrorfill_1", 0, 0, false, true, glass2);
        mirrorFill("mirrorfill_2", 0, 1, false, true, glass2);
        mirrorFill("mirrorfill_3", 0, 2, false, true, glass3);
        mirrorFill("mirrorfill_4", 1, 0, false, true, glass2);
        mirrorFill("mirrorfill_5", 1, 1, false, true, glass3);
        mirrorFill("mirrorfill_6", 2, 0, false, true, glass2);
        mirrorFill("mirrorfill_7", 2, 1, false, true, glass2);
        mirrorFill("mirrorfill_8", 2, 2, false, true, glass3);

        mirrorFill("mirrorfill_9", 0, 3, false, true, glass1);
        mirrorFill("mirrorfill_10", 0, 4, false, false, glass1);
        mirrorFill("mirrorfill_11", 1, 3, false, true, glass1);
        mirrorFill("mirrorfill_12", 1, 2, false, true, glass1);
        mirrorFill("mirrorfill_13", 2, 3, false, true, glass1);
        mirrorFill("mirrorfill_14", 2, 4, false, false, glass1);


        Level level = automator.getLevel();
        Level nextLevel = plugin.getLevelManager().getHighestLevel().getLevel() > level.getLevel() ? this.plugin.getLevelManager().getLevel(level.getLevel() + 1) : null;

        // main automator information icon
        setItem("information", 1, 4, GuiUtils.createButtonItem(
                Settings.AUTOMATOR_ITEM.getMaterial(XMaterial.OBSIDIAN),
                plugin.getLocale().getMessage("interface.automator.currentlevel")
                        .processPlaceholder("level", level.getLevel()).toText(),
                getAutomatorDescription(nextLevel)));

        if (Settings.UPGRADE_WITH_XP.getBoolean()
                && level.getCostExperience() != -1
                && player.hasPermission("EpicAutomators.Upgrade.XP")) {
            setButton("upgrade_xp", 1, 2, GuiUtils.createButtonItem(
                            Settings.XP_ICON.getMaterial(XMaterial.EXPERIENCE_BOTTLE),
                            plugin.getLocale().getMessage("interface.automator.upgradewithxp").getMessage(),
                            nextLevel != null
                                    ? plugin.getLocale().getMessage("interface.automator.upgradewithxplore")
                                    .processPlaceholder("cost", nextLevel.getCostExperience()).getMessage()
                                    : plugin.getLocale().getMessage("interface.automator.alreadymaxed").getMessage()),
                    (event) -> {
                        automator.upgrade(this.player, CostType.EXPERIENCE);
                        showPage();
                    });
        }

        if (Settings.UPGRADE_WITH_ECONOMY.getBoolean()
                && level.getCostEconomy() != -1
                && this.player.hasPermission("EpicAutomators.Upgrade.ECO")) {
            setButton("upgrade_economy", 1, 6, GuiUtils.createButtonItem(
                            Settings.ECO_ICON.getMaterial(XMaterial.SUNFLOWER),
                            this.plugin.getLocale().getMessage("interface.automator.upgradewitheconomy").getMessage(),
                            nextLevel != null
                                    ? this.plugin.getLocale().getMessage("interface.automator.upgradewitheconomylore")
                                    .processPlaceholder("cost", NumberUtils.formatNumber(nextLevel.getCostEconomy())).getMessage()
                                    : this.plugin.getLocale().getMessage("interface.automator.alreadymaxed").getMessage()),
                    (event) -> {
                        automator.upgrade(this.player, CostType.ECONOMY);
                        showPage();
                    });
        }

        int num = -2;
        if (level.getSpeedMultiplier() != 0)
            num++;
        if (level.isAutoPickup())
            num++;
        if (level.getMaxDistance() != 0)
            num++;
        if (level.getMaxBlocks() != 0)
            num++;
        if (level.getDamage() > 1)
            num++;
        if (level.isCrops())
            num++;
        if (level.isAutoSmelt())
            num++;

        int current = 0;

        if (level.getMaxDistance() != 0) {
            setButton("distance", infoIconOrder[num][current++], GuiUtils.createButtonItem(
                            Settings.DISTANCE_ICON.getMaterial(XMaterial.COMPASS),
                            this.plugin.getLocale().getMessage("interface.automator.distancetitle").getMessage(),
                            this.plugin.getLocale().getMessage("interface.automator.distanceinfo")
                                    .processPlaceholder("amount", automator.getLaserDistance()).getMessageLines('|')),
                    event -> {
                        int strength = automator.getLaserDistance();
                        if (event.clickType.isRightClick()) {
                            if (strength < level.getMaxDistance())
                                automator.setLaserDistance(strength + 5);
                        } else {
                            if (strength > 5)
                                automator.setLaserDistance(strength - 5);
                        }
                        automator.save("laser_distance");
                        showPage();
                    });
        }

        if (level.getMaxBlocks() != 0) {
            setButton("blocks", infoIconOrder[num][current++], GuiUtils.createButtonItem(
                            Settings.BLOCKS_ICON.getMaterial(XMaterial.STONE),
                            this.plugin.getLocale().getMessage("interface.automator.blockstitle").getMessage(),
                            this.plugin.getLocale().getMessage("interface.automator.blocksinfo")
                                    .processPlaceholder("amount", automator.getMaxBlocks()).getMessageLines('|')),
                    event -> {
                        int strength = automator.getMaxBlocks();
                        if (event.clickType.isRightClick()) {
                            if (strength < level.getMaxBlocks())
                                automator.setMaxBlocks(strength + 1);
                        } else {
                            if (strength > 0)
                                automator.setMaxBlocks(strength - 1);
                        }
                        automator.save("max_blocks");
                        showPage();
                    });
        }

        if (level.isAutoPickup()) {
            setButton("auto_pickup", infoIconOrder[num][current++], GuiUtils.createButtonItem(
                            Settings.AUTO_PICKUP_ICON.getMaterial(XMaterial.HOPPER),
                            this.plugin.getLocale().getMessage("interface.automator.autopickuptitle").getMessage(),
                            this.plugin.getLocale().getMessage("interface.automator.autopickupinfo")
                                    .processPlaceholder("status", automator.isAutoPickup() ? plugin.getLocale().getMessage("on").toText()
                                            : plugin.getLocale().getMessage("off").toText()).getMessageLines('|')),
                    event -> {
                        automator.setAutoPickup(!automator.isAutoPickup());
                        automator.save("auto_pickup");
                        showPage();
                    });
        }

        if (level.getDamage() > 1) {
            setButton("damage", infoIconOrder[num][current++], GuiUtils.createButtonItem(
                            Settings.DAMAGE_ICON.getMaterial(XMaterial.DIAMOND_SWORD),
                            this.plugin.getLocale().getMessage("interface.automator.damagetitle").getMessage(),
                            this.plugin.getLocale().getMessage("interface.automator.damageinfo")
                                    .processPlaceholder("status", automator.doesDamage() ? plugin.getLocale().getMessage("on").toText()
                                            : plugin.getLocale().getMessage("off").toText()).getMessageLines('|')),
                    event -> {
                        automator.setDoesDamage(!automator.doesDamage());
                        automator.save("does_damage");
                        showPage();
                    });
        }

        if (level.isCrops()) {
            setButton("crops", infoIconOrder[num][current++], GuiUtils.createButtonItem(
                            Settings.CROPS_ICON.getMaterial(XMaterial.WHEAT),
                            this.plugin.getLocale().getMessage("interface.automator.cropstitle").getMessage(),
                            this.plugin.getLocale().getMessage("interface.automator.cropsinfo")
                                    .processPlaceholder("harvesting", automator.isAutoHarvest() ? plugin.getLocale().getMessage("on").toText()
                                            : plugin.getLocale().getMessage("off").toText())
                                    .processPlaceholder("replant", automator.isReplant() ? plugin.getLocale().getMessage("on").toText()
                                            : plugin.getLocale().getMessage("off").toText()).getMessageLines('|')),
                    event -> {
                        if (event.clickType.isLeftClick()) {
                            automator.setCrops(!automator.isAutoHarvest());
                            automator.save("crops");
                        } else if (event.clickType.isRightClick()) {
                            automator.setReplant(!automator.isReplant());
                            automator.save("replant");
                        }

                        showPage();
                    });
        }

        if (level.isAutoSmelt()) {
            setButton("auto_smelt", infoIconOrder[num][current++], GuiUtils.createButtonItem(
                            Settings.AUTO_SMELT_ICON.getMaterial(XMaterial.FURNACE),
                            this.plugin.getLocale().getMessage("interface.automator.autosmelttitle").getMessage(),
                            this.plugin.getLocale().getMessage("interface.automator.autosmeltinfo")
                                    .processPlaceholder("status", automator.isAutoSmelt() ? plugin.getLocale().getMessage("on").toText()
                                            : plugin.getLocale().getMessage("off").toText()).getMessageLines('|')),
                    event -> {
                        automator.setAutoSmelt(!automator.isAutoSmelt());
                        automator.save("auto_smelt");
                        showPage();
                    });
        }

        if (!automator.isRunning() && !automator.isFueled()) {
            setItem(4, GuiUtils.createButtonItem(XMaterial.YELLOW_DYE, plugin.getLocale()
                    .getMessage("interface.automator.nofuel").getMessage()));
        } else {
            XMaterial material = automator.isRunning() ? XMaterial.REDSTONE_BLOCK : XMaterial.EMERALD_BLOCK;
            setButton(4, GuiUtils.createButtonItem(material, automator.isRunning() ? plugin.getLocale()
                            .getMessage("interface.automator.stop").getMessage() : plugin.getLocale()
                            .getMessage("interface.automator.start").getMessage()),
                    event -> {
                        automator.setRunning(!automator.isRunning());
                        automator.save("running");
                        showPage();
                    });
        }
    }

    private void runTask() {
        this.task = Bukkit.getScheduler().scheduleSyncRepeatingTask(this.plugin, () -> {
            updateAutomator();
            showPage();
        }, 2L, 1L);
    }

    public void updateInventory() {
        ItemStack[] inventory = automator.getInventory();
        for (int i = 0; i < 27; ++i)
            setItem(i + 27, inventory[i]);
        automator.setWasUpdated(false);
    }

    public void updateAutomator() {
        if (automator.wasUpdated())
            return;

        boolean wasUpdated = false;
        ItemStack[] inventory = automator.getInventory();
        for (int i = 0; i < 27; ++i) {
            ItemStack item = getItem(i + 27);
            if (item == null || !item.equals(inventory[i])) {
                wasUpdated = true;
                inventory[i] = item;
            }
        }
        if (wasUpdated) {
            automator.setInventory(inventory);
            automator.save("inventory");
        }
    }

    private List<String> getAutomatorDescription(Level nextLevel) {
        Level level = automator.getLevel();
        ArrayList<String> lore = new ArrayList<>();
        lore.addAll(level.getDescription());
        lore.add("");
        if (nextLevel == null) {
            lore.add(plugin.getLocale().getMessage("interface.automator.alreadymaxed").toText());
        } else {
            lore.add(plugin.getLocale().getMessage("interface.automator.level")
                    .processPlaceholder("level", nextLevel.getLevel()).toText());
            lore.addAll(nextLevel.getDescription());
        }
        return lore;
    }
}
