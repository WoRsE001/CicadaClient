package cicada.client.feature.module.modules.misc

import cicada.client.event.Event
import cicada.client.event.impl.TickEvent
import cicada.client.feature.module.ClientModule
import cicada.client.feature.module.ModuleCategory
import cicada.client.setting.preset.TargetFinder
import cicada.client.utils.client.player
import cicada.client.utils.math.minus
import cicada.client.utils.player.canCrit
import cicada.client.rotation.Rotation
import cicada.client.rotation.ai.Sample
import cicada.client.utils.rotation.rotation
import kotlinx.serialization.json.*
import net.minecraft.world.entity.player.Input
import net.minecraft.world.phys.Vec3

// SCWGxD regrets everything he did. 24.06.2026 12:45.
object ModuleRatkaEXE : ClientModule("RatkaEXE", ModuleCategory.MISC) {
    private val targetFinder = tree(TargetFinder())

    val dataSet = mutableListOf<Sample>()
    private var lastRotation = Rotation(0f, 0f)

    override fun onEvent(event: Event) {
        if (event is TickEvent.Pre) {
            targetFinder.updateTarget()

            if (targetFinder.target != null) {
                val target = targetFinder.target!!

                dataSet += Sample(
                    target.position() - player.position(),
                    (player.rotation() - lastRotation).wrapped(),
                    player.canCrit(),
                    player.hurtTime,
                    target.hurtTime,
                    player.input.keyPresses
                )
            }

            lastRotation = player.rotation()
        }
    }
}