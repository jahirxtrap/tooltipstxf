package com.jahirtrap.tooltips.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class ModTags {
    public interface Blocks {
        TagKey<Block> NEEDS_NETHERITE_TOOL = create(new ResourceLocation("needs_netherite_tool"));

        private static TagKey<Block> create(ResourceLocation name) {
            return TagKey.create(Registries.BLOCK, name);
        }
    }
}
