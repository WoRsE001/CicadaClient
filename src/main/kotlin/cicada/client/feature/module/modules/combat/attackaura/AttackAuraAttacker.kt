package cicada.client.feature.module.modules.combat.attackaura

import cicada.client.event.Event
import cicada.client.event.impl.LegitClickTimingEvent
import cicada.client.setting.ToggleableConfigurable
import cicada.client.utils.client.mc
import cicada.client.utils.client.player
import cicada.client.utils.raycast.customStartAttack
import cicada.client.utils.player.canCrit
import net.minecraft.world.entity.LivingEntity

// SCWGxD regrets everything he did. 19.04.2026 15:22.
object AttackAuraAttacker : ToggleableConfigurable("Attack", true) {
    private val attackSync = choice("Attack sync")
    private val attackSyncNone = attackSync.choice("None")
    private val attackSyncCrit = attackSync.choice("Crit")
    private val attackSyncCombined = attackSync.choice("Combined").select()

    private val preAttackRange by float("Pre attack range", 3f, 0f..10f)
    private val attackRange = float("Attack range", 3f, 0f..6f)

    private val rayCast = toggleableGroup("Ray cast", true)

    fun onEvent(event: Event, target: LivingEntity) {
        if (event is LegitClickTimingEvent) {
            if (shouldAttack(target))
                mc.customStartAttack(attackRange.inner.toDouble())
        }
    }

    fun shouldAttack(target: LivingEntity): Boolean {
        if (player.distanceTo(target) <= preAttackRange && (player.getAttackStrengthScale(0.5f) > 0.9)) {
            if (player.onGround() && attackSyncCombined.selected() || player.canCrit() || attackSyncNone.selected())
                return true
        }

        return false
    }
}