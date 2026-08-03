package cicada.client.feature.module.modules.combat

import cicada.client.event.Event
import cicada.client.event.impl.LegitClickTimingEvent
import cicada.client.feature.module.ClientModule
import cicada.client.feature.module.ModuleCategory
import cicada.client.mixin.accessors.AccessorMinecraft
import cicada.client.utils.client.mc
import cicada.client.utils.client.player
import cicada.client.utils.player.canCrit
import net.minecraft.world.phys.HitResult

// SCWGxD regrets everything he did. 22.07.2026 17:20.
object ModuleAutoAttack : ClientModule("AutoAttack", ModuleCategory.COMBAT) {
    private val onlyCrit by boolean("Only crit", false)

    override fun onEvent(event: Event) {
        if (event is LegitClickTimingEvent) {
            if ((player.onGround() && !onlyCrit || player.canCrit()) && player.getAttackStrengthScale(0.5f) > 0.9 && mc.hitResult?.type == HitResult.Type.ENTITY) {
                (mc as AccessorMinecraft).invokeStartAttack()
            }
        }
    }
}