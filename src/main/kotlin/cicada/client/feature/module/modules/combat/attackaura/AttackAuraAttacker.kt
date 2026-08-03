package cicada.client.feature.module.modules.combat.attackaura

import cicada.client.event.Event
import cicada.client.event.impl.EventGameLoop
import cicada.client.event.impl.LegitClickTimingEvent
import cicada.client.mixin.accessors.AccessorMinecraft
import cicada.client.setting.value.Configurable
import cicada.client.setting.value.ToggleableConfigurable
import cicada.client.utils.client.mc
import cicada.client.utils.client.player
import cicada.client.utils.math.coerceIn
import cicada.client.utils.math.random
import cicada.client.utils.player.canCrit
import cicada.client.utils.player.groundTick
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.phys.EntityHitResult
import kotlin.math.max
import kotlin.math.roundToInt

// SCWGxD regrets everything he did. 19.04.2026 15:22.
object AttackAuraAttacker : ToggleableConfigurable("Attacker", true) {
    private val maxClicksPerSecond by floatRange("Max clicks per second", 20f..20f, 0f..40f)
    private val recalculateIn by floatRange("Recalculate in", 0f..0f, 0f..40f, "clicks")

    object AttackConditions : Configurable("Attack conditions") {
        abstract class Condition(
            name: String,
            defaultToggled: Boolean
        ) : ToggleableConfigurable(name, defaultToggled) {
            abstract fun shouldAttack(target: LivingEntity): Boolean
        }

        object Crit : Condition("Crit", false) {
            val ignoreWhileOnGround by boolean("Ignore while on ground", false)
            val groundTicks by int("Ground ticks", 2, 1..10).visible { ignoreWhileOnGround }

            override fun shouldAttack(target: LivingEntity) = (
                    player.onGround() &&
                            ignoreWhileOnGround &&
                            player.groundTick >= groundTicks
                    ) || player.canCrit()
        }

        object Distance : Condition("Distance", false) {
            val preAttackRange by float("Pre attack range", 6f, 0f..10f)

            override fun shouldAttack(target: LivingEntity) =
                player.eyePosition
                    .coerceIn(target.boundingBox)
                    .distanceTo(player.eyePosition) <= max(preAttackRange, attackRange)

        }

        object ItemCooldown : Condition("Item cooldown", false) {
            val itemCooldown by float("Item cooldown", 0.9f, 0f..1f)

            override fun shouldAttack(target: LivingEntity) = player.getAttackStrengthScale(0.5f) > itemCooldown
        }

        object RayCast : Condition("RayCast", true) {
            val onlyOnEntity by boolean("Only on entity", false)
            val onlyOnTarget by boolean("Only on target", false).visible { onlyOnEntity }
            // val xFOV by float("X FOX", 20f, 0f..180f).visible { onlyOnTarget }
            // val yFOV by float("Y FOX", 20f, 0f..180f).visible { onlyOnTarget }

            override fun shouldAttack(target: LivingEntity): Boolean {
                if (!onlyOnEntity) return true
                val hitResult = mc.hitResult ?: return false
                if (hitResult !is EntityHitResult) return false
                if (onlyOnTarget && hitResult.entity != target) return false
                return true
            }
        }

        init {
            tree(Crit)
            tree(Distance)
            tree(ItemCooldown)
            tree(RayCast)
        }

        fun shouldAttack(target: LivingEntity): Boolean {
            for (value in inner) {
                if (value !is Condition) continue
                if (value.toggled && !value.shouldAttack(target)) return false
            }

            return true
        }
    }

    private val attackConditions = tree(AttackConditions)
    private val attackRange by float("Attack range", 3f, 0f..6f)

    private var clickDelay = 0
    private var lastClickTime = 0L
    private var clicksBeforeRecalculation = 0
    private var clicks = 0

    fun onEvent(event: Event, target: LivingEntity) {
        if (event is EventGameLoop.Pre) {
            if (clicksBeforeRecalculation <= 0) {
                clickDelay = (1000.0 / maxClicksPerSecond.random()).roundToInt()
                clicksBeforeRecalculation = recalculateIn.random().roundToInt()
            }

            if (attackConditions.shouldAttack(target) && System.currentTimeMillis() - lastClickTime >= clickDelay) {
                clicks++
                lastClickTime = System.currentTimeMillis()
                clicksBeforeRecalculation--
            }
        }

        if (event is LegitClickTimingEvent) {
            repeat(clicks) {
                (mc as AccessorMinecraft).invokeStartAttack()
            }

            clicks = 0
        }
    }
}
