package cicada.client.module.impl.visual.esp.entitytype

import cicada.client.event.Event
import cicada.client.module.impl.visual.esp.mode.ESPMode
import cicada.client.module.impl.visual.esp.mode.impl.ESP2DOutlineMode
import cicada.client.module.impl.visual.esp.mode.impl.ESP2DPointerMode
import cicada.client.module.impl.visual.esp.mode.impl.ESP3DBoxMode
import cicada.client.setting.ChoiceValue
import net.minecraft.world.entity.Entity

// SCWGxD regrets everything he did. 06.04.2026 12:12.
class ESPEntityTypeRenderer(name: String, val condition: (Entity) -> Boolean) : ChoiceValue.Choice(name) {
    private val renderModes = choice("Render modes").apply {
        choice(ESP2DOutlineMode())
        choice(ESP2DPointerMode()).select()
        choice(ESP3DBoxMode())
    }

    fun onEvent(event: Event, entity: Entity) {
        for (choice in renderModes.choices) {
            if (choice.selected() && choice is ESPMode && condition(entity)) {
                choice.onEvent(event, entity)
            }
        }
    }
}