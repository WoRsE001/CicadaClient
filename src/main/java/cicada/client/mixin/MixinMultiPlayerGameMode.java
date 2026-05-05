package cicada.client.mixin;

import cicada.client.event.impl.AttackEvent;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MultiPlayerGameMode.class)
public class MixinMultiPlayerGameMode {
    @Inject(at = @At("HEAD"), method = "attack")
    private void callAttackEvent$Pre(Player player, Entity entity, CallbackInfo ci) {
        AttackEvent.Pre event = AttackEvent.Pre.INSTANCE;
        event.setPlayer(player);
        event.setTarget(entity);
        event.call();
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/MultiPlayerGameMode;ensureHasSentCarriedItem()V", shift = At.Shift.AFTER), method = "attack")
    private void callAttackEvent$BeforeAttackPacket(Player player, Entity entity, CallbackInfo ci) {
        AttackEvent.BeforeAttackPacket event = AttackEvent.BeforeAttackPacket.INSTANCE;
        event.setPlayer(player);
        event.setTarget(entity);
        event.call();
    }

    @Inject(at = @At(value = "TAIL"), method = "attack")
    private void callAttackEvent$Post(Player player, Entity entity, CallbackInfo ci) {
        AttackEvent.Post event = AttackEvent.Post.INSTANCE;
        event.setPlayer(player);
        event.setTarget(entity);
        event.call();
    }
}
