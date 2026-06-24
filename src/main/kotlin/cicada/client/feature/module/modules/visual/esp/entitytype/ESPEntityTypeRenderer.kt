package cicada.client.feature.module.modules.visual.esp.entitytype

import cicada.client.event.Event
import cicada.client.feature.module.modules.visual.esp.mode.ESPMode
import cicada.client.feature.module.modules.visual.esp.mode.impl.ESP2DOutlineMode
import cicada.client.feature.module.modules.visual.esp.mode.impl.ESP2DPointerMode
import cicada.client.feature.module.modules.visual.esp.mode.impl.ESP3DBoxMode
import cicada.client.setting.MultiChoiceValue
import net.minecraft.world.entity.Entity

// SCWGxD regrets everything he did. 06.04.2026 12:12.
class ESPEntityTypeRenderer(name: String, val isValidEntity: (Entity) -> Boolean) : MultiChoiceValue.Choice(name, true) {
    private val renderModes = multiChoice("Render modes").apply {
        choice(ESP2DOutlineMode())
        choice(ESP2DPointerMode())
        choice(ESP3DBoxMode())
    }

    fun onEvent(event: Event, entity: Entity) {
        for (choice in renderModes.inner.filter { it.toggled }) {
            if (choice is ESPMode) {
                choice.onEvent(event, entity)
            }
        }
    }
}