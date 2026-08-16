package cicada.client.feature.module.modules.misc

import cicada.client.event.Event
import cicada.client.event.events.EventAttack
import cicada.client.event.events.EventTick
import cicada.client.event.events.EventWorldChange
import cicada.client.feature.module.ClientModule
import cicada.client.feature.module.ModuleCategory
import cicada.client.utils.client.level
import cicada.client.utils.client.player
import cicada.client.utils.math.Color4f
import cicada.utility.player.inventory.isSword
import net.minecraft.tags.ItemTags
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Items

object ModuleMurderMysteryHelper : ClientModule(
    "MurderMysteryHelper",
    ModuleCategory.MISC
) {
    val rolesHighlight = toggleableGroup("RolesHighlight", false)
    val murdersColor = rolesHighlight.color("MurdersColor", Color4f(1f, 0f, 0f, 1f))
    val detectivesColor = rolesHighlight.color("DetectivesColor", Color4f(0f, 1f, 0f, 1f))

    private val silentKill = toggleableGroup("SilentKill", false)
    private val staticSlot by silentKill.boolean("StaticSlot", false)
    private val slot by silentKill.int("StaticSlot", 0, 0..8).visible { staticSlot }

    private val _murders = mutableListOf<Player>()
    private val _detectives = mutableListOf<Player>()
    private var stashSlot = -1

    val murders: List<Player> get() = _murders
    val detectives: List<Player> get() = _detectives

    override fun onDisable() {
        clear()
    }

    override fun onEvent(event: Event) {
        if (event is EventAttack.Pre) {
            val slot = if (staticSlot) slot else getSwordSlot()

            stashSlot = -1

            if (slot == -1)
                return

            stashSlot = player.inventory.selectedSlot

            player.inventory.setSelectedSlot(slot)
        }

        if (event is EventAttack.Post) {
            if (stashSlot != -1)
                player.inventory.setSelectedSlot(stashSlot)
        }

        if (event is EventWorldChange)
            clear()

        if (event is EventTick.Pre) {
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