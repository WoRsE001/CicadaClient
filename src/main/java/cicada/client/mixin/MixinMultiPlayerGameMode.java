package cicada.client.mixin;

import cicada.client.event.events.EventAttack;
import cicada.client.feature.module.modules.world.ModuleFastBreak;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MultiPlayerGameMode.class)
public class MixinMultiPlayerGameMode {
    @Inject(at = @At("HEAD"), method = "attack", cancellable = true)
    private void callAttackEvent$Pre(Player player, Entity entity, CallbackInfo ci) {
        EventAttack.Pre event = EventAttack.Pre.INSTANCE;
        event.setPlayer(player);
        event.setTarget(entity);
        event.call();
        if (event.getCanceled()) ci.cancel();
    }

    @ModifyExpressionValue(method = "startDestroyBlock", at = @At(value = "CONSTANT", args = "intValue=5"))
    private int noDestroyDelay(int original) {
        ModuleFastBreak fastBreak = ModuleFastBreak.INSTANCE;

        if (fastBreak.shouldFastBreak()) {
            return fastBreak.getBreakDelay();
        }

        return original;
    }
}
