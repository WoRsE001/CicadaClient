package cicada.client.feature.module.modules.visual

import cicada.client.event.Event
import cicada.client.feature.module.ModuleCategory
import cicada.client.feature.module.ClientModule
import cicada.client.feature.module.modules.visual.esp.entitytype.ESPEntityTypeRenderer
import cicada.client.utils.level
import cicada.client.utils.mc
import net.minecraft.client.player.LocalPlayer
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.entity.player.Player

object ModuleESP : ClientModule("ESP", ModuleCategory.VISUAL) {
    private val entityTypeRenderer = choice("Entity type").apply {
        choice(ESPEntityTypeRenderer("Player") { it is Player && (it !is LocalPlayer || !mc.options.cameraType.isFirstPerson) }).select()
        choice(ESPEntityTypeRenderer("Item") { it is ItemEntity })
    }

    override fun onEvent(event: Event) {
        for (entity in level.entitiesForRendering()) {
            for (choice in entityTypeRenderer.choices) {
                if (choice.selected() && choice is ESPEntityTypeRenderer) {
                    choice.onEvent(event, entity)
                }
            }
        }
    }
}