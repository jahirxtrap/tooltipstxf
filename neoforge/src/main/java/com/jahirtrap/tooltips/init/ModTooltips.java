package com.jahirtrap.tooltips.init;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.component.TypedDataComponent;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.RegistryOps;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ComposterBlock;
import net.neoforged.fml.ModList;

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
            float compostable = ComposterBlock.getValue(stack);
            if (compostable > -1 && compostable != 0) {
                Component compostableTooltip = Component.translatable("tooltipstxf.tooltip.compostable", formatText(compostable * 100)).append("%").withColor(getColor(0x555555, ModConfig.compostableColor));
                list.add(compostableTooltip);
            }
        }

        if (ModConfig.showBurnTime && player != null) {
            try {
                int burnTime = stack.getBurnTime(null, player.level().fuelValues());
                if (burnTime > -1 && burnTime != 0) {
                    Component burnTimeTooltip;
                    if (ModConfig.timeInSeconds)
                        burnTimeTooltip = Component.translatable("tooltipstxf.tooltip.burn_time.seconds", formatText(burnTime / 20f));
                    else
                        burnTimeTooltip = Component.translatable("tooltipstxf.tooltip.burn_time", burnTime);
                    list.add(burnTimeTooltip.copy().withColor(getColor(0x555555, ModConfig.burnTimeColor)));
                }
            } catch (Exception ignored) {
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

        if (ModConfig.showSongDuration) {
            var jukebox = stack.get(DataComponents.JUKEBOX_PLAYABLE);
            if (jukebox != null) {
                var song = jukebox.song().value();
                Component songDurationTooltip = Component.translatable("tooltipstxf.tooltip.song_duration", formatText(song.lengthInSeconds()));
                list.add(songDurationTooltip.copy().withColor(getColor(0x555555, ModConfig.songDurationColor)));
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

        var tool = stack.get(DataComponents.TOOL);
        if (tool != null) {
            if (ModConfig.showMiningLevel && player != null) {
                int miningLevel = getMiningLevel(tool, player.level());
                if (miningLevel >= 0) {
                    Component miningLevelTooltip = Component.translatable("tooltipstxf.tooltip.mining_level", miningLevel).withColor(getColor(0x555555, ModConfig.miningLevelColor));
                    list.add(miningLevelTooltip);
                }
            }

            if (ModConfig.showMiningSpeed) {
                float miningSpeed = getMiningSpeed(tool);
                if (miningSpeed >= 0) {
                    Component miningSpeedTooltip = Component.translatable("tooltipstxf.tooltip.mining_speed", formatText(miningSpeed)).withColor(getColor(0x555555, ModConfig.miningSpeedColor));
                    list.add(miningSpeedTooltip);
                }
            }
        }

        if (ModConfig.showModName) {
            String modId = BuiltInRegistries.ITEM.getKey(stack.getItem()).getNamespace();
            Component modNameTooltip = Component.literal(ModList.get().getModContainerById(modId).map(container -> container.getModInfo().getDisplayName()).orElse(modId)).withStyle(ChatFormatting.ITALIC).withColor(getColor(0x5555ff, ModConfig.modNameColor));
            list.add(modNameTooltip);
        }

        if (ModConfig.showComponents) {
            if (hasControlDown()) {
                list.add(Component.translatable("tooltipstxf.tooltip.components").withColor(getColor(0x555555, ModConfig.componentsColors, 0)));
                var registries = context.registries();
                RegistryOps<Tag> ops = registries != null ? registries.createSerializationContext(NbtOps.INSTANCE) : null;
                for (TypedDataComponent<?> component : stack.getComponents()) {
                    var key = BuiltInRegistries.DATA_COMPONENT_TYPE.getKey(component.type());
                    String id = key != null ? key.toString() : component.type().toString();
                    list.add(Component.literal("  ").append(Component.literal(id + ": ").withColor(getColor(0xAAAAAA, ModConfig.componentsColors, 2))).append(Component.literal(componentValue(component, ops)).withColor(getColor(0x555555, ModConfig.componentsColors, 3))));
                }
            } else {
                list.add(Component.translatable("tooltipstxf.tooltip.components").withColor(getColor(0x555555, ModConfig.componentsColors, 0)).append(Component.literal(" ")).append(Component.literal("CTRL").withColor(getColor(0x55FFFF, ModConfig.componentsColors, 1))));
            }
        }
    }

    private static boolean hasControlDown() {
        var window = Minecraft.getInstance().getWindow();
        return InputConstants.isKeyDown(window, InputConstants.KEY_LCONTROL) || InputConstants.isKeyDown(window, InputConstants.KEY_RCONTROL);
    }

    private static String componentValue(TypedDataComponent<?> component, RegistryOps<Tag> ops) {
        if (ops != null) {
            var result = component.encodeValue(ops).result();
            if (result.isPresent()) return result.get().toString();
        }
        return String.valueOf(component.value());
    }

    private static String formatText(double value) {
        if (value % 1 == 0) return String.valueOf((int) value);
        else return String.format("%.2f", value).replaceAll("0*$", "").replaceAll("\\.$", "");
    }

    private static float getEnchantPowerBonus(Level level, Block block) {
        return block.defaultBlockState().getEnchantPowerBonus(level, BlockPos.ZERO);
    }

    public static int getMiningLevel(Tool tool, Level level) {
        if (level.registryAccess().lookupOrThrow(Registries.BLOCK).get(ModTags.Blocks.NEEDS_NETHERITE_TOOL).map(set -> set.stream().anyMatch(h -> tool.isCorrectForDrops(h.value().defaultBlockState()))).orElse(false)) {
            return 4;
        } else if (tool.isCorrectForDrops(Blocks.ANCIENT_DEBRIS.defaultBlockState())) {
            return 3;
        } else if (tool.isCorrectForDrops(Blocks.DIAMOND_ORE.defaultBlockState())) {
            return 2;
        } else if (tool.isCorrectForDrops(Blocks.IRON_ORE.defaultBlockState())) {
            return 1;
        } else if (tool.isCorrectForDrops(Blocks.STONE.defaultBlockState())) {
            return 0;
        } else return -1;
    }

    public static float getMiningSpeed(Tool tool) {
        for (Tool.Rule rule : tool.rules()) {
            if (rule.speed().isPresent()) return rule.speed().get();
        }
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
