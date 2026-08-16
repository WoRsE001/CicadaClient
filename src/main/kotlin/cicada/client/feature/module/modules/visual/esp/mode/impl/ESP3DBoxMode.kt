package cicada.client.feature.module.modules.visual.esp.mode.impl

import cicada.client.event.Event
import cicada.client.event.events.EventRender
import cicada.client.feature.module.modules.misc.ModuleMurderMysteryHelper
import cicada.client.feature.module.modules.visual.esp.mode.ESPMode
import cicada.client.render.Renderer3D
import cicada.client.render.engine.FILLED_QUAD_TYPE
import cicada.client.utils.math.Color4f
import cicada.client.utils.math.boundingBox
import cicada.client.utils.player.isDetective
import cicada.client.utils.player.isMurder
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player

// SCWGxD regrets everything he did. 04.04.2026 15:40.
class ESP3DBoxMode : ESPMode("Box") {
    override val color = color("Color", Color4f(1f, 1f, 1f, 1f))

    override fun onEvent(event: Event, entity: Entity) {
        if (event !is EventRender.World)
            return

        val color = if (
            ModuleMurderMysteryHelper.toggled &&
            ModuleMurderMysteryHelper.rolesHighlight.toggled &&
            entity is Player
        ) {
            if (entity.isMurder)
                ModuleMurderMysteryHelper.murdersColor.inner
            else if (entity.isDetective)
                ModuleMurderMysteryHelper.detectivesColor.inner
            else
                color.inner
        } else color.inner

        Renderer3D.box(
            event.bufferSource, event.poseStack, FILLED_QUAD_TYPE,
            entity.boundingBox(event.deltaTracker.getGameTimeDeltaPartialTick(false)),
            color
        )
    }
}