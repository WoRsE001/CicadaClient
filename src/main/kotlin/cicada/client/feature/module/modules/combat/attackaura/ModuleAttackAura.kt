package cicada.client.feature.module.modules.combat.attackaura

import cicada.client.event.Event
import cicada.client.event.events.EventTick
import cicada.client.feature.module.ClientModule
import cicada.client.feature.module.ModuleCategory
import cicada.client.feature.module.modules.combat.attackaura.aim.AttackAuraAimer
import cicada.client.rotation.Rotator
import cicada.client.setting.preset.MovementCorrector
import cicada.client.setting.preset.TargetFinder
import cicada.client.setting.preset.TargetRenderer
import net.minecraft.world.entity.LivingEntity

// SCWGxD regrets everything he did. 04.04.2026 5:37.
object ModuleAttackAura : ClientModule("AttackAura", ModuleCategory.COMBAT), Rotator {
    private val targetFinder = tree(TargetFinder())
    private val aimer = tree(AttackAuraAimer)
    private val movementCorrector = tree(MovementCorrector())
    private val attacker = tree(AttackAuraAttacker)
    private val targetRenderer = tree(TargetRenderer())

    val target: LivingEntity?
        get() = targetFinder.target

    override val rotatePriority = 0

    init {
        registerToRotations()
    }

    override fun onDisable() {
        targetFinder.resetTarget()
    }

    override fun onEvent(event: Event) {
        if (event is EventTick.Pre) {
            targetFinder.updateTarget()
        }

        target?.let {
            movementCorrector.onEvent(event)
            attacker.onEvent(event, target!!)
            targetRenderer.render(event, target!!)
        }
    }

    override fun rotate() {
        aimer.rotateTo(target!!)
    }

    override fun willRotate() =
        shouldListenEvents() && aimer.toggled && target != null
}