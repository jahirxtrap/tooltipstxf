package com.jahirtrap.tooltips.init;

import com.jahirtrap.configlib.TXFConfig;

public class ModConfig extends TXFConfig {
    @Entry(name = "Enable Mod")
    public static boolean enableMod = true;
    @Entry(name = "Time In Seconds")
    public static boolean timeInSeconds = false;
    @Entry(name = "Show Durability", itemDisplay = "minecraft:iron_sword")
    public static boolean showDurability = true;
    @Entry(name = "Show Food Values", itemDisplay = "minecraft:bread")
    public static boolean showFoodValues = true;
    @Entry(name = "Show Compostable", itemDisplay = "minecraft:composter")
    public static boolean showCompostable = true;
    @Entry(name = "Show Burn Time", itemDisplay = "minecraft:furnace")
    public static boolean showBurnTime = true;
    @Entry(name = "Show Use Cooldown", itemDisplay = "minecraft:clock")
    public static boolean showUseCooldown = false;
    @Entry(name = "Show Enchantability", itemDisplay = "minecraft:enchanting_table")
    public static boolean showEnchantability = false;
    @Entry(name = "Show Repair Cost", itemDisplay = "minecraft:anvil")
    public static boolean showRepairCost = false;
    @Entry(name = "Show Strength", itemDisplay = "minecraft:stone")
    public static boolean showStrength = false;
    @Entry(name = "Show Enchantment Power", itemDisplay = "minecraft:bookshelf")
    public static boolean showEnchantmentPower = true;
    @Entry(name = "Show Mining Level", itemDisplay = "minecraft:iron_pickaxe")
    public static boolean showMiningLevel = false;
    @Entry(name = "Show Mining Speed", itemDisplay = "minecraft:iron_pickaxe")
    public static boolean showMiningSpeed = false;
    @Entry(name = "Show Mod Name", itemDisplay = "minecraft:writable_book")
    public static boolean showModName = false;
    public static Comment colors;
    @Entry(name = "Durability Color", width = 7, min = 7, isColor = true)
    public static String durabilityColor = "#555555";
    @Entry(name = "Food Values Color", width = 7, min = 7, isColor = true)
    public static String foodValuesColor = "#555555";
    @Entry(name = "Compostable Color", width = 7, min = 7, isColor = true)
    public static String compostableColor = "#555555";
    @Entry(name = "Burn Time Color", width = 7, min = 7, isColor = true)
    public static String burnTimeColor = "#555555";
    @Entry(name = "Use Cooldown Color", width = 7, min = 7, isColor = true)
    public static String useCooldownColor = "#555555";
    @Entry(name = "Enchantability Color", width = 7, min = 7, isColor = true)
    public static String enchantabilityColor = "#555555";
    @Entry(name = "Repair Cost Color", width = 7, min = 7, isColor = true)
    public static String repairCostColor = "#555555";
    @Entry(name = "Strength Color", width = 7, min = 7, isColor = true)
    public static String strengthColor = "#555555";
    @Entry(name = "Enchantment Power Color", width = 7, min = 7, isColor = true)
    public static String enchantmentPowerColor = "#555555";
    @Entry(name = "Mining Level Color", width = 7, min = 7, isColor = true)
    public static String miningLevelColor = "#555555";
    @Entry(name = "Mining Speed Color", width = 7, min = 7, isColor = true)
    public static String miningSpeedColor = "#555555";
    @Entry(name = "Mod Name Color", width = 7, min = 7, isColor = true)
    public static String modNameColor = "#5555ff";
}
