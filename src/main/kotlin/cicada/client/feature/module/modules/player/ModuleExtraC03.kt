package cicada.client.feature.module.modules.player

import cicada.client.event.Event
import cicada.client.event.impl.SendPosEvent
import cicada.client.feature.module.ModuleCategory
import cicada.client.feature.module.ClientModule
import cicada.client.utils.client.connection
import cicada.client.utils.client.player
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket

// SCWGxD regrets everything he did. 29.04.2026 11:17.
object ModuleExtraC03 : ClientModule("ExtraC03", ModuleCategory.PLAYER) {
    private val condition = multiChoice("Condition")
    private val whileItemUse = condition.choice("While item use")
    private val whileRegeneration = condition.choice("While regeneration")
    private val whileBurning = condition.choice("While burning")
    private val regenerationToHealth by int("Regeneration to health", 19, 0..20).visible { whileRegeneration.toggled }

    override fun onEvent(event: Event) {
        whileItemUse.toggled = true
        if (event is SendPosEvent.Pre) {
            if (
                (!whileItemUse.toggled || player.isUsingItem) &&
                (!whileRegeneration.toggled || player.health < regenerationToHealth) &&
                (!whileBurning.toggled || player.isOnFire)
            ) connection.send(ServerboundMovePlayerPacket.PosRot(player.position(), player.yRot, player.xRot + 0.05f, player.onGround(), player.horizontalCollision))

        }
    }
}