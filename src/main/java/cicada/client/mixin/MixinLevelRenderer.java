package cicada.client.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.resource.ResourceHandle;
import com.mojang.blaze3d.vertex.PoseStack;
import cicada.client.event.impl.RenderEvent;
import cicada.client.utils.MinecraftExtensionsKt;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.chunk.ChunkSectionsToRender;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.util.profiling.ProfilerFiller;
import org.joml.Matrix4fc;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// SCWGxD regrets everything he did. 04.04.2026 11:23.
@Mixin(LevelRenderer.class)
public class MixinLevelRenderer {
    @SuppressWarnings( "rawtypes" )
    @Inject(method = "lambda$addMainPass$0", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/MultiBufferSource$BufferSource;endBatch()V", ordinal = 6, shift = At.Shift.AFTER))
    private void callRender3DEvent(
            GpuBufferSlice terrainFog,
            LevelRenderState levelRenderState,
            ProfilerFiller profiler,
            ChunkSectionsToRender chunkSectionsToRender,
            ResourceHandle entityOutlineTarget,
            ResourceHandle translucentTarget,
            ResourceHandle mainTarget,
            ResourceHandle itemEntityTarget,
            ResourceHandle particleTarget,
            boolean renderOutline,
            Matrix4fc modelViewMatrix,
            CallbackInfo ci,
            @Local(name="poseStack") PoseStack poseStack,
            @Local(name="bufferSource") MultiBufferSource.BufferSource bufferSource
    ) {
        RenderEvent.World.INSTANCE.setPoseStack(poseStack);
        RenderEvent.World.INSTANCE.setBufferSource(bufferSource);
        RenderEvent.World.INSTANCE.setDeltaTracker(MinecraftExtensionsKt.getMc().getDeltaTracker());
        RenderEvent.World.INSTANCE.call();
    }
}
