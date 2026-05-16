package cicada.client.feature.module.modules.visual.esp.mode

import cicada.client.event.Event
import cicada.client.config.types.ChoiceValue
import cicada.client.config.types.ColorValue
import net.minecraft.world.entity.Entity

// SCWGxD regrets everything he did. 06.04.2026 11:47.
abstract class ESPMode(name: String) : ChoiceValue.Choice(name) {
    abstract val color: ColorValue

    abstract fun onEvent(event: Event, entity: Entity)
}