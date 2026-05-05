package cicada.client.mixin;

import cicada.client.rotation.CameraRotation;
import cicada.client.utils.MinecraftExtensionsKt;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class MixinEntity {
    @Shadow
    private float xRot;

    @Shadow
    private float yRot;

    @Inject(at = @At("HEAD"), method = "turn", cancellable = true)
    private void unlockCamera(double cursorDeltaX, double cursorDeltaY, CallbackInfo ci) {
        if ((Object) this != MinecraftExtensionsKt.getPlayer())
            return;

        float pitchDelta = (float)cursorDeltaY * 0.15F;
        float yawDelta = (float)cursorDeltaX * 0.15F;

        CameraRotation rotation = CameraRotation.INSTANCE;

        if (rotation.getUnlocked()) {
            rotation.setX(CameraRotation.INSTANCE.getX() + pitchDelta);
            rotation.setY(CameraRotation.INSTANCE.getY() + yawDelta);
            rotation.clampX(90f);
            ci.cancel();
        }
    }

    @Inject(at = @At("TAIL"), method = "turn")
    private void setCameraRotation(double cursorDeltaX, double cursorDeltaY, CallbackInfo ci) {
        CameraRotation rotation = CameraRotation.INSTANCE;

        if (!rotation.getUnlocked()) {
            rotation.setX(xRot);
            rotation.setY(rotation.getY() + Mth.wrapDegrees(yRot - rotation.getY()));
        }
    }
}
