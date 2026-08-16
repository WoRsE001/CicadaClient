package cicada.client.event.events

import cicada.client.event.CancelableEvent
import cicada.client.event.Event
import cicada.utility.player.inventory.isConsumable
import cicada.utility.player.inventory.isSword
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Input
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.BowItem
import net.minecraft.world.item.ItemStack

interface EventAttack {
    object Pre : CancelableEvent() {
        lateinit var player: Player
        lateinit var target: Entity
    }

    object Post : CancelableEvent() {
        lateinit var player: Player
        lateinit var target: Entity
    }
}

object EventSlowDown : CancelableEvent() {
    var type = Type.Bow
    var sprint = false
    var slowDown = 0f

    fun getTypeByItem(itemStack: ItemStack): Type? {
        if (itemStack.item is BowItem) return Type.Bow
        else if (itemStack.isConsumable) return Type.Consume
        else if (itemStack.isSword) return Type.SwordBlock
        return null
    }

    enum class Type() {
        Bow,
        Consume,
        Hit,
        Sneak,
        SwordBlock
    }
}

interface EventPlayerStateUpdate {
    object Pre: Event
    object Post: Event
}

object EventSendInput : Event {
    var input = Input.EMPTY
}

object EventRelativeMove : Event {
    var yaw = 0f
}

object EventJump : Event {
    var jumpPower = 0f
    var motionYaw = 0f
    var motionAddFactor = 0f
}

interface EventPlayerTurn {
    object Pre: Event

    object Post: Event
}
