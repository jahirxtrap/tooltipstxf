package com.jahirtrap.tooltips.init;

import com.jahirtrap.configlib.TXFConfig;

public class ModConfig extends TXFConfig {
    public static final String GENERAL = "general", COLORS = "colors";

    @Entry(category = GENERAL, name = "Enable Mod")
    public static boolean enableMod = true;
    @Entry(category = GENERAL, name = "Time In Seconds")
    public static boolean timeInSeconds = false;
    @Entry(category = GENERAL, name = "Show Durability", itemDisplay = "minecraft:iron_sword")
    public static boolean showDurability = true;
    @Entry(category = GENERAL, name = "Show Food Values", itemDisplay = "minecraft:bread")
    public static boolean showFoodValues = true;
    @Entry(category = GENERAL, name = "Show Compostable", itemDisplay = "minecraft:composter")
    public static boolean showCompostable = true;
    @Entry(category = GENERAL, name = "Show Burn Time", itemDisplay = "minecraft:furnace")
    public static boolean showBurnTime = true;
    @Entry(category = GENERAL, name = "Show Use Cooldown", itemDisplay = "minecraft:clock")
    public static boolean showUseCooldown = false;
    @Entry(category = GENERAL, name = "Show Enchantability", itemDisplay = "minecraft:enchanting_table")
    public static boolean showEnchantability = false;
    @Entry(category = GENERAL, name = "Show Repair Cost", itemDisplay = "minecraft:anvil")
    public static boolean showRepairCost = false;
    @Entry(category = GENERAL, name = "Show Strength", itemDisplay = "minecraft:stone")
    public static boolean showStrength = false;
    @Entry(category = GENERAL, name = "Show Enchantment Power", itemDisplay = "minecraft:bookshelf")
    public static boolean showEnchantmentPower = true;
    @Entry(category = GENERAL, name = "Show Mining Level", itemDisplay = "minecraft:iron_pickaxe")
    public static boolean showMiningLevel = false;
    @Entry(category = GENERAL, name = "Show Mining Speed", itemDisplay = "minecraft:iron_pickaxe")
    public static boolean showMiningSpeed = false;
    @Entry(category = GENERAL, name = "Show Mod Name", itemDisplay = "minecraft:writable_book")
    public static boolean showModName = false;

    @Entry(category = COLORS, name = "Durability Color", width = 7, min = 7, isColor = true)
    public static String durabilityColor = "#555555";
    @Entry(category = COLORS, name = "Food Values Color", width = 7, min = 7, isColor = true)
    public static String foodValuesColor = "#555555";
    @Entry(category = COLORS, name = "Compostable Color", width = 7, min = 7, isColor = true)
    public static String compostableColor = "#555555";
    @Entry(category = COLORS, name = "Burn Time Color", width = 7, min = 7, isColor = true)
    public static String burnTimeColor = "#555555";
    @Entry(category = COLORS, name = "Use Cooldown Color", width = 7, min = 7, isColor = true)
    public static String useCooldownColor = "#555555";
    @Entry(category = COLORS, name = "Enchantability Color", width = 7, min = 7, isColor = true)
    public static String enchantabilityColor = "#555555";
    @Entry(category = COLORS, name = "Repair Cost Color", width = 7, min = 7, isColor = true)
    public static String repairCostColor = "#555555";
    @Entry(category = COLORS, name = "Strength Color", width = 7, min = 7, isColor = true)
    public static String strengthColor = "#555555";
    @Entry(category = COLORS, name = "Enchantment Power Color", width = 7, min = 7, isColor = true)
    public static String enchantmentPowerColor = "#555555";
    @Entry(category = COLORS, name = "Mining Level Color", width = 7, min = 7, isColor = true)
    public static String miningLevelColor = "#555555";
    @Entry(category = COLORS, name = "Mining Speed Color", width = 7, min = 7, isColor = true)
    public static String miningSpeedColor = "#555555";
    @Entry(category = COLORS, name = "Mod Name Color", width = 7, min = 7, isColor = true)
    public static String modNameColor = "#5555ff";
}
