package com.jahirtrap.tooltips.init;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.RecordItem;
import net.minecraft.world.item.TieredItem;
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
                Component durabilityTooltip = Component.translatable("item.durability", stack.getMaxDamage() - stack.getDamageValue(), stack.getMaxDamage());
                list.add(durabilityTooltip.copy().withStyle(s -> s.withColor(getColor(0x555555, ModConfig.durabilityColor))));
            }
        }

        if (ModConfig.showFoodValues) {
            var foodProperties = stack.getItem().getFoodProperties();
            if (stack.isEdible() && foodProperties != null) {
                Component foodTooltip = Component.empty();
                int nutrition = foodProperties.getNutrition();
                float saturation = nutrition * foodProperties.getSaturationModifier() * 2;
                if (nutrition != 0 && saturation != 0)
                    foodTooltip = Component.translatable("tooltipstxf.tooltip.nutrition", nutrition).append(" ").append(Component.translatable("tooltipstxf.tooltip.saturation", formatText(saturation)));
                else if (nutrition != 0)
                    foodTooltip = Component.translatable("tooltipstxf.tooltip.nutrition", nutrition);
                else if (saturation != 0)
                    foodTooltip = Component.translatable("tooltipstxf.tooltip.saturation", formatText(saturation));
                if (!foodTooltip.equals(Component.empty()))
                    list.add(foodTooltip.copy().withStyle(s -> s.withColor(getColor(0x555555, ModConfig.foodValuesColor))));
            }
        }

        if (ModConfig.showCompostable) {
            float compostable = ComposterBlock.COMPOSTABLES.getOrDefault(stack.getItem(), 0);
            if (compostable != 0) {
                Component compostableTooltip = Component.translatable("tooltipstxf.tooltip.compostable", formatText(compostable * 100)).append("%").withStyle(s -> s.withColor(getColor(0x555555, ModConfig.compostableColor)));
                list.add(compostableTooltip);
            }
        }

        if (ModConfig.showBurnTime) {
            int burnTime = AbstractFurnaceBlockEntity.getFuel().getOrDefault(stack.getItem(), 0);
            if (burnTime != 0) {
                Component burnTimeTooltip;
                if (ModConfig.timeInSeconds)
                    burnTimeTooltip = Component.translatable("tooltipstxf.tooltip.burn_time.seconds", formatText(burnTime / 20f));
                else
                    burnTimeTooltip = Component.translatable("tooltipstxf.tooltip.burn_time", burnTime);
                list.add(burnTimeTooltip.copy().withStyle(s -> s.withColor(getColor(0x555555, ModConfig.burnTimeColor))));
            }
        }

        if (ModConfig.showUseCooldown && player != null) {
            var cooldown = player.getCooldowns().cooldowns.get(stack.getItem());
            if (cooldown != null) {
                Component cooldownComponent;
                if (ModConfig.timeInSeconds)
                    cooldownComponent = Component.translatable("tooltipstxf.tooltip.use_cooldown.seconds", formatText((cooldown.endTime - cooldown.startTime) / 20f));
                else
                    cooldownComponent = Component.translatable("tooltipstxf.tooltip.use_cooldown", cooldown.endTime - cooldown.startTime);
                list.add(cooldownComponent.copy().withStyle(s -> s.withColor(getColor(0x555555, ModConfig.useCooldownColor))));
            }
        }

        if (ModConfig.showSongDuration) {
            float songDuration = getSongLengthInSeconds(stack);
            if (songDuration >= 0) {
                Component songDurationTooltip = Component.translatable("tooltipstxf.tooltip.song_duration", formatText(songDuration));
                list.add(songDurationTooltip.copy().withStyle(s -> s.withColor(getColor(0x555555, ModConfig.songDurationColor))));
            }
        }

        if (ModConfig.showEnchantability) {
            int enchantable = stack.getItem().getEnchantmentValue();
            if (enchantable != 0) {
                Component enchantabilityComponent = Component.translatable("tooltipstxf.tooltip.enchantability", enchantable).withStyle(s -> s.withColor(getColor(0x555555, ModConfig.enchantabilityColor)));
                list.add(enchantabilityComponent);
            }
        }

        if (ModConfig.showRepairCost) {
            int repairCost = stack.getBaseRepairCost();
            if (repairCost != 0)
                list.add(Component.translatable("tooltipstxf.tooltip.repair_cost", repairCost + 1).withStyle(s -> s.withColor(getColor(0x555555, ModConfig.repairCostColor))));
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
                    list.add(strengthTooltip.copy().withStyle(s -> s.withColor(getColor(0x555555, ModConfig.strengthColor))));
            }

            if (ModConfig.showEnchantmentPower && player != null) {
                float enchantPower = getEnchantPowerBonus(player.level(), block);
                if (enchantPower != 0) {
                    Component enchantPowerTooltip = Component.translatable("tooltipstxf.tooltip.enchantment_power", formatText(enchantPower)).withStyle(s -> s.withColor(getColor(0x555555, ModConfig.enchantmentPowerColor)));
                    list.add(enchantPowerTooltip);
                }
            }
        }

        if (stack.getItem() instanceof TieredItem tieredItem) {
            var tier = tieredItem.getTier();
            if (ModConfig.showMiningLevel) {
                Component miningLevelTooltip = Component.translatable("tooltipstxf.tooltip.mining_level", tier.getLevel()).withStyle(s -> s.withColor(getColor(0x555555, ModConfig.miningLevelColor)));
                list.add(miningLevelTooltip);
            }

            if (ModConfig.showMiningSpeed) {
                Component miningSpeedTooltip = Component.translatable("tooltipstxf.tooltip.mining_speed", formatText(tier.getSpeed())).withStyle(s -> s.withColor(getColor(0x555555, ModConfig.miningSpeedColor)));
                list.add(miningSpeedTooltip);
            }
        }

        if (ModConfig.showModName) {
            String modId = BuiltInRegistries.ITEM.getKey(stack.getItem()).getNamespace();
            Component modNameTooltip = Component.literal(FabricLoader.getInstance().getModContainer(modId).map(container -> container.getMetadata().getName()).orElse(modId)).withStyle(ChatFormatting.ITALIC).withStyle(s -> s.withColor(getColor(0x5555ff, ModConfig.modNameColor)));
            list.add(modNameTooltip);
        }

        if (ModConfig.showComponents) {
            CompoundTag tag = stack.getTag();
            if (tag != null && !tag.getAllKeys().isEmpty()) {
                if (Screen.hasControlDown()) {
                    list.add(Component.translatable("tooltipstxf.tooltip.components").withStyle(s -> s.withColor(getColor(0x555555, ModConfig.componentsColors, 0))));
                    for (String key : tag.getAllKeys().stream().sorted().toList()) {
                        Tag value = tag.get(key);
                        list.add(Component.literal("  ").append(Component.literal(key + ": ").withStyle(s -> s.withColor(getColor(0xAAAAAA, ModConfig.componentsColors, 2)))).append(Component.literal(value != null ? value.toString() : "").withStyle(s -> s.withColor(getColor(0x555555, ModConfig.componentsColors, 3)))));
                    }
                } else {
                    list.add(Component.translatable("tooltipstxf.tooltip.components").withStyle(s -> s.withColor(getColor(0x555555, ModConfig.componentsColors, 0))).append(Component.literal(" ")).append(Component.literal("CTRL").withStyle(s -> s.withColor(getColor(0x55FFFF, ModConfig.componentsColors, 1)))));
                }
            }
        }
    }

    private static String formatText(double value) {
        if (value % 1 == 0) return String.valueOf((int) value);
        else return String.format("%.2f", value).replaceAll("0*$", "").replaceAll("\\.$", "");
    }

    private static float getEnchantPowerBonus(Level level, Block block) {
        return block.defaultBlockState().is(BlockTags.ENCHANTMENT_POWER_PROVIDER) ? 1 : 0;
    }

    private static float getSongLengthInSeconds(ItemStack stack) {
        if (stack.getItem() instanceof RecordItem record) return record.getLengthInTicks() / 20f;
        return -1;
    }

    private static Integer getColor(int defaultValue, List<String> colors, int index) {
        return getColor(defaultValue, index >= 0 && index < colors.size() ? colors.get(index) : "");
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
