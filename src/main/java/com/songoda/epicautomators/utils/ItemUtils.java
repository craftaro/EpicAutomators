package com.songoda.epicautomators.utils;

import com.craftaro.third_party.com.cryptomorin.xseries.XMaterial;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class ItemUtils {

    private static final Map<XMaterial, Integer> FUEL_BURN_TIMES = new HashMap<>();

    static {
        FUEL_BURN_TIMES.put(XMaterial.ACACIA_FENCE, 300);
        FUEL_BURN_TIMES.put(XMaterial.ACACIA_FENCE_GATE, 300);
        FUEL_BURN_TIMES.put(XMaterial.ACACIA_LOG, 300);
        FUEL_BURN_TIMES.put(XMaterial.ACACIA_PLANKS, 300);
        FUEL_BURN_TIMES.put(XMaterial.ACACIA_PRESSURE_PLATE, 300);
        FUEL_BURN_TIMES.put(XMaterial.ACACIA_SIGN, 200);
        FUEL_BURN_TIMES.put(XMaterial.ACACIA_SLAB, 150);
        FUEL_BURN_TIMES.put(XMaterial.ACACIA_STAIRS, 300);
        FUEL_BURN_TIMES.put(XMaterial.ACACIA_TRAPDOOR, 300);
        FUEL_BURN_TIMES.put(XMaterial.ACACIA_WOOD, 300);
        FUEL_BURN_TIMES.put(XMaterial.BAMBOO, 50);
        FUEL_BURN_TIMES.put(XMaterial.BAMBOO_FENCE, 300);
        FUEL_BURN_TIMES.put(XMaterial.BAMBOO_FENCE_GATE, 300);
        FUEL_BURN_TIMES.put(XMaterial.BAMBOO_MOSAIC, 300);
        FUEL_BURN_TIMES.put(XMaterial.BAMBOO_MOSAIC_SLAB, 150);
        FUEL_BURN_TIMES.put(XMaterial.BAMBOO_MOSAIC_STAIRS, 300);
        FUEL_BURN_TIMES.put(XMaterial.BAMBOO_PLANKS, 300);
        FUEL_BURN_TIMES.put(XMaterial.BAMBOO_PRESSURE_PLATE, 300);
        FUEL_BURN_TIMES.put(XMaterial.BAMBOO_SIGN, 200);
        FUEL_BURN_TIMES.put(XMaterial.BAMBOO_SLAB, 150);
        FUEL_BURN_TIMES.put(XMaterial.BAMBOO_STAIRS, 300);
        FUEL_BURN_TIMES.put(XMaterial.BAMBOO_TRAPDOOR, 300);
        FUEL_BURN_TIMES.put(XMaterial.BARREL, 300);
        FUEL_BURN_TIMES.put(XMaterial.BIRCH_FENCE, 300);
        FUEL_BURN_TIMES.put(XMaterial.BIRCH_FENCE_GATE, 300);
        FUEL_BURN_TIMES.put(XMaterial.BIRCH_LOG, 300);
        FUEL_BURN_TIMES.put(XMaterial.BIRCH_PLANKS, 300);
        FUEL_BURN_TIMES.put(XMaterial.BIRCH_PRESSURE_PLATE, 300);
        FUEL_BURN_TIMES.put(XMaterial.BIRCH_SIGN, 200);
        FUEL_BURN_TIMES.put(XMaterial.BIRCH_SLAB, 150);
        FUEL_BURN_TIMES.put(XMaterial.BIRCH_STAIRS, 300);
        FUEL_BURN_TIMES.put(XMaterial.BIRCH_TRAPDOOR, 300);
        FUEL_BURN_TIMES.put(XMaterial.BIRCH_WOOD, 300);
        FUEL_BURN_TIMES.put(XMaterial.BLACK_BANNER, 300);
        FUEL_BURN_TIMES.put(XMaterial.BLACK_CARPET, 67);
        FUEL_BURN_TIMES.put(XMaterial.BLACK_WOOL, 100);
        FUEL_BURN_TIMES.put(XMaterial.BLAZE_ROD, 2400);
        FUEL_BURN_TIMES.put(XMaterial.BLUE_BANNER, 300);
        FUEL_BURN_TIMES.put(XMaterial.BLUE_CARPET, 67);
        FUEL_BURN_TIMES.put(XMaterial.BLUE_WOOL, 100);
        FUEL_BURN_TIMES.put(XMaterial.BOOKSHELF, 300);
        FUEL_BURN_TIMES.put(XMaterial.BOW, 200);
        FUEL_BURN_TIMES.put(XMaterial.BOWL, 100);
        FUEL_BURN_TIMES.put(XMaterial.BROWN_BANNER, 300);
        FUEL_BURN_TIMES.put(XMaterial.BROWN_CARPET, 67);
        FUEL_BURN_TIMES.put(XMaterial.BROWN_WOOL, 100);
        FUEL_BURN_TIMES.put(XMaterial.CHARCOAL, 1600);
        FUEL_BURN_TIMES.put(XMaterial.CHERRY_FENCE, 300);
        FUEL_BURN_TIMES.put(XMaterial.CHERRY_FENCE_GATE, 300);
        FUEL_BURN_TIMES.put(XMaterial.CHERRY_LOG, 300);
        FUEL_BURN_TIMES.put(XMaterial.CHERRY_PLANKS, 300);
        FUEL_BURN_TIMES.put(XMaterial.CHERRY_PRESSURE_PLATE, 300);
        FUEL_BURN_TIMES.put(XMaterial.CHERRY_SIGN, 200);
        FUEL_BURN_TIMES.put(XMaterial.CHERRY_SLAB, 150);
        FUEL_BURN_TIMES.put(XMaterial.CHERRY_STAIRS, 300);
        FUEL_BURN_TIMES.put(XMaterial.CHERRY_TRAPDOOR, 300);
        FUEL_BURN_TIMES.put(XMaterial.CHERRY_WOOD, 300);
        FUEL_BURN_TIMES.put(XMaterial.CHEST, 300);
        FUEL_BURN_TIMES.put(XMaterial.COAL, 1600);
        FUEL_BURN_TIMES.put(XMaterial.COAL_BLOCK, 16000);
        FUEL_BURN_TIMES.put(XMaterial.COMPOSTER, 300);
        FUEL_BURN_TIMES.put(XMaterial.CRAFTING_TABLE, 300);
        FUEL_BURN_TIMES.put(XMaterial.CRIMSON_FENCE, 300);
        FUEL_BURN_TIMES.put(XMaterial.CRIMSON_FENCE_GATE, 300);
        FUEL_BURN_TIMES.put(XMaterial.CRIMSON_HYPHAE, 300);
        FUEL_BURN_TIMES.put(XMaterial.CRIMSON_PLANKS, 300);
        FUEL_BURN_TIMES.put(XMaterial.CRIMSON_PRESSURE_PLATE, 300);
        FUEL_BURN_TIMES.put(XMaterial.CRIMSON_SIGN, 200);
        FUEL_BURN_TIMES.put(XMaterial.CRIMSON_SLAB, 150);
        FUEL_BURN_TIMES.put(XMaterial.CRIMSON_STAIRS, 300);
        FUEL_BURN_TIMES.put(XMaterial.CRIMSON_STEM, 300);
        FUEL_BURN_TIMES.put(XMaterial.CRIMSON_TRAPDOOR, 300);
        FUEL_BURN_TIMES.put(XMaterial.CYAN_BANNER, 300);
        FUEL_BURN_TIMES.put(XMaterial.CYAN_CARPET, 67);
        FUEL_BURN_TIMES.put(XMaterial.CYAN_WOOL, 100);
        FUEL_BURN_TIMES.put(XMaterial.DARK_OAK_FENCE, 300);
        FUEL_BURN_TIMES.put(XMaterial.DARK_OAK_FENCE_GATE, 300);
        FUEL_BURN_TIMES.put(XMaterial.DARK_OAK_LOG, 300);
        FUEL_BURN_TIMES.put(XMaterial.DARK_OAK_PLANKS, 300);
        FUEL_BURN_TIMES.put(XMaterial.DARK_OAK_PRESSURE_PLATE, 300);
        FUEL_BURN_TIMES.put(XMaterial.DARK_OAK_SIGN, 200);
        FUEL_BURN_TIMES.put(XMaterial.DARK_OAK_SLAB, 150);
        FUEL_BURN_TIMES.put(XMaterial.DARK_OAK_STAIRS, 300);
        FUEL_BURN_TIMES.put(XMaterial.DARK_OAK_TRAPDOOR, 300);
        FUEL_BURN_TIMES.put(XMaterial.DARK_OAK_WOOD, 300);
        FUEL_BURN_TIMES.put(XMaterial.DAYLIGHT_DETECTOR, 300);
        FUEL_BURN_TIMES.put(XMaterial.DEAD_BUSH, 100);
        FUEL_BURN_TIMES.put(XMaterial.DRIED_KELP_BLOCK, 4000);
        FUEL_BURN_TIMES.put(XMaterial.FISHING_ROD, 300);
        FUEL_BURN_TIMES.put(XMaterial.GRAY_BANNER, 300);
        FUEL_BURN_TIMES.put(XMaterial.GRAY_CARPET, 67);
        FUEL_BURN_TIMES.put(XMaterial.GRAY_WOOL, 100);
        FUEL_BURN_TIMES.put(XMaterial.GREEN_BANNER, 300);
        FUEL_BURN_TIMES.put(XMaterial.GREEN_CARPET, 67);
        FUEL_BURN_TIMES.put(XMaterial.GREEN_WOOL, 100);
        FUEL_BURN_TIMES.put(XMaterial.JUKEBOX, 300);
        FUEL_BURN_TIMES.put(XMaterial.JUNGLE_FENCE, 300);
        FUEL_BURN_TIMES.put(XMaterial.JUNGLE_FENCE_GATE, 300);
        FUEL_BURN_TIMES.put(XMaterial.JUNGLE_LOG, 300);
        FUEL_BURN_TIMES.put(XMaterial.JUNGLE_PLANKS, 300);
        FUEL_BURN_TIMES.put(XMaterial.JUNGLE_PRESSURE_PLATE, 300);
        FUEL_BURN_TIMES.put(XMaterial.JUNGLE_SIGN, 200);
        FUEL_BURN_TIMES.put(XMaterial.JUNGLE_SLAB, 150);
        FUEL_BURN_TIMES.put(XMaterial.JUNGLE_STAIRS, 300);
        FUEL_BURN_TIMES.put(XMaterial.JUNGLE_TRAPDOOR, 300);
        FUEL_BURN_TIMES.put(XMaterial.JUNGLE_WOOD, 300);
        FUEL_BURN_TIMES.put(XMaterial.LADDER, 300);
        FUEL_BURN_TIMES.put(XMaterial.LAVA_BUCKET, 20000);
        FUEL_BURN_TIMES.put(XMaterial.LECTERN, 300);
        FUEL_BURN_TIMES.put(XMaterial.LIGHT_BLUE_BANNER, 300);
        FUEL_BURN_TIMES.put(XMaterial.LIGHT_BLUE_CARPET, 67);
        FUEL_BURN_TIMES.put(XMaterial.LIGHT_BLUE_WOOL, 100);
        FUEL_BURN_TIMES.put(XMaterial.LIGHT_GRAY_BANNER, 300);
        FUEL_BURN_TIMES.put(XMaterial.LIGHT_GRAY_CARPET, 67);
        FUEL_BURN_TIMES.put(XMaterial.LIGHT_GRAY_WOOL, 100);
        FUEL_BURN_TIMES.put(XMaterial.LIME_BANNER, 300);
        FUEL_BURN_TIMES.put(XMaterial.LIME_CARPET, 67);
        FUEL_BURN_TIMES.put(XMaterial.LIME_WOOL, 100);
        FUEL_BURN_TIMES.put(XMaterial.LOOM, 300);
        FUEL_BURN_TIMES.put(XMaterial.MAGENTA_BANNER, 300);
        FUEL_BURN_TIMES.put(XMaterial.MAGENTA_CARPET, 67);
        FUEL_BURN_TIMES.put(XMaterial.MAGENTA_WOOL, 100);
        FUEL_BURN_TIMES.put(XMaterial.MANGROVE_FENCE, 300);
        FUEL_BURN_TIMES.put(XMaterial.MANGROVE_FENCE_GATE, 300);
        FUEL_BURN_TIMES.put(XMaterial.MANGROVE_LOG, 300);
        FUEL_BURN_TIMES.put(XMaterial.MANGROVE_PLANKS, 300);
        FUEL_BURN_TIMES.put(XMaterial.MANGROVE_PRESSURE_PLATE, 300);
        FUEL_BURN_TIMES.put(XMaterial.MANGROVE_ROOTS, 300);
        FUEL_BURN_TIMES.put(XMaterial.MANGROVE_SIGN, 200);
        FUEL_BURN_TIMES.put(XMaterial.MANGROVE_SLAB, 150);
        FUEL_BURN_TIMES.put(XMaterial.MANGROVE_STAIRS, 300);
        FUEL_BURN_TIMES.put(XMaterial.MANGROVE_TRAPDOOR, 300);
        FUEL_BURN_TIMES.put(XMaterial.MANGROVE_WOOD, 300);
        FUEL_BURN_TIMES.put(XMaterial.NOTE_BLOCK, 300);
        FUEL_BURN_TIMES.put(XMaterial.OAK_FENCE, 300);
        FUEL_BURN_TIMES.put(XMaterial.OAK_FENCE_GATE, 300);
        FUEL_BURN_TIMES.put(XMaterial.OAK_LOG, 300);
        FUEL_BURN_TIMES.put(XMaterial.OAK_PLANKS, 300);
        FUEL_BURN_TIMES.put(XMaterial.OAK_PRESSURE_PLATE, 300);
        FUEL_BURN_TIMES.put(XMaterial.OAK_SIGN, 200);
        FUEL_BURN_TIMES.put(XMaterial.OAK_SLAB, 150);
        FUEL_BURN_TIMES.put(XMaterial.OAK_STAIRS, 300);
        FUEL_BURN_TIMES.put(XMaterial.OAK_TRAPDOOR, 300);
        FUEL_BURN_TIMES.put(XMaterial.OAK_WOOD, 300);
        FUEL_BURN_TIMES.put(XMaterial.ORANGE_BANNER, 300);
        FUEL_BURN_TIMES.put(XMaterial.ORANGE_CARPET, 67);
        FUEL_BURN_TIMES.put(XMaterial.ORANGE_WOOL, 100);
        FUEL_BURN_TIMES.put(XMaterial.PINK_BANNER, 300);
        FUEL_BURN_TIMES.put(XMaterial.PINK_CARPET, 67);
        FUEL_BURN_TIMES.put(XMaterial.PINK_WOOL, 100);
        FUEL_BURN_TIMES.put(XMaterial.PURPLE_BANNER, 300);
        FUEL_BURN_TIMES.put(XMaterial.PURPLE_CARPET, 67);
        FUEL_BURN_TIMES.put(XMaterial.PURPLE_WOOL, 100);
        FUEL_BURN_TIMES.put(XMaterial.RED_BANNER, 300);
        FUEL_BURN_TIMES.put(XMaterial.RED_CARPET, 67);
        FUEL_BURN_TIMES.put(XMaterial.RED_WOOL, 100);
        FUEL_BURN_TIMES.put(XMaterial.SCAFFOLDING, 400);
        FUEL_BURN_TIMES.put(XMaterial.SMITHING_TABLE, 300);
        FUEL_BURN_TIMES.put(XMaterial.SPRUCE_FENCE, 300);
        FUEL_BURN_TIMES.put(XMaterial.SPRUCE_FENCE_GATE, 300);
        FUEL_BURN_TIMES.put(XMaterial.SPRUCE_LOG, 300);
        FUEL_BURN_TIMES.put(XMaterial.SPRUCE_PLANKS, 300);
        FUEL_BURN_TIMES.put(XMaterial.SPRUCE_PRESSURE_PLATE, 300);
        FUEL_BURN_TIMES.put(XMaterial.SPRUCE_SIGN, 200);
        FUEL_BURN_TIMES.put(XMaterial.SPRUCE_SLAB, 150);
        FUEL_BURN_TIMES.put(XMaterial.SPRUCE_STAIRS, 300);
        FUEL_BURN_TIMES.put(XMaterial.SPRUCE_TRAPDOOR, 300);
        FUEL_BURN_TIMES.put(XMaterial.SPRUCE_WOOD, 300);
        FUEL_BURN_TIMES.put(XMaterial.STICK, 100);
        FUEL_BURN_TIMES.put(XMaterial.STRIPPED_ACACIA_LOG, 300);
        FUEL_BURN_TIMES.put(XMaterial.STRIPPED_ACACIA_WOOD, 300);
        FUEL_BURN_TIMES.put(XMaterial.STRIPPED_BAMBOO_BLOCK, 300);
        FUEL_BURN_TIMES.put(XMaterial.STRIPPED_BIRCH_LOG, 300);
        FUEL_BURN_TIMES.put(XMaterial.STRIPPED_BIRCH_WOOD, 300);
        FUEL_BURN_TIMES.put(XMaterial.STRIPPED_CHERRY_LOG, 300);
        FUEL_BURN_TIMES.put(XMaterial.STRIPPED_CHERRY_WOOD, 300);
        FUEL_BURN_TIMES.put(XMaterial.STRIPPED_CRIMSON_HYPHAE, 300);
        FUEL_BURN_TIMES.put(XMaterial.STRIPPED_CRIMSON_STEM, 300);
        FUEL_BURN_TIMES.put(XMaterial.STRIPPED_DARK_OAK_LOG, 300);
        FUEL_BURN_TIMES.put(XMaterial.STRIPPED_DARK_OAK_WOOD, 300);
        FUEL_BURN_TIMES.put(XMaterial.STRIPPED_JUNGLE_LOG, 300);
        FUEL_BURN_TIMES.put(XMaterial.STRIPPED_JUNGLE_WOOD, 300);
        FUEL_BURN_TIMES.put(XMaterial.STRIPPED_MANGROVE_LOG, 300);
        FUEL_BURN_TIMES.put(XMaterial.STRIPPED_MANGROVE_WOOD, 300);
        FUEL_BURN_TIMES.put(XMaterial.STRIPPED_OAK_LOG, 300);
        FUEL_BURN_TIMES.put(XMaterial.STRIPPED_OAK_WOOD, 300);
        FUEL_BURN_TIMES.put(XMaterial.STRIPPED_SPRUCE_LOG, 300);
        FUEL_BURN_TIMES.put(XMaterial.STRIPPED_SPRUCE_WOOD, 300);
        FUEL_BURN_TIMES.put(XMaterial.STRIPPED_WARPED_HYPHAE, 300);
        FUEL_BURN_TIMES.put(XMaterial.STRIPPED_WARPED_STEM, 300);
        FUEL_BURN_TIMES.put(XMaterial.TRAPPED_CHEST, 300);
        FUEL_BURN_TIMES.put(XMaterial.WARPED_FENCE, 300);
        FUEL_BURN_TIMES.put(XMaterial.WARPED_FENCE_GATE, 300);
        FUEL_BURN_TIMES.put(XMaterial.WARPED_HYPHAE, 300);
        FUEL_BURN_TIMES.put(XMaterial.WARPED_PLANKS, 300);
        FUEL_BURN_TIMES.put(XMaterial.WARPED_PRESSURE_PLATE, 300);
        FUEL_BURN_TIMES.put(XMaterial.WARPED_SIGN, 200);
        FUEL_BURN_TIMES.put(XMaterial.WARPED_SLAB, 150);
        FUEL_BURN_TIMES.put(XMaterial.WARPED_STAIRS, 300);
        FUEL_BURN_TIMES.put(XMaterial.WARPED_STEM, 300);
        FUEL_BURN_TIMES.put(XMaterial.WARPED_TRAPDOOR, 300);
        FUEL_BURN_TIMES.put(XMaterial.WHITE_BANNER, 300);
        FUEL_BURN_TIMES.put(XMaterial.WHITE_CARPET, 67);
        FUEL_BURN_TIMES.put(XMaterial.WHITE_WOOL, 100);
        FUEL_BURN_TIMES.put(XMaterial.WOODEN_AXE, 200);
        FUEL_BURN_TIMES.put(XMaterial.WOODEN_HOE, 200);
        FUEL_BURN_TIMES.put(XMaterial.WOODEN_PICKAXE, 200);
        FUEL_BURN_TIMES.put(XMaterial.WOODEN_SHOVEL, 200);
        FUEL_BURN_TIMES.put(XMaterial.WOODEN_SWORD, 200);
        FUEL_BURN_TIMES.put(XMaterial.YELLOW_BANNER, 300);
        FUEL_BURN_TIMES.put(XMaterial.YELLOW_CARPET, 67);
        FUEL_BURN_TIMES.put(XMaterial.YELLOW_WOOL, 100);
    }

    public static int getBurnTime(XMaterial material) {
        int burnTimeTicks = FUEL_BURN_TIMES.getOrDefault(material, 0);
        return burnTimeTicks / 20;
    }

    public static boolean isFuel(ItemStack item) {
        return isFuel(XMaterial.matchXMaterial(item));
    }

    public static boolean isFuel(XMaterial material) {
        return getBurnTime(material) > 0;
    }

    public static ItemStack getSmeltedItem(Material material) {
        switch (material) {
            case IRON_ORE:
                return new ItemStack(Material.IRON_INGOT);
            case GOLD_ORE:
                return new ItemStack(Material.GOLD_INGOT);
            case COBBLESTONE:
                return new ItemStack(Material.STONE);
            case SAND:
                return new ItemStack(Material.GLASS);
            case RED_SAND:
                return new ItemStack(Material.GLASS);
            case NETHERRACK:
                return new ItemStack(Material.NETHER_BRICK);
            case CLAY:
                return new ItemStack(Material.TERRACOTTA);
            case CACTUS:
                return new ItemStack(Material.GREEN_DYE);
            case SEA_PICKLE:
                return new ItemStack(Material.LIME_DYE);
            case WET_SPONGE:
                return new ItemStack(Material.SPONGE);
            case OAK_LOG:
            case SPRUCE_LOG:
            case BIRCH_LOG:
            case JUNGLE_LOG:
            case ACACIA_LOG:
            case DARK_OAK_LOG:
            case STRIPPED_OAK_LOG:
            case STRIPPED_SPRUCE_LOG:
            case STRIPPED_BIRCH_LOG:
            case STRIPPED_JUNGLE_LOG:
            case STRIPPED_ACACIA_LOG:
            case STRIPPED_DARK_OAK_LOG:
            case OAK_WOOD:
            case SPRUCE_WOOD:
            case BIRCH_WOOD:
            case JUNGLE_WOOD:
            case ACACIA_WOOD:
            case DARK_OAK_WOOD:
            case STRIPPED_OAK_WOOD:
            case STRIPPED_SPRUCE_WOOD:
            case STRIPPED_BIRCH_WOOD:
            case STRIPPED_JUNGLE_WOOD:
            case STRIPPED_ACACIA_WOOD:
            case STRIPPED_DARK_OAK_WOOD:
                return new ItemStack(Material.CHARCOAL);
            case CHORUS_FRUIT:
                return new ItemStack(Material.POPPED_CHORUS_FRUIT);
            case BEEF:
            case CHICKEN:
            case COD:
            case MUTTON:
            case PORKCHOP:
            case RABBIT:
            case SALMON:
            case POTATO:
            case KELP:
                return new ItemStack(material, 1, (short) 1); // Cooked version of the item
            case ANCIENT_DEBRIS:
                return new ItemStack(Material.NETHERITE_SCRAP);
            case QUARTZ_BLOCK:
                return new ItemStack(Material.SMOOTH_QUARTZ);
            case STONE_BRICKS:
                return new ItemStack(Material.CRACKED_STONE_BRICKS);
            default:
                return null;
        }
    }
}