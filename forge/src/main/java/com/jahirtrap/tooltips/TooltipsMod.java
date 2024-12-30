package com.jahirtrap.tooltips;

import com.jahirtrap.configlib.TXFConfig;
import com.jahirtrap.tooltips.init.ModConfig;
import net.minecraftforge.fml.common.Mod;

@Mod(TooltipsMod.MODID)
public class TooltipsMod {

    public static final String MODID = "tooltipstxf";

    public TooltipsMod() {
        TXFConfig.init(MODID, ModConfig.class);
    }
}
