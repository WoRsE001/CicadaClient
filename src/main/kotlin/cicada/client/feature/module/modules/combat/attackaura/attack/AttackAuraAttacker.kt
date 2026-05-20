package cicada.client.feature.module.modules.combat.attackaura.attack

import cicada.client.event.Event
import cicada.client.event.impl.AttackEvent
import cicada.client.event.impl.GameLoopEvent
import cicada.client.event.impl.LegitClickTimingEvent
import cicada.client.mixin.accessors.AccessorMinecraft
import cicada.client.config.types.ToggleableConfigurable
import cicada.client.utils.client.connection
import cicada.client.utils.math.gaussianRandom
import cicada.client.utils.math.random
import cicada.client.utils.client.mc
import cicada.client.utils.client.player
import cicada.client.utils.player.canCrit
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket
import net.minecraft.world.entity.LivingEntity

// SCWGxD regrets everything he did. 19.04.2026 15:22.
object AttackAuraAttacker : ToggleableConfigurable("Attack", true) {
    private val attackType = choice("Attack type")
    private val attackTypeLegacy = attackType.choice("Legacy")
    private val attackTypeModern = attackType.choice("Modern").select()

    private val APS by floatRange("Attacks per second", 20f..20f, 0f..40f)
    private val randomizeType = choice("Randomize type")
    private val randomizeTypeBasic = randomizeType.choice("Basic").select()
    private val randomizeTypeGaussian = randomizeType.choice("Gaussian")

    private val critType = choice("Crit type")
    private val critTypeNever = critType.choice("Never")
    private val critTypeSmart = critType.choice("Smart")
    private val critTypeAlways = critType.choice("Always").select()

    private val preAttackRange by float("Pre attack range", 3f, 0f..6f)
    private val critFix by boolean("Crit fix", false)

    private var lastTimeKaka = 0L
    var attacksCount = 0

    fun onEvent(event: Event, target: LivingEntity) {
        if (event is GameLoopEvent.Pre) {
            val delay = 1000f /
                    if (attackTypeModern.selected()) {
                        20f
                    } else {
                        if (randomizeTypeBasic.selected())
                            APS.random()
                        else
                            APS.gaussianRandom()
                    }

            if (System.currentTimeMillis() - lastTimeKaka > delay) {
                if (canAttack(target)) {
                    attacksCount++
                    lastTimeKaka = System.currentTimeMillis()
                }
            }
        }

        if (event is LegitClickTimingEvent) {
            repeat(attacksCount) {
                (mc as AccessorMinecraft).invokeStartAttack()
            }
            attacksCount = 0
        }

        if (critFix && player.isSprinting) {
            if (event is AttackEvent.Pre) {
                connection.send(ServerboundPlayerCommandPacket(player, ServerboundPlayerCommandPacket.Action.STOP_SPRINTING))
            }

            if (event is AttackEvent.Post) {
                connection.send(ServerboundPlayerCommandPacket(player, ServerboundPlayerCommandPacket.Action.START_SPRINTING))
            }
        }
    }

    fun canAttack(target: LivingEntity): Boolean {
        if (player.distanceTo(target) <= preAttackRange && (player.getAttackStrengthScale(0.5f) > 0.9 || attackTypeLegacy.selected())) {
            if (player.onGround() && critTypeSmart.selected() || player.canCrit() || critTypeNever.selected())
                return true
        }

        return false
    }
}