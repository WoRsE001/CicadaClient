package cicada.client.mixin;

import cicada.client.module.impl.visual.ModuleNoRender;
import net.minecraft.client.renderer.fog.environment.DarknessFogEnvironment;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// SCWGxD regrets everything he did. 02.04.2026 3:26.
@Mixin(DarknessFogEnvironment.class)
public class MixinDarknessFogEnvironment {
    @Inject(method = "getMobEffect()Lnet/minecraft/core/Holder;", at = @At("HEAD"), cancellable = true)
    public void hookGetStatusEffect(CallbackInfoReturnable<Holder<MobEffect>> cir) {
        if (ModuleNoRender.INSTANCE.getToggled() && ModuleNoRender.INSTANCE.getDarknessFog()) {
            cir.setReturnValue(null);
        }
    }
}
