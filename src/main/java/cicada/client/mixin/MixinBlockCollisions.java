package cicada.client.mixin;

import cicada.client.event.impl.EventBlockShape;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockCollisions;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

// SCWGxD regrets everything he did. 24.06.2026 14:51.
@Mixin(BlockCollisions.class)
public class MixinBlockCollisions {
    @Shadow
    @org.spongepowered.asm.mixin.Final
    private BlockPos.MutableBlockPos pos;

    @ModifyExpressionValue(
        method = "computeNext",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/phys/shapes/CollisionContext;getCollisionShape(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/CollisionGetter;Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/phys/shapes/VoxelShape;"
        )
    )
    private VoxelShape callBlockShapeEvent(VoxelShape original, @Local BlockState blockState) {
        EventBlockShape event = EventBlockShape.INSTANCE;
        event.setState(blockState);
        event.setPos(this.pos.immutable());
        event.setShape(original);
        event.call();
        return event.getShape();
    }
}