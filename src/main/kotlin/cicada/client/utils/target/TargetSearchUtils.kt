package cicada.client.utils.target

import cicada.client.rotation.CameraRotation
import cicada.client.utils.level
import cicada.client.utils.player
import cicada.client.utils.rotation.yawTo
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.decoration.ArmorStand
import kotlin.math.abs

// SCWGxD regrets everything he did. 04.04.2026 17:13.
enum class SortType(val sort: (LivingEntity) -> Double) {
    FOV({ abs(CameraRotation.y - yawTo(it.eyePosition)).toDouble() }),
    Distance({ player.distanceTo(it).toDouble() }),
    Health({ (it.health + it.absorptionAmount).toDouble() }),
    HurtTime({ it.hurtTime.toDouble() });
}

fun validTargets(
    range: Float
): List<LivingEntity> {
    return level.entitiesForRendering()
        .filterIsInstance<LivingEntity>()
        .filter { it != player && player.distanceTo(it) <= range && !it.isDeadOrDying && it !is ArmorStand }
}

fun List<LivingEntity>.bestTargetBy(
    sortType: SortType
): LivingEntity? {
    return this.minByOrNull { sortType.sort(it) }
}