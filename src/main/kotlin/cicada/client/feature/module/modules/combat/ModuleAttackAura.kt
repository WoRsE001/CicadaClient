package cicada.client.feature.module.modules.combat

import cicada.client.event.Event
import cicada.client.event.impl.TickEvent
import cicada.client.feature.module.ModuleCategory
import cicada.client.feature.module.ClientModule
import cicada.client.feature.module.modules.combat.attackaura.attack.AttackAuraAttacker
import cicada.client.feature.module.modules.combat.attackaura.aim.AttackAuraAimer
import cicada.client.feature.module.modules.combat.attackaura.autoblock.AttackAuraAutoBlock
import cicada.client.rotation.Rotator
import cicada.client.config.types.preset.MovementCorrector
import cicada.client.config.types.preset.TargetFinder
import cicada.client.config.types.preset.TargetRenderer
import cicada.client.utils.client.nullCheck
import cicada.client.utils.client.player
import cicada.client.utils.rotation.rotate

// SCWGxD regrets everything he did. 04.04.2026 5:37.
object ModuleAttackAura : ClientModule("AttackAura", ModuleCategory.COMBAT, description = "Атакует пидора"), Rotator {
    val targetFinder = tree(TargetFinder())
    private val aimer = tree(AttackAuraAimer)
    private val attacker = tree(AttackAuraAttacker)
    private val movementCorrector = tree(MovementCorrector())
    private val autoblock = tree(AttackAuraAutoBlock)
    private val targetRenderer = tree(TargetRenderer())

    override val rotatePriority = 0

    init {
        registerToRotations()
    }

    override fun onDisable() {
        autoblock.unBlock()
        targetFinder.resetTarget()
    }

    override fun onEvent(event: Event) {
        if (event is TickEvent.Pre) {
            targetFinder.updateTarget()
        }

        targetFinder.target?.let {
            attacker.onEvent(event, targetFinder.target!!)
            autoblock.onEvent(event, targetFinder.target!!)
            targetRenderer.render(event, targetFinder.target!!)
        }
    }

    override fun rotate() {
        player.rotate(aimer.delta(targetFinder.target!!))
    }

    override fun willRotate() = toggled && nullCheck() && targetFinder.target != null
}