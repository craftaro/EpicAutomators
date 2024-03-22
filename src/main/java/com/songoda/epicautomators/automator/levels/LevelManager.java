package com.songoda.epicautomators.automator.levels;

import com.songoda.epicautomators.automator.Automator;

import java.util.*;

public class LevelManager {

    private final NavigableMap<Integer, Level> registeredLevels = new TreeMap<>();

    public void addLevel(Level level) {
        this.registeredLevels.put(level.getLevel(), level);
    }

    public Level getLevel(int level) {
        return this.registeredLevels.get(level);
    }

    public Level getLowestLevel() {
        return this.registeredLevels.firstEntry().getValue();
    }

    public Level getHighestLevel() {
        return this.registeredLevels.lastEntry().getValue();
    }

    public boolean isLevel(int level) {
        return this.registeredLevels.containsKey(level);
    }

    public Map<Integer, Level> getLevels() {
        return Collections.unmodifiableMap(this.registeredLevels);
    }

    public void clear() {
        this.registeredLevels.clear();
    }
}
