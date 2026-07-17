package cicada.client.feature.module.modules.combat.attackaura.aim.mode

import cicada.client.setting.value.ChoiceValue
import net.minecraft.world.entity.LivingEntity

// SCWGxD regrets everything he did. 28.04.2026 11:53.
abstract class AttackAuraAimMode(name: String) : ChoiceValue.Choice(name) {
    abstract fun rotateTo(target: LivingEntity)
}