package cicada.client.module.impl.misc

import cicada.client.event.Event
import cicada.client.event.impl.AttackEvent
import cicada.client.event.impl.TickEvent
import cicada.client.event.impl.WorldChangeEvent
import cicada.client.module.Category
import cicada.client.module.Module
import cicada.client.utils.level
import cicada.client.utils.math.Color4f
import cicada.client.utils.player
import cicada.utility.player.inventory.isSword
import net.minecraft.tags.ItemTags
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Items

object ModuleMurderMysteryHelper : Module(
    "MurderMysteryHelper",
    Category.MISC
) {
    val rolesHighlight = toggleableGroup("Roles highlight", false)
    val murdersColor = rolesHighlight.color("Murders color", Color4f(1f, 0f, 0f, 1f))
    val detectivesColor = rolesHighlight.color("Detectives color", Color4f(0f, 1f, 0f, 1f))

    private val silentKill = toggleableGroup("Silent kill", false)
    private val staticSlot by silentKill.boolean("Static slot", false)
    private val slot by silentKill.int("Static slot", 0, 0..8).visible { staticSlot }

    private val _murders = mutableListOf<Player>()
    private val _detectives = mutableListOf<Player>()
    private var stashSlot = -1

    val murders: List<Player> get() = _murders
    val detectives: List<Player> get() = _detectives

    override fun onDisable() {
        clear()
    }

    override fun onEvent(event: Event) {
        if (event is AttackEvent.Pre) {
            val slot = if (staticSlot) slot else getSwordSlot()

            stashSlot = -1

            if (slot == -1)
                return

            stashSlot = player.inventory.selectedSlot

            player.inventory.setSelectedSlot(slot)
        }

        if (event is AttackEvent.Post) {
            if (stashSlot != -1)
                player.inventory.setSelectedSlot(stashSlot)
        }

        if (event is WorldChangeEvent)
            clear()

        if (event is TickEvent.Pre) {
            for (player in level.players()) {
                if (player in _murders || player in _detectives)
                    continue

                val heldItem = player.mainHandItem

                // ВЕЛИКОЛЕПНЫЙ МАЙНКРАФТ СПОСИБО ЗА НАЗВАНИЕ ФУНКЦИИ IS()
                if (heldItem.`is`(ItemTags.SWORDS))
                    _murders += player

                if (heldItem.`is`(Items.BOW))
                    _detectives += player
            }
        }
    }

    private fun clear() {
        _murders.clear()
        _detectives.clear()
    }

    private fun getSwordSlot(): Int {
        for (i in 0..8) {
            if (player.inventory.getItem(i).isSword)
                return i
        }

        return -1
    }
}