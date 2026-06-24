package cicada.client.feature.module.modules.combat.attackaura.aim

import cicada.client.feature.module.modules.combat.attackaura.aim.mode.*
import cicada.client.setting.ToggleableConfigurable
import net.minecraft.world.entity.LivingEntity

// SCWGxD regrets everything he did. 20.04.2026 7:35.
object AttackAuraAimer : ToggleableConfigurable("Aimer", true) {
    private val mode by choice("Mode").apply {
        choice(RageAimMode)
        choice(UniversalAimMode).select()
    }

    fun rotateTo(target: LivingEntity) {
        if (mode is AttackAuraAimMode) (mode as AttackAuraAimMode).rotateTo(target)
    }
}