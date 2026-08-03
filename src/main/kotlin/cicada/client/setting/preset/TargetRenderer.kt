package cicada.client.setting.preset

import cicada.client.event.Event
import cicada.client.setting.value.ToggleableConfigurable
import cicada.client.setting.preset.targetrenderer.TargetRenderMode
import cicada.client.setting.preset.targetrenderer.impl.TargetRender2DRectMode
import net.minecraft.world.entity.Entity

// SCWGxD regrets everything he did. 13.04.2026 7:45.
class TargetRenderer(defaultToggled: Boolean = false) : ToggleableConfigurable(
    "Target renderer", defaultToggled
) {
    private val mode = choice("Mode").apply {
        choice(TargetRender2DRectMode()).select()
    }

    fun render(event: Event, target: Entity) {
        if (!toggled) return

        val renderer = mode.inner

        if (renderer !is TargetRenderMode)
            return

        renderer.render(event, target)
    }
}