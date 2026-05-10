package cicada.client.utils.target

import cicada.client.rotation.CameraRotation
import cicada.client.utils.player
import cicada.client.utils.rotation.yawTo
import net.minecraft.world.entity.LivingEntity
import kotlin.math.abs

// SCWGxD regrets everything he did. 04.04.2026 17:13.
enum class BestEntityBy(val entity: (List<LivingEntity>) -> LivingEntity?) {
    FOV({ it.minByOrNull { abs(CameraRotation.y - yawTo(it.eyePosition)).toDouble() } }),
    Distance({ it.minByOrNull { player.distanceTo(it).toDouble() } }),
    Health({ it.minByOrNull { (it.health + it.absorptionAmount).toDouble() } }),
    HurtTime({ it.minByOrNull { it.hurtTime.toDouble() } })
}