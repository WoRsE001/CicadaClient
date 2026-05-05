package cicada.client.setting.preset.targetrenderer

import cicada.client.event.Event
import cicada.client.setting.ChoiceValue
import net.minecraft.world.entity.Entity

// SCWGxD regrets everything he did. 13.04.2026 7:49.
abstract class TargetRenderMode(name: String) : ChoiceValue.Choice(name) {
    abstract fun render(event: Event, target: Entity)
}