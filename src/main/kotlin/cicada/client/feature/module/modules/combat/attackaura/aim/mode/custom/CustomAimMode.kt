package cicada.client.feature.module.modules.combat.attackaura.aim.mode.custom

import cicada.client.feature.module.modules.combat.attackaura.aim.mode.AttackAuraAimMode
import cicada.client.feature.module.modules.combat.attackaura.aim.mode.custom.deltatransform.BasicRandomizer
import cicada.client.feature.module.modules.combat.attackaura.aim.mode.custom.deltatransform.GCDFixer
import cicada.client.feature.module.modules.combat.attackaura.aim.mode.custom.deltatransform.SpeedLimiter
import cicada.client.feature.module.modules.combat.attackaura.aim.mode.custom.deltatransform.Spring
import cicada.client.feature.module.modules.combat.attackaura.aim.mode.custom.pointselect.PointSelector
import cicada.client.utils.player
import cicada.client.utils.rotation.Rotation
import cicada.client.utils.rotation.rotation
import cicada.client.utils.rotation.rotationTo
import net.minecraft.world.entity.LivingEntity

// SCWGxD regrets everything he did. 28.04.2026 11:55.
object CustomAimMode : AttackAuraAimMode("Custom") {
    private val pointSelector = tree(PointSelector)
    private val deltaTransform = group("Delta transform")
    private val speedLimiter = deltaTransform.tree(SpeedLimiter)
    private val basicRandomizer = deltaTransform.tree(BasicRandomizer)
    private val spring = deltaTransform.tree(Spring)
    private val gcdFixer = deltaTransform.tree(GCDFixer)

    private val deltaTransformers = listOf(speedLimiter, basicRandomizer, spring, gcdFixer)

    override fun delta(target: LivingEntity): Rotation {
        val point = pointSelector.select(target)
        var delta = (rotationTo(point) - player.rotation()).wrapped()

        for (transformer in deltaTransformers) {
            if (!transformer.toggled) continue
            delta = transformer.transform(delta)
        }

        return delta
    }
}