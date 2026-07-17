package cicada.client.feature.module.modules.visual.esp.mode

import cicada.client.event.Event
import cicada.client.setting.value.ColorValue
import cicada.client.setting.value.MultiChoiceValue
import net.minecraft.world.entity.Entity

// SCWGxD regrets everything he did. 06.04.2026 11:47.
abstract class ESPMode(name: String) : MultiChoiceValue.Choice(name, true) {
    abstract val color: ColorValue

    abstract fun onEvent(event: Event, entity: Entity)
}