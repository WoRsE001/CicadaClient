package cicada.client.feature.module.modules.combat.attackaura.aim.mode

import FastNoise
import cicada.client.feature.module.modules.combat.attackaura.aim.mode.UniversalAimMode.Jitter
import cicada.client.feature.module.modules.combat.attackaura.aim.mode.UniversalAimMode.speed
import cicada.client.utils.math.coerceIn
import cicada.client.utils.client.player
import cicada.client.utils.math.randomFloat
import cicada.client.rotation.Rotation
import cicada.client.setting.Configurable
import cicada.client.utils.math.boundingBox
import cicada.client.utils.math.random
import cicada.client.utils.rotation.gcd
import cicada.client.utils.rotation.rotate
import cicada.client.utils.rotation.rotation
import cicada.client.utils.rotation.rotationTo
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.phys.Vec3

object RageAimMode : AttackAuraAimMode("Rage") {
    override fun rotateTo(target: LivingEntity) {
        val point = player.eyePosition.coerceIn(target.boundingBox)
        val delta = (rotationTo(point) - player.rotation()).wrapped()
        player.rotate(delta)
    }
}

object IntaveAimMode : AttackAuraAimMode("Intave") {
    override fun rotateTo(target: LivingEntity) {
        val point = player.eyePosition.coerceIn(target.boundingBox)
        val delta = (rotationTo(point) - player.rotation()).wrapped()
        delta.clamp(30f, 30f)

        val boundingBox = target.boundingBox

        delta += if (
            player.eyePosition.x !in boundingBox.minX..boundingBox.maxX !=
            player.eyePosition.z !in boundingBox.minZ..boundingBox.maxZ
        ) {
            Rotation(randomFloat(3f), randomFloat(3f))
        } else if (
            player.eyePosition.x in boundingBox.minX..boundingBox.maxX &&
            player.eyePosition.z in boundingBox.minZ..boundingBox.maxZ
        ) {
            Rotation(0f, 0f)
        } else {
            Rotation(randomFloat(3f), 0f)
        }

        delta.round(gcd(), gcd())
        player.rotate(delta)
    }
}

object UniversalAimMode : AttackAuraAimMode("Universal") {
    val speed by float("Speed", 70f, 0f..100f)

    object Dependence : Configurable("Dependence") {
        val xRadius by float("X radius", 1f, 0f..5f)
        val xThreshold by float("X threshold", 4f, 0f..10f)
        val yRadius by float("Y radius", 1f, 0f..5f)
        val yThreshold by float("Y threshold", 4f, 0f..10f)
    }

    object Sprint : Configurable("Spring") {
        val x by floatRange("Factor X", 0.1f..0.3f, 0f..1f)
        val y by floatRange("Factor Y", 0.3f..0.5f, 0f..1f)
    }

    object Jitter : Configurable("Jitter") {
        val x by float("Factor X", 1f, 0f..5f)
        val y by float("Factor Y", 1f, 0f..5f)
    }

    private var lastDelta = Rotation(0f, 0f)

    override fun rotateTo(target: LivingEntity) {
        val point = player.rotation().clamped(target.boundingBox).directionVector
        val delta = (rotationTo(point) - player.rotation()).wrapped()
        val speed = delta.length().coerceIn(-speed, speed)
        delta /= delta.length()
        delta *= speed
        delta.gazLarpit(Sprint.x.random(), Sprint.y.random(), lastDelta)

        val dependOnX = delta.y >= Dependence.xThreshold
        val dependOnY = delta.x >= Dependence.yThreshold

        if (dependOnX) {
            delta.x += Dependence.xRadius
        }

        if (dependOnY) {
            delta.y += Dependence.yRadius
        }

        delta += Rotation(randomFloat(Jitter.x), randomFloat(Jitter.y))
        delta.round(gcd(), gcd())
        player.rotate(delta)
        lastDelta = delta
    }
}

object NoiseAimMode : AttackAuraAimMode("Noise") {
    val noiseGenerator = FastNoise(1488)

    override fun rotateTo(target: LivingEntity) {
        val time = (System.currentTimeMillis() % Int.MAX_VALUE) / 1_000f

        noiseGenerator.SetNoiseType(FastNoise.NoiseType.OpenSimplex2)
        noiseGenerator.SetFrequency(1f)

        val noiseX = noiseGenerator.GetNoise(time, 0f  , 0f  )
        val noiseY = noiseGenerator.GetNoise(0f  , time, 0f  )
        val noiseZ = noiseGenerator.GetNoise(0f  , 0f  , time)

        val boundingBox = target.boundingBox
        val wHitBox = (boundingBox.maxX - boundingBox.minX) / 2
        val hHitBox = (boundingBox.maxY - boundingBox.minY) / 2
        val lHitBox = (boundingBox.maxZ - boundingBox.minZ) / 2

        val point = Vec3(
            boundingBox.center.x + wHitBox * noiseX,
            boundingBox.center.y + hHitBox * noiseY,
            boundingBox.center.z + lHitBox * noiseZ,
        )

        val delta = (rotationTo(point) - player.rotation()).wrapped()
        player.rotate(delta)
    }
}