package com.songoda.epicautomators.automator.levels;

import com.craftaro.core.locale.Locale;
import com.songoda.epicautomators.EpicAutomators;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class Level {
    private final List<String> description = new ArrayList<>();
    private final int level;
    private final int costExperience;
    private final int costEconomy;
    private final double speedMultiplier;
    private final int maxBlocks;
    private final int maxDistance;
    private final boolean autoPickup;
    private final int damage;
    private final Color color;
    private final boolean crops;
    private final boolean autoSmelt;

    public Level(int level, int costExperience, int costEconomy, double speedMultiplier, int maxDistance, int maxBlocks,
                 boolean autoPickup, int damage, Color color, boolean crops, boolean autoSmelt) {
        this.level = level;
        this.costExperience = costExperience;
        this.costEconomy = costEconomy;
        this.speedMultiplier = speedMultiplier;
        this.maxDistance = maxDistance;
        this.maxBlocks = maxBlocks;
        this.autoPickup = autoPickup;
        this.damage = damage;
        this.color = color;
        this.crops = crops;
        this.autoSmelt = autoSmelt;
        buildDescription();
    }

    public void buildDescription() {
        Locale locale = EpicAutomators.getInstance().getLocale();

        this.description.add(locale.getMessage("interface.automator.speed")
                .processPlaceholder("speed", speedMultiplier).getMessage());
        this.description.add(locale.getMessage("interface.automator.maxdistance")
                .processPlaceholder("distance", maxDistance).getMessage());
        this.description.add(locale.getMessage("interface.automator.maxblocks")
                .processPlaceholder("blocks", maxBlocks).getMessage());

        if (autoPickup)
            this.description.add(locale.getMessage("interface.automator.autopickup")
                    .processPlaceholder("autopickup", autoPickup).getMessage());

        if (damage > 0)
            this.description.add(locale.getMessage("interface.automator.damage")
                    .processPlaceholder("damage", damage).getMessage());

        if (crops)
            this.description.add(locale.getMessage("interface.automator.crops")
                    .processPlaceholder("crops", crops).getMessage());

        if (autoSmelt)
            this.description.add(locale.getMessage("interface.automator.autosmelt")
                    .processPlaceholder("autosmelt", autoSmelt).getMessage());
    }

    public List<String> getDescription() {
        return new ArrayList<>(this.description);
    }

    public int getLevel() {
        return this.level;
    }

    public int getCostExperience() {
        return this.costExperience;
    }

    public int getCostEconomy() {
        return this.costEconomy;
    }

    public double getSpeedMultiplier() {
        return this.speedMultiplier;
    }

    public int getMaxDistance() {
        return this.maxDistance;
    }

    public boolean isAutoPickup() {
        return this.autoPickup;
    }

    public int getMaxBlocks() {
        return this.maxBlocks;
    }

    public int getDamage() {
        return this.damage;
    }

    public Color getColor() {
        return this.color;
    }

    public boolean isCrops() {
        return this.crops;
    }

    public boolean isAutoSmelt() {
        return this.autoSmelt;
    }
}