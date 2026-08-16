package cicada.client.feature.module.modules.combat

import cicada.client.event.Event
import cicada.client.event.events.EventAttack
import cicada.client.feature.module.ClientModule
import cicada.client.feature.module.ModuleCategory
import cicada.client.utils.client.player

// SCWGxD regrets everything he did. 21.06.2026 17:06.
object ModuleAttributeSwapper : ClientModule("AttributeSwapper", ModuleCategory.COMBAT) {
    private val attributeSlot by int("AttributeSlot", 2, 1..9)
    private val attackSlot by int("AttackSlot", 1, 1..9)

    override fun onEvent(event: Event) {
        if (event is EventAttack.Pre) {
            player.inventory.selectedSlot = attributeSlot - 1
        }

        if (event is EventAttack.Post) {
            player.inventory.selectedSlot = attackSlot - 1
        }
    }
}