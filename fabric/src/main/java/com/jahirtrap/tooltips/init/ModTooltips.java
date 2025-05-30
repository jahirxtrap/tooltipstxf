package com.jahirtrap.tooltips.init;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ComposterBlock;

import java.util.List;

public class ModTooltips {
    public static void init(ItemStack stack, Player player, Item.TooltipContext context, List<Component> list, TooltipFlag flag) {
        if (!ModConfig.enableMod) return;

        if (ModConfig.showDurability) {
            if ((!flag.isAdvanced() || (flag.isAdvanced() && !stack.isDamaged())) && stack.getMaxDamage() != 0) {
                Component durabilityTooltip = Component.translatable("item.durability", stack.getMaxDamage() - stack.getDamageValue(), stack.getMaxDamage());
                list.add(durabilityTooltip.copy().withColor(getColor(0x555555, ModConfig.durabilityColor)));
            }
        }

        if (ModConfig.showFoodValues) {
            var foodProperties = stack.get(DataComponents.FOOD);
            if (foodProperties != null) {
                Component foodTooltip = Component.empty();
                if (foodProperties.nutrition() != 0 && foodProperties.saturation() != 0)
                    foodTooltip = Component.translatable("tooltipstxf.tooltip.nutrition", foodProperties.nutrition()).append(" ").append(Component.translatable("tooltipstxf.tooltip.saturation", formatText(foodProperties.saturation())));
                else if (foodProperties.nutrition() != 0)
                    foodTooltip = Component.translatable("tooltipstxf.tooltip.nutrition", foodProperties.nutrition());
                else if (foodProperties.saturation() != 0)
                    foodTooltip = Component.translatable("tooltipstxf.tooltip.saturation", formatText(foodProperties.saturation()));
                if (!foodTooltip.equals(Component.empty()))
                    list.add(foodTooltip.copy().withColor(getColor(0x555555, ModConfig.foodValuesColor)));
            }
        }

        if (ModConfig.showCompostable) {
            float compostable = ComposterBlock.COMPOSTABLES.getOrDefault(stack.getItem(), 0);
            if (compostable != 0) {
                Component compostableTooltip = Component.translatable("tooltipstxf.tooltip.compostable", formatText(compostable * 100)).append("%").withColor(getColor(0x555555, ModConfig.compostableColor));
                list.add(compostableTooltip);
            }
        }

        if (ModConfig.showBurnTime && player != null) {
            int burnTime = player.level().fuelValues().burnDuration(stack);
            if (burnTime != 0) {
                Component burnTimeTooltip;
                if (ModConfig.timeInSeconds)
                    burnTimeTooltip = Component.translatable("tooltipstxf.tooltip.burn_time.seconds", formatText(burnTime / 20f));
                else
                    burnTimeTooltip = Component.translatable("tooltipstxf.tooltip.burn_time", burnTime);
                list.add(burnTimeTooltip.copy().withColor(getColor(0x555555, ModConfig.burnTimeColor)));
            }
        }

        if (ModConfig.showUseCooldown) {
            var cooldown = stack.get(DataComponents.USE_COOLDOWN);
            if (cooldown != null && cooldown.seconds() != 0) {
                Component cooldownComponent;
                if (ModConfig.timeInSeconds)
                    cooldownComponent = Component.translatable("tooltipstxf.tooltip.use_cooldown.seconds", formatText(cooldown.seconds()));
                else
                    cooldownComponent = Component.translatable("tooltipstxf.tooltip.use_cooldown", cooldown.ticks());
                list.add(cooldownComponent.copy().withColor(getColor(0x555555, ModConfig.useCooldownColor)));
            }
        }

        if (ModConfig.showEnchantability) {
            var enchantable = stack.get(DataComponents.ENCHANTABLE);
            if (enchantable != null && enchantable.value() != 0) {
                Component enchantabilityComponent = Component.translatable("tooltipstxf.tooltip.enchantability", enchantable.value()).withColor(getColor(0x555555, ModConfig.enchantabilityColor));
                list.add(enchantabilityComponent);
            }
        }

        if (ModConfig.showRepairCost) {
            int repairCost = stack.getComponents().getOrDefault(DataComponents.REPAIR_COST, 0);
            if (repairCost != 0)
                list.add(Component.translatable("tooltipstxf.tooltip.repair_cost", repairCost + 1).withColor(getColor(0x555555, ModConfig.repairCostColor)));
        }

        var block = Block.byItem(stack.getItem());
        if (block != Blocks.AIR) {
            if (ModConfig.showStrength) {
                Component strengthTooltip = Component.empty();
                if (block.defaultDestroyTime() != 0 && block.getExplosionResistance() != 0)
                    strengthTooltip = Component.translatable("tooltipstxf.tooltip.hardness", formatText(block.defaultDestroyTime())).append(" ").append(Component.translatable("tooltipstxf.tooltip.resistance", formatText(block.getExplosionResistance())));
                else if (block.defaultDestroyTime() != 0)
                    strengthTooltip = Component.translatable("tooltipstxf.tooltip.hardness", formatText(block.defaultDestroyTime()));
                else if (block.getExplosionResistance() != 0)
                    strengthTooltip = Component.translatable("tooltipstxf.tooltip.resistance", formatText(block.getExplosionResistance()));
                if (!strengthTooltip.equals(Component.empty()))
                    list.add(strengthTooltip.copy().withColor(getColor(0x555555, ModConfig.strengthColor)));
            }

            if (ModConfig.showEnchantmentPower && player != null) {
                float enchantPower = getEnchantPowerBonus(player.level(), block);
                if (enchantPower != 0) {
                    Component enchantPowerTooltip = Component.translatable("tooltipstxf.tooltip.enchantment_power", formatText(enchantPower)).withColor(getColor(0x555555, ModConfig.enchantmentPowerColor));
                    list.add(enchantPowerTooltip);
                }
            }
        }

        if (ModConfig.showModName) {
            String modId = BuiltInRegistries.ITEM.getKey(stack.getItem()).getNamespace();
            Component modNameTooltip = Component.literal(FabricLoader.getInstance().getModContainer(modId).map(container -> container.getMetadata().getName()).orElse(modId)).withStyle(ChatFormatting.ITALIC).withColor(getColor(0x5555ff, ModConfig.modNameColor));
            list.add(modNameTooltip);
        }
    }

    private static String formatText(double value) {
        if (value % 1 == 0) return String.valueOf((int) value);
        else return String.format("%.2f", value).replaceAll("0*$", "").replaceAll("\\.$", "");
    }

    private static float getEnchantPowerBonus(Level level, Block block) {
        return block.defaultBlockState().is(BlockTags.ENCHANTMENT_POWER_PROVIDER) ? 1 : 0;
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
