package cicada.client.module.impl.world

import cicada.client.event.Event
import cicada.client.event.impl.MovementInputEvent
import cicada.client.event.impl.RenderEvent
import cicada.client.module.Category
import cicada.client.module.Module
import cicada.client.utils.level
import cicada.client.utils.player
import cicada.client.utils.player.velocityX
import cicada.client.utils.player.velocityZ
import cicada.client.utils.render.FILLED_QUAD
import cicada.client.utils.render.Render3D
import net.minecraft.core.BlockPos
import kotlin.math.abs
import kotlin.math.sign

object ModuleBridgeAssist : Module("BridgeAssist", Category.WORLD) {
    private val edgeOffset by float("Edge offset", 0.05f, 0f..0.5f)
    private val sneakIfPressed by boolean("Sneak if presses", true)
    private val pitchCheck by floatRange("Pitch check", -90f..-70f, -90f..90f)

    override fun onEvent(event: Event) {
        if (event is MovementInputEvent) {
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