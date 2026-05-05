package cicada.client.module.impl.combat.attackaura.aim.mode

import cicada.client.setting.ChoiceValue
import cicada.client.utils.rotation.Rotation
import net.minecraft.world.entity.LivingEntity

// SCWGxD regrets everything he did. 28.04.2026 11:53.
abstract class AttackAuraAimMode(name: String) : ChoiceValue.Choice(name) {
    abstract fun delta(target: LivingEntity): Rotation
}