package cicada.client.mixin.accessors;

import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.state.GameRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(GameRenderer.class)
public interface AccessorGameRenderer {
    @Accessor("gameRenderState")
    GameRenderState gameRenderState();
}
