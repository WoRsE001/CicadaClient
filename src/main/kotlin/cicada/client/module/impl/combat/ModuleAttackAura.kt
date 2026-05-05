package cicada.client.module.impl.combat

import cicada.client.event.Event
import cicada.client.event.impl.TickEvent
import cicada.client.module.Category
import cicada.client.module.Module
import cicada.client.module.impl.combat.attackaura.attack.AttackAuraAttacker
import cicada.client.module.impl.combat.attackaura.aim.AttackAuraAimer
import cicada.client.rotation.RotationListener
import cicada.client.setting.preset.TargetFinder
import cicada.client.setting.preset.TargetRenderer
import cicada.client.utils.nullCheck
import cicada.client.utils.player
import cicada.client.utils.rotation.rotate

// SCWGxD regrets everything he did. 04.04.2026 5:37.
object ModuleAttackAura : Module("AttackAura", Category.COMBAT), RotationListener {
    val targetFinder = tree(TargetFinder())
    private val rotator = tree(AttackAuraAimer)
    private val attacker = tree(AttackAuraAttacker)
    private val targetRenderer = tree(TargetRenderer())

    override val rotatePriority = 1

    init {
        registerToRotations()
    }

    override fun onDisable() {
        targetFinder.resetTarget()
    }

    override fun onEvent(event: Event) {
        if (event is TickEvent.Pre) {
            targetFinder.updateTarget()
        }

        targetFinder.target?.let {
            attacker.onEvent(event, targetFinder.target!!)
            targetRenderer.render(event, targetFinder.target!!)
        }
    }

    override fun rotate() {
        player.rotate(rotator.delta(targetFinder.target!!))
    }

    override fun willRotate() = toggled && nullCheck() && targetFinder.target != null
}