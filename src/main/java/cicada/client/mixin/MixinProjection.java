package cicada.client.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import cicada.client.module.impl.visual.ModuleAspectRatio;
import net.minecraft.client.renderer.Projection;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

// SCWGxD regrets everything he did. 02.04.2026 4:05.
@Mixin(Projection.class)
public class MixinProjection {
    @ModifyExpressionValue(method = "getMatrix", at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/Projection;width:F", ordinal = 0, opcode = Opcodes.GETFIELD))
    private float aspectRatio(float original) {
        if (ModuleAspectRatio.INSTANCE.getToggled()) {
            return original * ModuleAspectRatio.INSTANCE.getFactor();
        }

        return original;
    }
}
