package com.songoda.epicautomators.settings;

import com.craftaro.core.compatibility.CompatibleMaterial;
import com.craftaro.core.compatibility.ServerVersion;
import com.craftaro.core.configuration.Config;
import com.craftaro.core.configuration.ConfigSetting;
import com.craftaro.core.hooks.EconomyManager;
import com.songoda.epicautomators.EpicAutomators;
import org.bukkit.Material;

import java.util.Arrays;

public class Settings {
    static final Config CONFIG = EpicAutomators.getPlugin(EpicAutomators.class).getCoreConfig();

    public static final ConfigSetting AUTOMATOR_ITEM = new ConfigSetting(CONFIG, "Main.Automator Item",
            ServerVersion.isServerVersionAtLeast(ServerVersion.V1_16) ? Material.CRYING_OBSIDIAN.name() : Material.OBSIDIAN.name());
    public static final ConfigSetting PARTICLE_TYPE = new ConfigSetting(CONFIG, "Main.Upgrade Particle Type", "SPELL_WITCH",
            "The type of particle shown when a furnace is upgraded.");

    public static final ConfigSetting ALLOW_UP = new ConfigSetting(CONFIG, "Main.Allow Up", true,
            "Should signs with a '^' on them send projectiles upwards?");

    public static final ConfigSetting ALLOW_DOWN = new ConfigSetting(CONFIG, "Main.Allow Down", true,
            "Should signs with a 'v' on them send projectiles downwards?");

    public static final ConfigSetting ALLOW_STOP = new ConfigSetting(CONFIG, "Main.Allow Stop", true,
            "Should signs with a 'x' on them stop the projectile?");

    public static final ConfigSetting SHOW_IDLE_PARTICLES = new ConfigSetting(CONFIG, "Main.Show Idle Particles", true,
            "Should idle particles be shown when the automator is not running?");

    public static final ConfigSetting ECONOMY_PLUGIN = new ConfigSetting(CONFIG, "Main.Economy", EconomyManager.getEconomy() == null ? "Vault" : EconomyManager.getEconomy().getName(),
            "Which economy plugin should be used?",
            "Supported plugins you have installed: \"" + String.join("\", \"", EconomyManager.getManager().getRegisteredPlugins()) + "\".");
    public static final ConfigSetting ECO_ICON = new ConfigSetting(CONFIG, "Interfaces.Economy Icon", "SUNFLOWER",
            "The icon used in the overview interface that represents the economy upgrade.");
    public static final ConfigSetting XP_ICON = new ConfigSetting(CONFIG, "Interfaces.XP Icon", "EXPERIENCE_BOTTLE",
            "The icon used in the overview interface that represents the experience upgrade.");

    public static final ConfigSetting UPGRADE_WITH_ECONOMY = new ConfigSetting(CONFIG, "Main.Upgrade With Economy", true,
            "Should you be able to upgrade furnaces with economy?");

    public static final ConfigSetting UPGRADE_WITH_XP = new ConfigSetting(CONFIG, "Main.Upgrade With XP", true,
            "Should you be able to upgrade furnaces with experience?");

    public static final ConfigSetting DISTANCE_ICON = new ConfigSetting(CONFIG, "Interfaces.Distance Icon", "COMPASS",
            "The icon used in the overview interface that represents the distance upgrade.");

    public static final ConfigSetting BLOCKS_ICON = new ConfigSetting(CONFIG, "Interfaces.Blocks Icon", "STONE",
            "The icon used in the overview interface that represents the max blocks upgrade.");

    public static final ConfigSetting DAMAGE_ICON = new ConfigSetting(CONFIG, "Interfaces.Damage Icon", "DIAMOND_SWORD",
            "The icon used in the overview interface that represents the damage upgrade.");

    public static final ConfigSetting CROPS_ICON = new ConfigSetting(CONFIG, "Interfaces.Crops Icon", "WHEAT",
            "The icon used in the overview interface that represents the crops upgrade.");

    public static final ConfigSetting AUTO_PICKUP_ICON = new ConfigSetting(CONFIG, "Interfaces.Auto Pickup Icon", "HOPPER",
            "The icon used in the overview interface that represents the auto pickup upgrade.");

    public static final ConfigSetting AUTO_SMELT_ICON = new ConfigSetting(CONFIG, "Interfaces.Auto Smelt Icon", "BLAZE_POWDER",
            "The icon used in the overview interface that represents the auto smelt upgrade.");

    public static final ConfigSetting MATERIAL_BLACKLIST = new ConfigSetting(CONFIG, "Main.Material Blacklist", Arrays.asList("BEDROCK", "BARRIER"),
            "Materials that should not be able to be used in the automator.");

    public static final ConfigSetting GLASS_TYPE_1 = new ConfigSetting(CONFIG, "Interfaces.Glass Type 1", "GRAY_STAINED_GLASS_PANE",
            "The type of glass pane used in the interfaces.");
    public static final ConfigSetting GLASS_TYPE_2 = new ConfigSetting(CONFIG, "Interfaces.Glass Type 2", "BLUE_STAINED_GLASS_PANE",
            "The type of glass pane used in the interfaces.");
    public static final ConfigSetting GLASS_TYPE_3 = new ConfigSetting(CONFIG, "Interfaces.Glass Type 3", "LIGHT_BLUE_STAINED_GLASS_PANE",
            "The type of glass pane used in the interfaces.");


    public static final ConfigSetting LANGUGE_MODE = new ConfigSetting(CONFIG, "System.Language Mode", "en_US",
            "The enabled language file.",
            "More language files (if available) can be found in the plugins data folder.");

    /**
     * In order to set dynamic economy comment correctly, this needs to be
     * called after EconomyManager load
     */
    public static void setupConfig() {
        CONFIG.load();
        CONFIG.setAutoremove(true).setAutosave(true);

        // convert glass pane settings
        int color;
        if ((color = GLASS_TYPE_1.getInt(-1)) != -1) {
            CONFIG.set(GLASS_TYPE_1.getKey(), CompatibleMaterial.getGlassPaneForColor(color).name());
        }
        if ((color = GLASS_TYPE_2.getInt(-1)) != -1) {
            CONFIG.set(GLASS_TYPE_2.getKey(), CompatibleMaterial.getGlassPaneForColor(color).name());
        }
        if ((color = GLASS_TYPE_3.getInt(-1)) != -1) {
            CONFIG.set(GLASS_TYPE_3.getKey(), CompatibleMaterial.getGlassPaneForColor(color).name());
        }

        CONFIG.saveChanges();
    }
}
