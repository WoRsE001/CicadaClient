package cicada.client.event.impl

import cicada.client.event.CancelableEvent
import cicada.client.event.Event
import cicada.utility.player.inventory.isConsumable
import cicada.utility.player.inventory.isSword
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.BowItem
import net.minecraft.world.item.ItemStack

// я знаю что это ужасно
interface AttackEvent {
    object Pre : CancelableEvent() {
        lateinit var player: Player
        lateinit var target: Entity
    }

    object BeforeAttackPacket : CancelableEvent() {
        lateinit var player: Player
        lateinit var target: Entity
    }

    object Post : CancelableEvent() {
        lateinit var player: Player
        lateinit var target: Entity
    }
}

object SlowDownEvent : CancelableEvent() {
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

interface PlayerStateUpdateEvent {
    object Pre: Event
    object Post: Event
}

object JumpEvent : Event {
    var jumpPower = 0f
    var motionYaw = 0f
    var motionAddFactor = 0f
}
