package cicada.client.feature.module.modules.combat.attackaura.aim.mode

import FastNoise
import cicada.client.rotation.Rotation
import cicada.client.setting.value.Configurable
import cicada.client.setting.value.ToggleableConfigurable
import cicada.client.utils.client.player
import cicada.client.utils.math.coerceIn
import cicada.client.utils.math.random
import cicada.client.utils.math.randomFloat
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
        delta.clamp(
            Math.random().toFloat() * 30f + 20f,
            Math.random().toFloat() * 30f + 20f
        )
        delta.round(gcd(), gcd())
        player.rotate(delta)
    }
}

object UniversalAimMode : AttackAuraAimMode("Universal") {
    val speed by float("Speed", 70f, 0f..100f)

    object Dependence : Configurable("Dependence") {
        val xRadius by float("XRadius", 1f, 0f..5f)
        val xThreshold by float("XThreshold", 4f, 0f..10f)
        val yRadius by float("YRadius", 1f, 0f..5f)
        val yThreshold by float("YThreshold", 4f, 0f..10f)
    }

    object Spring : Configurable("Spring") {
        val x by floatRange("FactorX", 0.1f..0.3f, 0f..1f)
        val y by floatRange("FactorY", 0.3f..0.5f, 0f..1f)
    }

    object Jitter : Configurable("Jitter") {
        val x by float("FactorX", 1f, 0f..5f)
        val y by float("FactorY", 1f, 0f..5f)
    }

    init {
        tree(Dependence)
        tree(Spring)
        tree(Jitter)
    }

    private var lastDelta = Rotation(0f, 0f)

    override fun rotateTo(target: LivingEntity) {
        val point = player.eyePosition.coerceIn(target.boundingBox)
        val delta = (rotationTo(point) - player.rotation()).wrapped()
        val speed = delta.length().coerceIn(-speed, speed)
        delta /= delta.length()
        delta *= speed
        delta.lerp(Spring.x.random(), Spring.y.random(), lastDelta)

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
    private val amplitudeX by float("AmplitudeX", 1f, 0f..5f)
    private val amplitudeY by float("AmplitudeY", 1f, 0f..5f)
    private val amplitudeZ by float("AmplitudeZ", 1f, 0f..5f)
    private val useGcd   by boolean("GCDRounding", true)

    object General : Configurable("General") {
        val seed      by int("Seed", 1337, 0..99999)
        val frequency by float("Frequency", 1f, 0f..5f)

        val noiseType = choice("NoiseType").apply {
            choice("OpenSimplex2")
            choice("OpenSimplex2S").select()
            choice("Cellular")
            choice("Perlin")
            choice("ValueCubic")
            choice("Value")
        }

        val cellular = tree(Cellular())

        val rotationType = choice("Rotation3D").apply {
            choice("None").select()
            choice("ImproveXYPlanes")
            choice("ImproveXZPlanes")
        }

        class Cellular : Configurable("Cellular") {
            val distanceFunction = choice("DistanceFunc").apply {
                choice("Euclidean")
                choice("EuclideanSq").select()
                choice("Manhattan")
                choice("Hybrid")
            }
            val returnType = choice("ReturnType").apply {
                choice("CellValue")
                choice("Distance").select()
                choice("Distance2")
                choice("Distance2Add")
                choice("Distance2Sub")
                choice("Distance2Mul")
                choice("Distance2Div")
            }
            val jitter by float("Jitter", 1f, 0f..2f)

            init {
                visible { noiseType.inner?.name == "Cellular" }
            }
        }
    }

    object Fractal : Configurable("Fractal") {
        val type = choice("Type").apply {
            choice("None").select()
            choice("FBm")
            choice("Ridged")
            choice("PingPong")
            choice("DomainWarpProgressive")
            choice("DomainWarpIndependent")
        }
        val octaves           by int  ("Octaves",            3,    1..8)
        val lacunarity        by float("Lacunarity",         2f,   0f..10f)
        val gain              by float("Gain",               0.5f, 0f..1f)
        val weightedStrength  by float("WeightedStrength",  0f,   0f..1f)
        val pingPongStrength  by float("Ping-pongStrength", 2f,   0f..10f).visible { type.inner?.name == "Ping pong" }
    }

    object DomainWarp : ToggleableConfigurable("Domain warp", false) {
        val type = choice("Type").apply {
            choice("OpenSimplex2").select()
            choice("OpenSimplex2Reduced")
            choice("BasicGrid")
        }
        val amplitude by float("Amplitude", 30f, 0f..100f)
    }

    private val noiseGenerator = FastNoise(0)

    init {
        tree(General)
        tree(Fractal)
        tree(DomainWarp)
    }

    private fun applySettings() {
        noiseGenerator.SetSeed(General.seed)
        noiseGenerator.SetFrequency(General.frequency)

        noiseGenerator.SetNoiseType(when (General.noiseType.inner?.name) {
            "OpenSimplex2"  -> FastNoise.NoiseType.OpenSimplex2
            "Cellular"      -> FastNoise.NoiseType.Cellular
            "Perlin"        -> FastNoise.NoiseType.Perlin
            "ValueCubic"    -> FastNoise.NoiseType.ValueCubic
            "Value"         -> FastNoise.NoiseType.Value
            else            -> FastNoise.NoiseType.OpenSimplex2S
        })

        noiseGenerator.SetRotationType3D(when (General.rotationType.inner?.name) {
            "ImproveXYPlanes" -> FastNoise.RotationType3D.ImproveXYPlanes
            "ImproveXZPlanes" -> FastNoise.RotationType3D.ImproveXZPlanes
            else                -> FastNoise.RotationType3D.None
        })

        noiseGenerator.SetFractalType(when (Fractal.type.inner?.name) {
            "FBm"                      -> FastNoise.FractalType.FBm
            "Ridged"                   -> FastNoise.FractalType.Ridged
            "PingPong"                 -> FastNoise.FractalType.PingPong
            "DomainWarpProgressive"    -> FastNoise.FractalType.DomainWarpProgressive
            "DomainWarpIndependent"    -> FastNoise.FractalType.DomainWarpIndependent
            else                       -> FastNoise.FractalType.None
        })
        noiseGenerator.SetFractalOctaves(Fractal.octaves)
        noiseGenerator.SetFractalLacunarity(Fractal.lacunarity)
        noiseGenerator.SetFractalGain(Fractal.gain)
        noiseGenerator.SetFractalWeightedStrength(Fractal.weightedStrength)
        noiseGenerator.SetFractalPingPongStrength(Fractal.pingPongStrength)

        noiseGenerator.SetCellularDistanceFunction(when (General.cellular.distanceFunction.inner?.name) {
            "Euclidean"   -> FastNoise.CellularDistanceFunction.Euclidean
            "Manhattan"   -> FastNoise.CellularDistanceFunction.Manhattan
            "Hybrid"      -> FastNoise.CellularDistanceFunction.Hybrid
            else          -> FastNoise.CellularDistanceFunction.EuclideanSq
        })
        noiseGenerator.SetCellularReturnType(when (General.cellular.returnType.inner?.name) {
            "CellValue"    -> FastNoise.CellularReturnType.CellValue
            "Distance2"    -> FastNoise.CellularReturnType.Distance2
            "Distance2Add" -> FastNoise.CellularReturnType.Distance2Add
            "Distance2Sub" -> FastNoise.CellularReturnType.Distance2Sub
            "Distance2Mul" -> FastNoise.CellularReturnType.Distance2Mul
            "Distance2Div" -> FastNoise.CellularReturnType.Distance2Div
            else           -> FastNoise.CellularReturnType.Distance
        })
        noiseGenerator.SetCellularJitter(General.cellular.jitter)

        if (DomainWarp.toggled) {
            noiseGenerator.SetDomainWarpType(when (DomainWarp.type.inner?.name) {
                "OpenSimplex2Reduced" -> FastNoise.DomainWarpType.OpenSimplex2Reduced
                "BasicGrid"           -> FastNoise.DomainWarpType.BasicGrid
                else                  -> FastNoise.DomainWarpType.OpenSimplex2
            })
            noiseGenerator.SetDomainWarpAmp(DomainWarp.amplitude)
        }
    }

    override fun rotateTo(target: LivingEntity) {
        applySettings()

        val time = (System.currentTimeMillis() % Int.MAX_VALUE).toFloat() / 1_000f

        val noiseX = noiseGenerator.GetNoise(time, 0f  , 0f  )
        val noiseY = noiseGenerator.GetNoise(0f  , time, 0f  )
        val noiseZ = noiseGenerator.GetNoise(0f  , 0f  , time)

        val boundingBox = target.boundingBox
        val wHalf = (boundingBox.maxX - boundingBox.minX) / 2
        val hHalf = (boundingBox.maxY - boundingBox.minY) / 2
        val lHalf = (boundingBox.maxZ - boundingBox.minZ) / 2

        val point = Vec3(
            boundingBox.center.x + wHalf * noiseX * amplitudeX,
            boundingBox.center.y + hHalf * noiseY * amplitudeY,
            boundingBox.center.z + lHalf * noiseZ * amplitudeZ,
        )

        val delta = (rotationTo(point) - player.rotation()).wrapped()
        if (useGcd) delta.round(gcd(), gcd())
        player.rotate(delta)
    }
}