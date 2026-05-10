package cicada.client.module.impl.combat.attackaura.autoblock

import cicada.client.event.Event
import cicada.client.event.impl.LegitClickTimingEvent
import cicada.client.event.impl.TickEvent
import cicada.client.module.impl.combat.attackaura.attack.AttackAuraAttacker
import cicada.client.setting.ToggleableConfigureable
import cicada.client.utils.connection
import cicada.client.utils.player
import cicada.utility.player.inventory.isSword
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket
import net.minecraft.network.protocol.game.ServerboundUseItemPacket
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.LivingEntity

object AttackAuraAutoBlock : ToggleableConfigureable("AutoBlock", false) {
    private val blockRange by float("Block range", 6f, 0f..20f)

    private var shouldBlock = false
    private var isBlocking = false

    fun onEvent(event: Event, target: LivingEntity) {
        if (!toggled) {
            if (event is LegitClickTimingEvent) unBlock()
            return
        }

        if (event is TickEvent.Pre) {
            if (player.distanceTo(target) <= blockRange && player.mainHandItem.isSword && AttackAuraAttacker.attacksCount <= 0) {
                shouldBlock = true
            } else {
                shouldBlock = false
            }
        }

        if (event is LegitClickTimingEvent) {
            if (shouldBlock) {
                block()
            } else {
                unBlock()
            }
        }
    }

    fun block() {
        if (!isBlocking) {
            connection.send(ServerboundUseItemPacket(InteractionHand.MAIN_HAND, 0, player.yRot, player.xRot))
            isBlocking = true
        }
    }

    fun unBlock() {
        if (isBlocking) {
            connection.send(ServerboundPlayerActionPacket(ServerboundPlayerActionPacket.Action.RELEASE_USE_ITEM, BlockPos.ZERO, Direction.DOWN))
            isBlocking = false
        }
    }
}