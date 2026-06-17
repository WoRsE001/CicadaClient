package cicada.client.mixin.accessors;

import net.minecraft.world.level.block.state.BlockBehaviour;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

// SCWGxD regrets everything he did. 10.06.2026 8:35.
@Mixin(BlockBehaviour.class)
public interface AccessorBlockBehaviour {
    @Accessor("hasCollision")
    boolean getHasCollision();
}
