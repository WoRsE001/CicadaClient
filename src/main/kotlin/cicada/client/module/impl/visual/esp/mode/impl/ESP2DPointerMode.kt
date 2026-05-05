package cicada.client.module.impl.visual.esp.mode.impl

import cicada.client.event.Event
import cicada.client.event.impl.RenderEvent
import cicada.client.module.impl.misc.ModuleMurderMysteryHelper
import cicada.client.module.impl.visual.esp.mode.ESPMode
import cicada.client.utils.math.Color4f
import cicada.client.utils.mc
import cicada.client.utils.player
import cicada.client.utils.player.isDetective
import cicada.client.utils.player.isMurder
import cicada.client.utils.render.triangle
import cicada.client.utils.rotation.yawTo
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import org.joml.Math
import org.joml.Matrix3x2f
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

// SCWGxD regrets everything he did. 12.04.2026 1:40.
// TODO: FIX
class ESP2DPointerMode : ESPMode("Pointer") {
    override val color = color("Color", Color4f(0f, 0f, 0f, 1f))

    override fun onEvent(event: Event, entity: Entity) {
        if (event is RenderEvent.Gui.Post) {
            val radius = 70f
            val size = 10f

            val angle = player.yRot - yawTo(entity.eyePosition) + 90f
            val x = mc.window.guiScaledWidth / 2f + cos(Math.toRadians(angle)) * radius
            val y = mc.window.guiScaledHeight / 2f + -sin(Math.toRadians(angle)) * radius

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

            event.graphics.pose().rotateAbout(-Math.toRadians(angle + 90f), x, y)

            // равнобедренный треуг.
            event.graphics.triangle(
                Matrix3x2f(event.graphics.pose()),
                x + sqrt(size.pow(2) * 0.75f), y - size / 2,
                x - sqrt(size.pow(2) * 0.75f), y - size / 2,
                x, y + size,
                color.toInt(), color.toInt(), color.toInt()
            )

            event.graphics.pose().rotateAbout(Math.toRadians(angle + 90f), x, y)
        }
    }
}