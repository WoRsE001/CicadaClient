package cicada.client.mixin.accessors;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

// SCWGxD regrets everything he did. 09.04.2026 8:34.
@Mixin(LocalPlayer.class)
public interface AccessorLocalPlayer {
    @Invoker("pick")
    HitResult invokePick(
            Entity cameraEntity,
            double blockInteractionRange,
            double entityInteractionRange,
            float partialTicks
    );

    @Invoker("filterHitResult")
    HitResult invokeFilterHitResult(HitResult hitResult, Vec3 from, double maxRange);
}
