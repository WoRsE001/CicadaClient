package cicada.client.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import cicada.client.module.impl.visual.ModuleNoRender;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

// SCWGxD regrets everything he did. 17.04.2026 8:52.
@Mixin(ScreenEffectRenderer.class)
public class MixinScreenEffectRenderer {
    @ModifyExpressionValue(method = "renderScreenEffect", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isOnFire()Z"))
    private boolean noRenderFireOverlay(boolean original) {
        return original && (!ModuleNoRender.INSTANCE.getToggled() || !ModuleNoRender.INSTANCE.getFireOverlay());
    }
}
