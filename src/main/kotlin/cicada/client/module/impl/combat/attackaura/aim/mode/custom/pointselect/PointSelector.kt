package cicada.client.module.impl.combat.attackaura.aim.mode.custom.pointselect

import cicada.client.setting.Configureable
import cicada.client.utils.math.coerceIn
import cicada.client.utils.math.mul
import cicada.client.utils.player
import cicada.client.utils.rotation.rotation
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.phys.Vec3

// SCWGxD regrets everything he did. 28.04.2026 12:03.
object PointSelector : Configureable("PointSelector") {
    private val mode = choice("Mode")
    private val modeSinglePoint = mode.choice("Single").select()
    private val modeClosestPos = mode.choice("Closest to position")
    private val modeClosestRot = mode.choice("Closest to rotation")

    private val expandXZ by float("Expand XZ", 1f, 0f..1f)
    private val expandY by float("Expand Y", 1f, 0f..1f)

    fun get(target: LivingEntity): Vec3 {
        val boundingBox = target.boundingBox.mul(expandXZ.toDouble(), expandY.toDouble())
        if (modeSinglePoint.selected()) {
            return target.eyePosition
        } else if (modeClosestPos.selected()) {
            return player.eyePosition.coerceIn(boundingBox)
        } else if (modeClosestRot.selected()) {
        }

        return target.eyePosition
    }
}