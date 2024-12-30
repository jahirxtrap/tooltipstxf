package com.jahirtrap.tooltips.init.mixin;

import com.jahirtrap.tooltips.init.ModTooltips;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @Inject(method = "getTooltipLines", at = @At(value = "RETURN"))
    private void getTooltipLines(Player player, TooltipFlag flag, CallbackInfoReturnable<List<Component>> cir) {
        ModTooltips.init((ItemStack) (Object) this, player, cir.getReturnValue(), flag);
    }
}
