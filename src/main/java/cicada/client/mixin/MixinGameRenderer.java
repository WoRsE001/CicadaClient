package cicada.client.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import cicada.client.module.impl.visual.ModuleNoRender;
import cicada.client.utils.math.MathUtilsKt;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.renderer.GameRenderer;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class MixinGameRenderer {
    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ProjectionMatrixBuffer;getBuffer(Lorg/joml/Matrix4f;)Lcom/mojang/blaze3d/buffers/GpuBufferSlice;"), method = "renderLevel")
    private void updateProjectionMatrix(DeltaTracker deltaTracker, CallbackInfo ci, @Local(name = "projectionMatrix") Matrix4f projectionMatrix, @Local(name = "modelViewMatrix") Matrix4fc modelViewMatrix) {
        MathUtilsKt.setLastProjectionMatrix(projectionMatrix);
        MathUtilsKt.setLastModelViewMatrix((Matrix4f) modelViewMatrix);
    }

    @ModifyExpressionValue(method = "bobView", at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/state/level/CameraEntityRenderState;isPlayer:Z"))
    private boolean noScreenBobbing(boolean original) {
        return original || ModuleNoRender.INSTANCE.getToggled() && ModuleNoRender.INSTANCE.getScreenBobbing();
    }
}
