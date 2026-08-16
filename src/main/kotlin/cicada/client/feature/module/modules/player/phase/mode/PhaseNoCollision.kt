package cicada.client.feature.module.modules.player.phase.mode

import cicada.client.event.Event
import cicada.client.event.events.EventBlockShape
import cicada.client.setting.value.ChoiceValue
import cicada.client.utils.client.mc
import cicada.client.utils.client.player
import net.minecraft.world.phys.shapes.Shapes

// SCWGxD regrets everything he did. 28.06.2026 7:22.
object PhaseNoCollision : ChoiceValue.Choice("NoCollision") {
    override fun onEvent(event: Event) {
        if (event is EventBlockShape) {
            if (event.pos.y >= player.blockPosition().y || mc.options.keyShift.isDown && player.onGround()) {
                event.shape = Shapes.empty()
            }
        }
    }
}