package cicada.client.module.impl.combat.attackaura.aim

import cicada.client.module.impl.combat.attackaura.aim.mode.AttackAuraAimMode
import cicada.client.module.impl.combat.attackaura.aim.mode.custom.AttackAuraCustomMode
import cicada.client.setting.ToggleableConfigureable
import cicada.client.utils.rotation.Rotation
import net.minecraft.world.entity.LivingEntity

// SCWGxD regrets everything he did. 20.04.2026 7:35.
object AttackAuraAimer : ToggleableConfigureable("Aim", true) {
    private val mode by choice("Mode").apply {
        choice(AttackAuraCustomMode).select()
    }

    fun delta(target: LivingEntity): Rotation {
        if (mode is AttackAuraAimMode) return (mode as AttackAuraAimMode).delta(target)
        return Rotation(0f, 0f)
    }
}