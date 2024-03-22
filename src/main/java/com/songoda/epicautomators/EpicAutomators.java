package com.songoda.epicautomators;

import com.craftaro.core.SongodaCore;
import com.craftaro.core.SongodaPlugin;
import com.craftaro.core.commands.CommandManager;
import com.craftaro.core.configuration.Config;
import com.craftaro.core.data.DatabaseManager;
import com.craftaro.core.gui.GuiManager;
import com.craftaro.core.hooks.ProtectionManager;
import com.craftaro.core.third_party.de.tr7zw.nbtapi.NBTItem;
import com.craftaro.core.utils.TextUtils;
import com.craftaro.third_party.com.cryptomorin.xseries.XMaterial;
import com.songoda.epicautomators.automator.AutomatorManager;
import com.songoda.epicautomators.automator.levels.Level;
import com.songoda.epicautomators.automator.levels.LevelManager;
import com.songoda.epicautomators.commands.CommandGive;
import com.songoda.epicautomators.commands.CommandReload;
import com.songoda.epicautomators.listeners.BlockListeners;
import com.songoda.epicautomators.listeners.EntityListeners;
import com.songoda.epicautomators.settings.Settings;
import com.songoda.epicautomators.task.AutomationTask;
import com.songoda.epicautomators.task.HopperTask;
import com.songoda.epicautomators.task.projectile.IdleTask;
import com.songoda.epicautomators.task.projectile.ProjectileTask;
import com.songoda.epicautomators.utils.EpicHoppersContainer;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.awt.*;
import java.io.File;
import java.util.Arrays;
import java.util.List;

public class EpicAutomators extends SongodaPlugin {

    private final Config levelsFile = new Config(this, "levels.yml");

    private AutomationTask automationTask;
    private ProjectileTask projectileTask;

    private AutomatorManager automatorManager;
    private LevelManager levelManager;

    private final GuiManager guiManager = new GuiManager(this);

    private CommandManager commandManager;

    private DatabaseManager databaseManager;

    @Override
    public void onPluginLoad() {

    }

    @Override
    public void onPluginEnable() {
        // Run Songoda Updater
        SongodaCore.registerPlugin(this, -1, XMaterial.REDSTONE);

        // Config
        Settings.setupConfig();
        setLocale(Settings.LANGUGE_MODE.getString(), false);

        // Register commands
        this.commandManager = new CommandManager(this);
        this.commandManager.addMainCommand("EpicAutomators")
                .addSubCommands(
                        new CommandGive(this),
                        new CommandReload(this));

        loadLevelManager();

        automatorManager = new AutomatorManager();


        PluginManager pluginManager = getServer().getPluginManager();

        pluginManager.registerEvents(new BlockListeners(this), this);
        pluginManager.registerEvents(new EntityListeners(), this);

        automationTask = new AutomationTask(this);
        projectileTask = new ProjectileTask(this);

        if (Settings.SHOW_IDLE_PARTICLES.getBoolean())
            new IdleTask(this);

        if (Bukkit.getPluginManager().isPluginEnabled("EpicHoppers")) {
            new EpicHoppersContainer();
        } else {
            new HopperTask(this);
        }

        databaseManager = new DatabaseManager(this);

        databaseManager.load("Automators", () -> automatorManager.loadData());

        ProtectionManager.load(this);

    }

    @Override
    public void onPluginDisable() {

    }

    @Override
    public void onDataLoad() {

    }

    @Override
    public void onConfigReload() {
        this.setLocale(getConfig().getString("System.Language Mode"), true);
        this.locale.reloadMessages();
    }

    @Override
    public List<Config> getExtraConfig() {
        return Arrays.asList(this.levelsFile, databaseManager.getConfig());
    }

    private void loadLevelManager() {
        if (!new File(this.getDataFolder(), "levels.yml").exists()) {
            this.saveResource("levels.yml", false);
        }
        this.levelsFile.load();

        // Load an instance of LevelManager
        this.levelManager = new LevelManager();

        /*
         * Register Levels into LevelManager from configuration.
         */
        for (String levelName : this.levelsFile.getKeys(false)) {
            ConfigurationSection levels = this.levelsFile.getConfigurationSection(levelName);

            int level = Integer.parseInt(levelName.split("-")[1]);
            int costExperience = levels.getInt("Cost-xp");
            int costEconomy = levels.getInt("Cost-eco");
            int distance = levels.getInt("Max-Distance");
            int maxBlocks = levels.getInt("Max-Blocks");
            double speedMultiplier = levels.getDouble("Speed-Multiplier");
            boolean autoPickup = levels.getBoolean("Auto-Pickup");
            boolean crops = levels.getBoolean("Crops");
            boolean autoSmelt = levels.getBoolean("Auto-Smelt");
            int damage = levels.getInt("Damage");
            String hex = levels.getString("Color");
            Color color = Color.decode(hex);


            Level levelObj = new Level(level, costExperience, costEconomy, speedMultiplier, distance, maxBlocks, autoPickup, damage, color, crops, autoSmelt);

            this.levelManager.addLevel(levelObj);
        }
        this.levelsFile.saveChanges();
    }

    public AutomatorManager getAutomatorManager() {
        return this.automatorManager;
    }

    public LevelManager getLevelManager() {
        return this.levelManager;
    }

    public AutomationTask getAutomationTask() {
        return this.automationTask;
    }

    public ProjectileTask getProjectileTask() {
        return this.projectileTask;
    }

    public GuiManager getGuiManager() {
        return guiManager;
    }

    public static EpicAutomators getInstance() {
        return JavaPlugin.getPlugin(EpicAutomators.class);
    }

    public ItemStack createLeveledAutomator(int level) {
        ItemStack item = new ItemStack(Settings.AUTOMATOR_ITEM.getMaterial().parseMaterial(), 1);

        ItemMeta itemmeta = item.getItemMeta();
        itemmeta.setDisplayName(TextUtils.formatText(formatName(level)));
        item.setItemMeta(itemmeta);

        NBTItem nbtItem = new NBTItem(item);
        nbtItem.setInteger("level", level);

        return nbtItem.getItem();
    }

    public int getAutomatorLevel(ItemStack item) {
        NBTItem nbtItem = new NBTItem(item);

        if (nbtItem.hasTag("level")) {
            return nbtItem.getInteger("level");
        }
        return -1;
    }

    public String formatName(int level) {
        return getLocale()
                .getMessage("general.nametag.automator")
                .processPlaceholder("level", level)
                .getMessage();
    }
}
