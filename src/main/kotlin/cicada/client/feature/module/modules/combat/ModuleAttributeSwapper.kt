package cicada.client.feature.module.modules.combat

import cicada.client.event.Event
import cicada.client.event.impl.AttackEvent
import cicada.client.feature.module.ClientModule
import cicada.client.feature.module.ModuleCategory
import cicada.client.utils.client.player

// SCWGxD regrets everything he did. 21.06.2026 17:06.
object ModuleAttributeSwapper : ClientModule("AttributeSwapper", ModuleCategory.COMBAT) {
    private val attributeSlot by int("Attribute slot", 2, 1..9)
    private val attackSlot by int("Attack slot", 1, 1..9)

    override fun onEvent(event: Event) {
        if (event is AttackEvent.Pre) {
            player.inventory.selectedSlot = attributeSlot - 1
        }

        if (event is AttackEvent.Post) {
            player.inventory.selectedSlot = attackSlot - 1
        }
    }
}