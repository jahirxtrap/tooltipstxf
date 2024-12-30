package com.jahirtrap.tooltips;

import com.jahirtrap.configlib.TXFConfig;
import com.jahirtrap.tooltips.init.ModConfig;
import net.fabricmc.api.ModInitializer;

public class TooltipsMod implements ModInitializer {

    public static final String MODID = "tooltipstxf";

    @Override
    public void onInitialize() {
        TXFConfig.init(MODID, ModConfig.class);
    }
}
