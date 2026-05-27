package cicada.client.mixin;

import cicada.client.feature.module.modules.visual.ModuleAmbient;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// SCWGxD regrets everything he did. 22.05.2026 10:48.
@Mixin(Level.class)
public class MixinLevel {
    @Inject(method = "getRainLevel", cancellable = true, at = @At("HEAD"))
    private void injectOverrideWeather(float delta, CallbackInfoReturnable<Float> cir) {
        if (!ModuleAmbient.INSTANCE.getToggled() || !ModuleAmbient.INSTANCE.getWeather().getToggled()) return;
        cir.setReturnValue(ModuleAmbient.INSTANCE.getRain());
    }

    @Inject(method = "getThunderLevel", cancellable = true, at = @At("HEAD"))
    private void injectOverrideThunder(float delta, CallbackInfoReturnable<Float> cir) {
        if (!ModuleAmbient.INSTANCE.getToggled() || !ModuleAmbient.INSTANCE.getWeather().getToggled()) return;
        cir.setReturnValue(ModuleAmbient.INSTANCE.getThunder());
    }
}
