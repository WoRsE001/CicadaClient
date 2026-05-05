package cicada.client.mixin;

import cicada.client.event.impl.SlowDownEvent;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// SCWGxD regrets everything he did. 17.04.2026 15:25.
@Mixin(Player.class)
public class MixinPlayer {
    @Unique
    boolean lastSprint = false;

    @Inject(method = "causeExtraKnockback", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;setSprinting(Z)V", shift = At.Shift.BEFORE))
    private void setLastSprint(Entity entity, float knockbackAmount, Vec3 oldMovement, CallbackInfo ci) {
        Player player = (Player) (Object) this;
        if (!(player instanceof LocalPlayer)) return;

        lastSprint = player.isSprinting();
    }

    @Inject(method = "causeExtraKnockback", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;setSprinting(Z)V", shift = At.Shift.AFTER))
    private void callSlowDownEvent(Entity entity, float knockbackAmount, Vec3 oldMovement, CallbackInfo ci) {
        Player player = (Player) (Object) this;
        if (!(player instanceof LocalPlayer)) return;

        Vec3 motion = player.getDeltaMovement().multiply(1 / 0.6, 1, 1 / 0.6);

        SlowDownEvent.INSTANCE.setType(SlowDownEvent.Type.Hit);
        SlowDownEvent.INSTANCE.setSprint(false);
        SlowDownEvent.INSTANCE.setSlowDown(0.6f);
        SlowDownEvent.INSTANCE.call();
        player.setDeltaMovement(motion.multiply(
                SlowDownEvent.INSTANCE.getSlowDown(),
                1.0,
                SlowDownEvent.INSTANCE.getSlowDown()
        ));
        player.setSprinting(lastSprint && SlowDownEvent.INSTANCE.getSprint());
    }
}
