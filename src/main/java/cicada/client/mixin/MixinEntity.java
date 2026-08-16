package cicada.client.mixin;

import cicada.client.event.events.EventPlayerTurn;
import cicada.client.event.events.EventRelativeMove;
import cicada.client.rotation.CameraRotation;
import cicada.client.utils.client.MinecraftExtensionsKt;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
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

        EventPlayerTurn.Pre.INSTANCE.call();

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

    @Inject(method = "turn", at = @At("RETURN"))
    private void kaka(double cursorDeltaX, double cursorDeltaY, CallbackInfo ci) {
        EventPlayerTurn.Post.INSTANCE.call();
    }

    @Inject(at = @At("TAIL"), method = "turn")
    private void setRotation(double cursorDeltaX, double cursorDeltaY, CallbackInfo ci) {
        CameraRotation rotation = CameraRotation.INSTANCE;

        if (!rotation.getUnlocked()) {
            rotation.setX(xRot);
            rotation.setY(rotation.getY() + Mth.wrapDegrees(yRot - rotation.getY()));
        }
    }

    @ModifyExpressionValue(method = "moveRelative", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;getYRot()F"))
    private float callRelativeMoveEvent(float original) {
        EventRelativeMove.INSTANCE.setYaw(original);
        EventRelativeMove.INSTANCE.call();
        return EventRelativeMove.INSTANCE.getYaw();
    }
}
