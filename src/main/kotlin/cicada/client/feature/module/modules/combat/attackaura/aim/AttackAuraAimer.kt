package cicada.client.feature.module.modules.combat.attackaura.aim

import cicada.client.feature.module.modules.combat.attackaura.aim.mode.*
import cicada.client.config.types.ToggleableConfigurable
import cicada.client.feature.module.modules.combat.attackaura.aim.mode.custom.CustomAimMode
import cicada.client.utils.rotation.Rotation
import net.minecraft.world.entity.LivingEntity

// SCWGxD regrets everything he did. 20.04.2026 7:35.
object AttackAuraAimer : ToggleableConfigurable("Aim", true) {
    private val mode by choice("Mode").apply {
        choice(CustomAimMode)
        choice(IntaveAimMode)
        choice(GrimAimMode)
        choice(PolarAimMode)
        choice(NoiseAimMode).select()
    }

    fun delta(target: LivingEntity): Rotation {
        if (mode is AttackAuraAimMode) return (mode as AttackAuraAimMode).delta(target)
        return Rotation(0f, 0f)
    }
}