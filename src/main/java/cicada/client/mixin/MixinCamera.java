package cicada.client.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import cicada.client.module.impl.misc.ModuleFixes;
import cicada.client.rotation.CameraRotation;
import net.minecraft.client.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Camera.class)
public class MixinCamera {
    @ModifyExpressionValue(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;getViewXRot(F)F"), method = "alignWithEntity")
    private float modifyX(float original) {
        return CameraRotation.INSTANCE.getX();
    }

    @ModifyExpressionValue(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;getViewYRot(F)F"), method = "alignWithEntity")
    private float modifyY(float original) {
        return CameraRotation.INSTANCE.getY();
    }
}
