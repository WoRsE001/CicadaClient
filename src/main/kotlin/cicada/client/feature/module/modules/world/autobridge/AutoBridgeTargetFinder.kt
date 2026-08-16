package cicada.client.feature.module.modules.world.autobridge

import cicada.client.setting.value.Configurable
import cicada.client.utils.client.level
import cicada.client.utils.client.player
import net.minecraft.core.BlockPos

object AutoBridgeTargetFinder : Configurable("TargetFinder") {
    val searchRange by float("SearchRange", 4.5f, 0f..6f)
    val heightCheck by boolean("HeightCheck", true)

    var target: BlockPos? = null

    fun updateTarget() {
        target = null

        val xRange = (player.eyePosition.x - searchRange).toInt()..(player.eyePosition.x + searchRange).toInt()
        var yRange = (player.eyePosition.y - searchRange).toInt()..(player.eyePosition.y + searchRange).toInt()
        if (heightCheck) yRange = yRange.first..yRange.last.coerceAtMost((player.eyePosition.y - 2).toInt())
        val zRange = (player.eyePosition.z - searchRange).toInt()..(player.eyePosition.z + searchRange).toInt()

        for (x in xRange) {
            for (y in yRange) {
                for (z in zRange) {
                    if (level.getBlockState(BlockPos(x, y, z)).isAir) continue
                    if (
                        target == null ||
                        player.distanceToSqr(x + 0.5, y + 0.5, z + 0.5) < player.distanceToSqr(
                            target!!.x + 0.5,
                            target!!.y + 0.5,
                            target!!.z + 0.5
                        )
                    ) target = BlockPos(x, y, z)
                }
            }
        }
    }

    fun resetTarget() {
        target = null
    }
}