package cicada.client.feature.module.modules.player

import cicada.client.event.Event
import cicada.client.event.events.EventSendPos
import cicada.client.feature.module.ClientModule
import cicada.client.feature.module.ModuleCategory
import cicada.client.utils.client.connection
import cicada.client.utils.client.player
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket

// SCWGxD regrets everything he did. 29.04.2026 11:17.
object ModuleExtraC03 : ClientModule("ExtraC03", ModuleCategory.PLAYER) {
    private val condition = multiChoice("Condition")
    private val whileItemUse = condition.choice("WhileItemSse")
    private val whileRegeneration = condition.choice("WhileRegeneration")
    private val whileBurning = condition.choice("WhileBurning")
    private val regenerationToHealth by int("RegenerationToHealth", 19, 0..20).visible { whileRegeneration.toggled }

    override fun onEvent(event: Event) {
        if (event is EventSendPos.Pre) {
            if (
                (!whileItemUse.toggled || player.isUsingItem) &&
                (!whileRegeneration.toggled || player.health < regenerationToHealth) &&
                (!whileBurning.toggled || player.isOnFire)
            ) connection.send(ServerboundMovePlayerPacket.PosRot(player.position(), player.yRot, player.xRot + 0.05f, player.onGround(), player.horizontalCollision))

        }
    }
}