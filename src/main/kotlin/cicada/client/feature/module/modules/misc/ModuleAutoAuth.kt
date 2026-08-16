package cicada.client.feature.module.modules.misc

import cicada.client.event.Event
import cicada.client.event.events.EventChatMessage
import cicada.client.feature.module.ClientModule
import cicada.client.feature.module.ModuleCategory
import cicada.client.utils.client.connection

object ModuleAutoAuth : ClientModule("AutoAuth", ModuleCategory.MISC) {
    val password = "[12333]"

    override fun onEvent(event: Event) {
        if (event is EventChatMessage.Receive) {
            val message = event.content

            if (message.contains("/reg")) {
                connection.sendCommand("register $password $password")
            } else if (message.contains("/l")) {
                connection.sendCommand("login $password")
            }
        }
    }
}