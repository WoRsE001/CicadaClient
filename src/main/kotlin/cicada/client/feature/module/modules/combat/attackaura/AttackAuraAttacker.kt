package cicada.client.feature.module.modules.combat.attackaura

import cicada.client.event.Event
import cicada.client.event.events.EventGameLoop
import cicada.client.event.events.EventClickTiming
import cicada.client.setting.value.Configurable
import cicada.client.setting.value.ToggleableConfigurable
import cicada.client.utils.client.mc
import cicada.client.utils.client.player
import cicada.client.utils.math.coerceIn
import cicada.client.utils.math.random
import cicada.client.utils.player.canCrit
import cicada.client.utils.player.groundTick
import cicada.client.utils.raycast.findEntityInCrosshair
import cicada.client.utils.rotation.rotation
import net.minecraft.world.entity.LivingEntity
import kotlin.math.roundToInt

// SCWGxD regrets everything he did. 19.04.2026 15:22.
object AttackAuraAttacker : ToggleableConfigurable("Attacker", true) {
    private val maxClicksPerSecond by floatRange("MaxClicksPerSecond", 20f..20f, 0f..40f)
    private val recalculateIn by floatRange("RecalculateIn", 0f..0f, 0f..40f, "clicks")

    object ClickConditions : Configurable("ClickConditions") {
        abstract class Condition(
            name: String,
            defaultToggled: Boolean
        ) : ToggleableConfigurable(name, defaultToggled) {
            abstract fun shouldAttack(target: LivingEntity): Boolean
        }

        object Crit : Condition("Crit", false) {
            val ignoreWhileOnGround by boolean("IgnoreWhileOnGround", false)
            val groundTicks by int("Ground ticks", 2, 1..10).visible { ignoreWhileOnGround }

            override fun shouldAttack(target: LivingEntity) = (
                    player.onGround() &&
                            ignoreWhileOnGround &&
                            player.groundTick >= groundTicks
                    ) || player.canCrit()
        }

        object ItemCooldown : Condition("ItemCooldown", false) {
            val itemCooldown by float("ItemCooldown", 0.9f, 0f..1f)

            override fun shouldAttack(target: LivingEntity) = player.getAttackStrengthScale(0.5f) > itemCooldown
        }

        object Range : Condition("Range", false) {
            val range by float("Range", 6f, 0f..10f)

            override fun shouldAttack(target: LivingEntity) =
                player.eyePosition.coerceIn(target.boundingBox).distanceTo(player.eyePosition) <= range
        }

        object RayCast : Condition("RayCast", true) {
            val onlyOnEntity by boolean("OnlyOnEntity", false)
            val onlyOnTarget by boolean("OnlyOnTarget", false).visible { onlyOnEntity }

            override fun shouldAttack(target: LivingEntity): Boolean {
                if (!onlyOnEntity) return true
                val hitResult = findEntityInCrosshair(67.0, player.rotation()) ?: return false
                return !(onlyOnTarget && hitResult.entity != target)
            }
        }

        init {
            tree(Crit)
            tree(ItemCooldown)
            tree(Range)
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

    private val clickConditions = tree(ClickConditions)
    private val attackRange by float("AttackRange", 3f, 0f..6f)

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

            if (clickConditions.shouldAttack(target) && System.currentTimeMillis() - lastClickTime >= clickDelay) {
                clicks++
                lastClickTime = System.currentTimeMillis()
                clicksBeforeRecalculation--
            }
        }

        if (event is EventClickTiming) {
            repeat(clicks) {
                mc.startAttack()
            }

            clicks = 0
        }
    }
}
