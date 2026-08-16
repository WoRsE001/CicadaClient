package cicada.client.mixin;

import cicada.client.utils.client.MinecraftExtensionsKt;
import net.minecraft.client.DeltaTracker;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DeltaTracker.Timer.class)
public abstract class MixinDeltaTracker$Timer {

    @Shadow
    private float deltaTicks;

    @Inject(method = "advanceGameTime(J)I", at = @At(value = "FIELD", target = "Lnet/minecraft/client/DeltaTracker$Timer;deltaTicks:F", shift = At.Shift.AFTER, opcode = Opcodes.PUTFIELD))
    private void hookTimer(CallbackInfoReturnable<Integer> callback) {
        float customTimer = MinecraftExtensionsKt.getGameSpeed(MinecraftExtensionsKt.getMc());
        if (customTimer > 0) {
            deltaTicks *= customTimer;
        }
    }
}
