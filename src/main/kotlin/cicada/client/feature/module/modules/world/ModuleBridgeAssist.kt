package cicada.client.feature.module.modules.world

import cicada.client.event.Event
import cicada.client.event.events.EventMovementInput
import cicada.client.feature.module.ClientModule
import cicada.client.feature.module.ModuleCategory
import cicada.client.utils.client.level
import cicada.client.utils.client.player
import cicada.client.utils.player.velocityX
import cicada.client.utils.player.velocityZ
import net.minecraft.core.BlockPos
import kotlin.math.abs
import kotlin.math.sign

object ModuleBridgeAssist : ClientModule("BridgeAssist", ModuleCategory.WORLD) {
    private val edgeOffset by float("EdgeOffset", 0.05f, 0f..0.5f)
    private val sneakIfPressed by boolean("SneakIfPresses", true)
    private val pitchCheck by floatRange("PitchCheck", -90f..-70f, -90f..90f)

    override fun onEvent(event: Event) {
        if (event is EventMovementInput) {
            if (player.xRot !in pitchCheck) return
            val targetBlock = getDirectionalBlockPos(edgeOffset)
            val isAirBlock = level.getBlockState(targetBlock).isAir
            if (sneakIfPressed) {
                if (event.sneak && !isAirBlock) {
                    event.sneak = false
                }
            } else {
                if (isAirBlock) {
                    event.sneak = true
                }
            }
        }
    }

    fun getDirectionalBlockPos(edgeOffset: Float): BlockPos {
        var x = player.position().x + 0.5f
        val y = player.position().y - 1
        var z = player.position().z + 0.5f

        if (abs(player.velocityX) > 0.1) x += player.velocityX.sign * edgeOffset
        if (abs(player.velocityZ) > 0.1) z += player.velocityZ.sign * edgeOffset

        return BlockPos(x.toInt(), y.toInt(), z.toInt())
    }
}