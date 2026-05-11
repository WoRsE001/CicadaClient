package cicada.client.module.impl.combat.attackaura.aim.mode

import cicada.client.utils.math.coerceIn
import cicada.client.utils.math.mul
import cicada.client.utils.math.randomFloat
import cicada.client.utils.player
import cicada.client.utils.rotation.Rotation
import cicada.client.utils.rotation.gcd
import cicada.client.utils.rotation.rotation
import cicada.client.utils.rotation.rotationTo
import net.minecraft.world.entity.LivingEntity

object IntaveAimMode : AttackAuraAimMode("Intave") {
    override fun delta(target: LivingEntity): Rotation {
        val point = player.eyePosition.coerceIn(target.boundingBox)
        val delta = (rotationTo(point) - player.rotation()).wrapped()
        delta /= 4f
        delta += Rotation(randomFloat(3f), randomFloat(3f))
        return delta
    }
}

object GrimAimMode : AttackAuraAimMode("Grim") {
    override fun delta(target: LivingEntity): Rotation {
        val point = player.eyePosition.coerceIn(target.boundingBox)
        val delta = (rotationTo(point) - player.rotation()).wrapped()
        return delta
    }
}

object PolarAimMode : AttackAuraAimMode("Polar") {
    var lastDelta = Rotation(0f, 0f)

    override fun delta(target: LivingEntity): Rotation {
        val point = player.rotation().clamped(target.boundingBox.mul(0.9, 1.0)).directionVector
        val delta = (rotationTo(point) - player.rotation()).wrapped()
        delta.clamp(60f, 90f)
        delta += Rotation(randomFloat(3f), randomFloat(3f))
        delta.gazLarpit(randomFloat(0.3f, 0.7f), randomFloat(0.1f, 0.3f), lastDelta)
        delta.round(gcd(), gcd())
        lastDelta = delta
        return delta
    }
}