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
    @Entry(name = "Show Mod Name", itemDisplay = "minecraft:writable_book")
    public static boolean showModName = false;
}
