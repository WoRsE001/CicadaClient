package cicada.client.mixin;

import cicada.client.feature.module.modules.visual.ModuleAspectRatio;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import cicada.client.rotation.CameraRotation;
import net.minecraft.client.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

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

    @ModifyArgs(method = "createProjectionMatrixForCulling", at = @At(value = "INVOKE", target = "Lorg/joml/Matrix4f;perspective(FFFFZ)Lorg/joml/Matrix4f;", remap = false))
    private void hookBasicProjectionMatrix(Args args) {
        if (ModuleAspectRatio.INSTANCE.getToggled()) {
            args.set(1, (float) args.get(1) / ModuleAspectRatio.INSTANCE.getFactor());
        }
    }
}
