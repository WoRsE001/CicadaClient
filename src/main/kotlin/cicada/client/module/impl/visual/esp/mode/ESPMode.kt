package cicada.client.module.impl.visual.esp.mode

import cicada.client.event.Event
import cicada.client.setting.ChoiceValue
import cicada.client.setting.ColorValue
import net.minecraft.world.entity.Entity

// SCWGxD regrets everything he did. 06.04.2026 11:47.
abstract class ESPMode(name: String) : ChoiceValue.Choice(name) {
    abstract val color: ColorValue

    abstract fun onEvent(event: Event, entity: Entity)
}