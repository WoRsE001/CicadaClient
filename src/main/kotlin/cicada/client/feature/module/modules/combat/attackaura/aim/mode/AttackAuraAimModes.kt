package cicada.client.feature.module.modules.combat.attackaura.aim.mode

import cicada.client.utils.math.coerceIn
import cicada.client.utils.client.player
import cicada.client.utils.math.randomFloat
import cicada.client.utils.rotation.Rotation
import cicada.client.utils.rotation.gcd
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

object UniversalAimMode : AttackAuraAimMode("Universal") {
    private var lastDelta = Rotation(0f, 0f)

    override fun rotateTo(target: LivingEntity) {
        val point = target.eyePosition
        val delta = (rotationTo(point) - player.rotation()).wrapped()
        val speed = delta.length().coerceIn(-70f..70f)
        delta /= delta.length()
        delta *= speed
        delta += Rotation(randomFloat(3), randomFloat(3))
        delta.gazLarpit(randomFloat(0.3f, 0.5f), randomFloat(0.3f, 0.5f), lastDelta)
        delta.round(gcd(), gcd())
        player.rotate(delta)
        lastDelta = delta
    }
}