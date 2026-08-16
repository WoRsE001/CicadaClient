package cicada.client.feature.module.modules.player

import cicada.client.event.Event
import cicada.client.event.events.EventClickTiming
import cicada.client.feature.module.ClientModule
import cicada.client.feature.module.ModuleCategory
import cicada.client.utils.client.level
import cicada.client.utils.client.mc
import cicada.client.utils.client.player
import cicada.client.utils.player.inventory.getDestroySpeedWithEnchantment
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult

// SCWGxD regrets everything he did. 18.06.2026 15:58.
object ModuleAutoTool : ClientModule("AutoTool", ModuleCategory.PLAYER) {
    private val switchDelay by int("SwitchDelay", 0, 0..20)
    private val backSwitchDelay by int("BackSwitchDelay", 0, 0..20)

    private var switchTimer = 0
    private var bachSwitchTimer = 0
    private var lastSlot = -1
    private var switched = false

    override fun onEvent(event: Event) {
        if (event is EventClickTiming) {
            switchTimer++
            bachSwitchTimer++

            val needSwitch = mc.hitResult is BlockHitResult && mc.options.keyAttack.isDown

            if (switchTimer >= switchDelay) {
                if (needSwitch && !switched) {
                    switched = true
                    switchTool((mc.hitResult as BlockHitResult).blockPos)
                }
            }

            if (switched) {
                if (!needSwitch) {
                    if (bachSwitchTimer >= backSwitchDelay) switchBack()
                } else {
                    bachSwitchTimer = 0
                    switchTool((mc.hitResult as BlockHitResult).blockPos)
                }
            }

            if (!needSwitch) switchTimer = 0
        }
    }

    fun getToolSlot(blockState: BlockState): Int {
        var bestSlot = player.inventory.selectedSlot
        var bestSlotMineSpeed = player.inventory.selectedItem.getDestroySpeedWithEnchantment(blockState)

        for (slot in 0..8) {
            if (slot == bestSlot)
                continue

            val slotMineSpeed = player.inventory.getItem(slot).getDestroySpeedWithEnchantment(blockState)

            if (slotMineSpeed > bestSlotMineSpeed) {
                bestSlot = slot
                bestSlotMineSpeed = slotMineSpeed
            }
        }

        return bestSlot
    }

    fun switchTool(blockPos: BlockPos) {
        if (lastSlot == -1) lastSlot = player.inventory.selectedSlot
        player.inventory.selectedSlot = getToolSlot(level.getBlockState(blockPos))
    }

    fun switchBack() {
        if (lastSlot != -1) player.inventory.selectedSlot = lastSlot
        switched = false
        lastSlot = -1
    }
}