package cicada.client.feature.module.modules.combat.attackaura.aim.mode

import cicada.client.utils.math.coerceIn
import cicada.client.utils.client.player
import cicada.client.utils.rotation.rotate
import cicada.client.utils.rotation.rotation
import cicada.client.utils.rotation.rotationTo
import net.minecraft.world.entity.LivingEntity

object RageAimMode : AttackAuraAimMode("Rage") {
    override fun rotateTo(target: LivingEntity) {
        val point = player.eyePosition.coerceIn(target.boundingBox)
        val delta = (rotationTo(point) - player.rotation()).wrapped()
        player.rotate(delta)
    }
}