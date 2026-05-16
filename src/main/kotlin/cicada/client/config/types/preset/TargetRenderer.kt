package cicada.client.config.types.preset

import cicada.client.event.Event
import cicada.client.config.types.ToggleableConfigurable
import cicada.client.config.types.preset.targetrenderer.TargetRenderMode
import cicada.client.config.types.preset.targetrenderer.impl.TargetRender2DRectMode
import net.minecraft.world.entity.Entity

// SCWGxD regrets everything he did. 13.04.2026 7:45.
class TargetRenderer : ToggleableConfigurable("Render target", true) {
    private val mode = choice("Mode").apply {
        choice(TargetRender2DRectMode()).select()
    }

    fun render(event: Event, target: Entity) {
        val renderer = mode.inner

        if (renderer !is TargetRenderMode)
            return

        renderer.render(event, target)
    }
}