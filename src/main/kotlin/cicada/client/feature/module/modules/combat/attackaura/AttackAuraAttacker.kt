package cicada.client.feature.module.modules.combat.attackaura

import cicada.client.event.Event
import cicada.client.event.impl.GameLoopEvent
import cicada.client.event.impl.LegitClickTimingEvent
import cicada.client.setting.Configurable
import cicada.client.setting.ToggleableConfigurable
import cicada.client.utils.client.mc
import cicada.client.utils.client.player
import cicada.client.utils.math.random
import cicada.client.utils.raycast.customStartAttack
import cicada.client.utils.player.canCrit
import net.minecraft.world.entity.LivingEntity
import kotlin.math.max
import kotlin.math.roundToInt

// SCWGxD regrets everything he did. 19.04.2026 15:22.
object AttackAuraAttacker : ToggleableConfigurable("Attack", true) {
    private val maxClicksPerSecond by floatRange("Max clicks per second", 20f..20f, 0f..40f)
    private val recalculateIn by floatRange("Recalculate in", 0f..0f, 0f..40f, "clicks")

    object AttackConditions : Configurable("Attack conditions") {
        object Crit : ToggleableConfigurable("Crit", false) {
            val ignoreWhileOnGround by boolean("Ignore while on ground", false)
        }

        object ItemCooldown : ToggleableConfigurable("Item cooldown", false) {
            val itemCooldown by float("Item cooldown", 0.9f, 0f..1f)
        }

        object RayCast : ToggleableConfigurable("RayCast", true)

        private val preAttackRange by float("Pre attack range", 6f, 0f..10f)

        fun shouldAttack(target: LivingEntity): Boolean {
            if (
                player.distanceTo(target) <= max(preAttackRange, attackRange) &&
                (player.getAttackStrengthScale(0.5f) > ItemCooldown.itemCooldown || !ItemCooldown.toggled)
            ) {
                if (player.onGround() && Crit.ignoreWhileOnGround || player.canCrit() || !Crit.toggled)
                    return true
            }

            return false
        }
    }

    private val attackRange by float("Attack range", 3f, 0f..6f)

    init {
        tree(AttackConditions)
    }

    private var clickDelay = 0
    private var lastClickTime = 0L
    private var clicksBeforeRecalculation = 0
    private var clicks = 0

    fun onEvent(event: Event, target: LivingEntity) {
        if (event is GameLoopEvent.Pre) {
            if (clicksBeforeRecalculation <= 0) {
                clickDelay = (1000.0 / maxClicksPerSecond.random()).roundToInt()
                clicksBeforeRecalculation = recalculateIn.random().roundToInt()
            }

            if (AttackConditions.shouldAttack(target) && System.currentTimeMillis() - lastClickTime >= clickDelay) {
                clicks++
                lastClickTime = System.currentTimeMillis()
                clicksBeforeRecalculation--
            }
        }

        if (event is LegitClickTimingEvent) {
            repeat(clicks) {
                mc.customStartAttack(attackRange.toDouble())
            }

            clicks = 0
        }
    }
}