package cicada.client.feature.module.modules.player.nofalldamage.mode

import cicada.client.event.Event
import cicada.client.event.events.EventSendPos
import cicada.client.setting.value.ChoiceValue

object NoFallDamageSpoofGround : ChoiceValue.Choice("SpoofGround") {
    private val onGround by boolean("OnGround", true)

    override fun onEvent(event: Event) {
        if (event is EventSendPos.Pre) {
            event.ground = onGround
        }
    }
}