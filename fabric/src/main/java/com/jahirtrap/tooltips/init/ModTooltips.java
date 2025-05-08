package com.jahirtrap.tooltips.init;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;

import java.util.List;

public class ModTooltips {
    public static void init(ItemStack stack, Player player, List<Component> list, TooltipFlag flag) {
        if (!ModConfig.enableMod) return;

        if (ModConfig.showDurability) {
            if ((!flag.isAdvanced() || (flag.isAdvanced() && !stack.isDamaged())) && stack.getMaxDamage() != 0) {
                Component durabilityTooltip = new TranslatableComponent("item.durability", stack.getMaxDamage() - stack.getDamageValue(), stack.getMaxDamage());
                list.add(durabilityTooltip.copy().withStyle(s -> s.withColor(getColor(0x555555, ModConfig.durabilityColor))));
            }
        }

        if (ModConfig.showFoodValues) {
            var foodProperties = stack.getItem().getFoodProperties();
            if (stack.isEdible() && foodProperties != null) {
                Component foodTooltip = null;
                int nutrition = foodProperties.getNutrition();
                float saturation = nutrition * foodProperties.getSaturationModifier() * 2;
                if (nutrition != 0 && saturation != 0)
                    foodTooltip = new TranslatableComponent("tooltipstxf.tooltip.nutrition", nutrition).append(" ").append(new TranslatableComponent("tooltipstxf.tooltip.saturation", formatText(saturation)));
                else if (nutrition != 0)
                    foodTooltip = new TranslatableComponent("tooltipstxf.tooltip.nutrition", nutrition);
                else if (saturation != 0)
                    foodTooltip = new TranslatableComponent("tooltipstxf.tooltip.saturation", formatText(saturation));
                if (foodTooltip != null)
                    list.add(foodTooltip.copy().withStyle(s -> s.withColor(getColor(0x555555, ModConfig.foodValuesColor))));
            }
        }

        if (ModConfig.showCompostable) {
            float compostable = ComposterBlock.COMPOSTABLES.getOrDefault(stack.getItem(), 0);
            if (compostable != 0) {
                Component compostableTooltip = new TranslatableComponent("tooltipstxf.tooltip.compostable", formatText(compostable * 100)).append("%").withStyle(s -> s.withColor(getColor(0x555555, ModConfig.compostableColor)));
                list.add(compostableTooltip);
            }
        }

        if (ModConfig.showBurnTime) {
            int burnTime = AbstractFurnaceBlockEntity.getFuel().getOrDefault(stack.getItem(), 0);
            if (burnTime != 0) {
                Component burnTimeTooltip;
                if (ModConfig.timeInSeconds)
                    burnTimeTooltip = new TranslatableComponent("tooltipstxf.tooltip.burn_time.seconds", formatText(burnTime / 20f));
                else
                    burnTimeTooltip = new TranslatableComponent("tooltipstxf.tooltip.burn_time", burnTime);
                list.add(burnTimeTooltip.copy().withStyle(s -> s.withColor(getColor(0x555555, ModConfig.burnTimeColor))));
            }
        }

        if (ModConfig.showUseCooldown && player != null) {
            var cooldown = player.getCooldowns().cooldowns.get(stack.getItem());
            if (cooldown != null) {
                Component cooldownComponent;
                if (ModConfig.timeInSeconds)
                    cooldownComponent = new TranslatableComponent("tooltipstxf.tooltip.use_cooldown.seconds", formatText((cooldown.endTime - cooldown.startTime) / 20f));
                else
                    cooldownComponent = new TranslatableComponent("tooltipstxf.tooltip.use_cooldown", cooldown.endTime - cooldown.startTime);
                list.add(cooldownComponent.copy().withStyle(s -> s.withColor(getColor(0x555555, ModConfig.useCooldownColor))));
            }
        }

        if (ModConfig.showEnchantability) {
            int enchantable = stack.getItem().getEnchantmentValue();
            if (enchantable != 0) {
                Component enchantabilityComponent = new TranslatableComponent("tooltipstxf.tooltip.enchantability", enchantable).withStyle(s -> s.withColor(getColor(0x555555, ModConfig.enchantabilityColor)));
                list.add(enchantabilityComponent);
            }
        }

        if (ModConfig.showRepairCost) {
            int repairCost = stack.getBaseRepairCost();
            if (repairCost != 0)
                list.add(new TranslatableComponent("tooltipstxf.tooltip.repair_cost", repairCost + 1).withStyle(s -> s.withColor(getColor(0x555555, ModConfig.repairCostColor))));
        }

        var block = Block.byItem(stack.getItem());
        if (block != Blocks.AIR) {
            if (ModConfig.showStrength) {
                Component strengthTooltip = null;
                if (block.defaultDestroyTime() != 0 && block.getExplosionResistance() != 0)
                    strengthTooltip = new TranslatableComponent("tooltipstxf.tooltip.hardness", formatText(block.defaultDestroyTime())).append(" ").append(new TranslatableComponent("tooltipstxf.tooltip.resistance", formatText(block.getExplosionResistance())));
                else if (block.defaultDestroyTime() != 0)
                    strengthTooltip = new TranslatableComponent("tooltipstxf.tooltip.hardness", formatText(block.defaultDestroyTime()));
                else if (block.getExplosionResistance() != 0)
                    strengthTooltip = new TranslatableComponent("tooltipstxf.tooltip.resistance", formatText(block.getExplosionResistance()));
                if (strengthTooltip != null)
                    list.add(strengthTooltip.copy().withStyle(s -> s.withColor(getColor(0x555555, ModConfig.strengthColor))));
            }

            if (ModConfig.showEnchantmentPower && player != null) {
                float enchantPower = getEnchantPowerBonus(player.getLevel(), block);
                if (enchantPower != 0) {
                    Component enchantPowerTooltip = new TranslatableComponent("tooltipstxf.tooltip.enchantment_power", formatText(enchantPower)).withStyle(s -> s.withColor(getColor(0x555555, ModConfig.enchantmentPowerColor)));
                    list.add(enchantPowerTooltip);
                }
            }
        }

        if (ModConfig.showModName) {
            String modId = Registry.ITEM.getKey(stack.getItem()).getNamespace();
            Component modNameTooltip = new TextComponent(FabricLoader.getInstance().getModContainer(modId).map(container -> container.getMetadata().getName()).orElse(modId)).withStyle(ChatFormatting.ITALIC).withStyle(s -> s.withColor(getColor(0x5555ff, ModConfig.modNameColor)));
            list.add(modNameTooltip);
        }
    }

    private static String formatText(double value) {
        if (value % 1 == 0) return String.valueOf((int) value);
        else return String.format("%.2f", value).replaceAll("0*$", "").replaceAll("\\.$", "");
    }

    private static float getEnchantPowerBonus(Level level, Block block) {
        return block.defaultBlockState().is(Blocks.BOOKSHELF) ? 1 : 0;
    }

    private static Integer getColor(int defaultValue, String hexColor) {
        int color = defaultValue;
        try {
            if (hexColor.startsWith("#")) hexColor = hexColor.substring(1);
            color = Integer.parseInt(hexColor, 16);
        } catch (Exception ignore) {
        }
        return color;
    }
}
