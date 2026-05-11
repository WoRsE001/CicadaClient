package cicada.client.module.impl.combat.attackaura.aim.mode.custom.deltatransform

import cicada.client.utils.math.gazLarpit
import cicada.client.utils.math.random
import cicada.client.utils.rotation.Rotation
import cicada.client.utils.rotation.gcd
import cicada.client.utils.rotation.lastDelta

object SpeedLimiter : DeltaTransformer("Speed limit") {
    private val x by float("X", 180f, 0f..180f)
    private val y by float("Y", 180f, 0f..180f)

    override fun transform(delta: Rotation): Rotation {
        return delta.clamped(x, y)
    }
}

object BasicRandomizer : DeltaTransformer("Basic randomize") {
    private val x by float("X", 0f, 0f..10f)
    private val y by float("Y", 0f, 0f..10f)

    override fun transform(delta: Rotation): Rotation {
        return delta + Rotation((-x..x).random(), (-y..y).random())
    }
}

object Spring : DeltaTransformer("Spring") {
    private val x by floatRange("X", 0.3f..0.7f, 0f..1f)
    private val y by floatRange("Y", 0.3f..0.7f, 0f..1f)

    override fun transform(delta: Rotation): Rotation {
        return Rotation(
            gazLarpit(x.random(), delta.x, lastDelta.x),
            gazLarpit(y.random(), delta.y, lastDelta.y)
        )
    }
}

object GCDFixer : DeltaTransformer("GCD Fix") {
    override fun transform(delta: Rotation): Rotation {
        return delta.rounded(gcd(), gcd())
    }
}
