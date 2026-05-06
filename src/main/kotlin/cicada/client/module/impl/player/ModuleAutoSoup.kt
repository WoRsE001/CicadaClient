package cicada.client.module.impl.player

import cicada.client.event.Event
import cicada.client.event.impl.LegitClickTimingEvent
import cicada.client.module.Category
import cicada.client.module.Module
import cicada.client.utils.gameMode
import cicada.client.utils.mc
import cicada.client.utils.player
import cicada.client.utils.time.Timer
import net.minecraft.world.InteractionHand
import net.minecraft.world.item.Items

object ModuleAutoSoup : Module("AutoSoup", Category.PLAYER) {
    private val health by int("Health", 6, 0..20)
    private val useDelay by int("Use delay", 0, 0..20)
    private val dropDelay by int("Drop delay", 0, 0..20)
    private val switchDelay by int("Switch delay", 0, 0..20)

    private val soupTimer = Timer()
    private val useTimer = Timer()
    private val dropTimer = Timer()
    private var switchBack = false
    private var lastSoupSlot = -1

    override fun onEvent(event: Event) {
        if (mc.screen != null) return

        if (event is LegitClickTimingEvent) {
            if (soupTimer.reached(switchDelay * 50)) {
                if (switchBack) {
                    if (lastSoupSlot != -1)
                        player.inventory.selectedSlot = lastSoupSlot

                    switchBack = false
                    soupTimer.reset()
                    useTimer.reset()
                    dropTimer.reset()
                }

                val soupSlot = soupSlot()

                if (soupSlot != -1 && player.health < health) {
                    lastSoupSlot = player.inventory.selectedSlot
                    player.inventory.selectedSlot = soupSlot

                    if (useTimer.reached(useDelay * 50)) {
                        gameMode.useItem(player, InteractionHand.MAIN_HAND)
                        if (dropTimer.reached(dropDelay)) {
                            if (!player.isSpectator && player.drop(mc.hasControlDown())) {
				                player.swing(InteractionHand.MAIN_HAND)
			                }
                        }
                    }

                    switchBack = true
                    soupTimer.reset()
                }
            } else if (player.inventory.selectedItem.`is`(Items.BOWL)) {
                if (!player.isSpectator && player.drop(mc.hasControlDown())) {
                    player.swing(InteractionHand.MAIN_HAND)
                }
            }
        }
    }

    fun soupSlot(): Int {
        for (i in 0 until 9) {
            val itemStack = player.inventoryMenu.slots[i + 36].item
            if (itemStack.`is`(Items.MUSHROOM_STEW)) return i
        }

        return -1
    }
}