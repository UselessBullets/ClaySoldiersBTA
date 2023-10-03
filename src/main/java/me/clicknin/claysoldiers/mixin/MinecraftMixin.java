package me.clicknin.claysoldiers.mixin;

import me.clicknin.claysoldiers.ClaySoldiers;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Minecraft.class, remap = false)
public class MinecraftMixin {
    @Shadow
    private static Minecraft theMinecraft;
    @Inject(method = "getMinecraft(Ljava/lang/Class;)Lnet/minecraft/client/Minecraft;", at = @At("HEAD"), cancellable = true)
    private static void getMinecraft(Class<?> caller, CallbackInfoReturnable<Minecraft> cir){
        cir.setReturnValue(theMinecraft);
    }
    @Inject(method = "run", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;runTick()V", shift = At.Shift.AFTER))
    private void addWaveTime(CallbackInfo ci) {
        ClaySoldiers.updateWaveTime();
    }
}
