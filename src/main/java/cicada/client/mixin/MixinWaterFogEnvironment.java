package cicada.client.mixin;

import cicada.client.module.impl.visual.ModuleNoRender;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.fog.FogData;
import net.minecraft.client.renderer.fog.environment.WaterFogEnvironment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// SCWGxD regrets everything he did. 17.04.2026 8:34.
@Mixin(WaterFogEnvironment.class)
public class MixinWaterFogEnvironment {
    @Inject(method = "setupFog", at = @At("HEAD"), cancellable = true)
    public void cancelSetupFog(FogData fog, Camera camera, ClientLevel level, float renderDistance, DeltaTracker deltaTracker, CallbackInfo ci) {
        if (ModuleNoRender.INSTANCE.getToggled() && ModuleNoRender.INSTANCE.getWaterFog()) {
            ci.cancel();
        }
    }
}
