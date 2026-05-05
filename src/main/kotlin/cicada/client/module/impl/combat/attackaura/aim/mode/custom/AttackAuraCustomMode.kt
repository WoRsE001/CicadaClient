package cicada.client.module.impl.combat.attackaura.aim.mode.custom

import cicada.client.module.impl.combat.attackaura.aim.mode.AttackAuraAimMode
import cicada.client.utils.rotation.Rotation
import net.minecraft.world.entity.LivingEntity

// SCWGxD regrets everything he did. 28.04.2026 11:55.
object AttackAuraCustomMode : AttackAuraAimMode("Custom") {
    private val xRotSpeed by float("Pitch rotate speed", 180f, 0f..100f)
    private val yRotSpeed by float("Yaw rotate speed", 180f, 0f..100f)

    override fun delta(target: LivingEntity): Rotation {
        TODO("Not yet implemented")
    }
}