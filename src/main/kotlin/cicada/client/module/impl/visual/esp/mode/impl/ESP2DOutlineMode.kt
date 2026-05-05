package cicada.client.module.impl.visual.esp.mode.impl

import cicada.client.event.Event
import cicada.client.event.impl.RenderEvent
import cicada.client.module.impl.misc.ModuleMurderMysteryHelper
import cicada.client.module.impl.visual.esp.mode.ESPMode
import cicada.client.utils.math.Color4f
import cicada.client.utils.math.boundingBox
import cicada.client.utils.math.projectWorldToScreen
import cicada.client.utils.mc
import cicada.client.utils.player.isDetective
import cicada.client.utils.player.isMurder
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import org.joml.Vector3f

// SCWGxD regrets everything he did. 04.04.2026 15:48.
class ESP2DOutlineMode : ESPMode("Outline 2D") {
    override val color = color("Color", Color4f(1f))

    override fun onEvent(event: Event, entity: Entity) {
        if (event !is RenderEvent.Gui.Post)
            return

        val graphics = event.graphics
        val bb = entity.boundingBox(event.deltaTracker.getGameTimeDeltaPartialTick(false))
        val cameraPos = mc.gameRenderer.mainCamera.position().toVector3f()

        // 5 строк вместо 10

        /*val points = mutableListOf(
            Vector3f(bb.minX.toFloat(), bb.minY.toFloat(), bb.minZ.toFloat()),
            Vector3f(bb.maxX.toFloat(), bb.minY.toFloat(), bb.minZ.toFloat()),
            Vector3f(bb.minX.toFloat(), bb.maxY.toFloat(), bb.minZ.toFloat()),
            Vector3f(bb.maxX.toFloat(), bb.maxY.toFloat(), bb.minZ.toFloat()),
            Vector3f(bb.minX.toFloat(), bb.minY.toFloat(), bb.maxZ.toFloat()),
            Vector3f(bb.maxX.toFloat(), bb.minY.toFloat(), bb.maxZ.toFloat()),
            Vector3f(bb.minX.toFloat(), bb.maxY.toFloat(), bb.maxZ.toFloat()),
            Vector3f(bb.maxX.toFloat(), bb.maxY.toFloat(), bb.maxZ.toFloat())
        )*/

        val points = List(8) { i ->
            Vector3f(
                if (i and 0x1 == 1) bb.maxX.toFloat() else bb.minX.toFloat(),
                if (i ushr 1 and 0x1 == 1) bb.maxY.toFloat() else bb.minY.toFloat(),
                if (i ushr 2 and 0x1 == 1) bb.maxZ.toFloat() else bb.minZ.toFloat()
            )
        }

        val positions = points.map { projectWorldToScreen(it, cameraPos) ?: return }

        val minX = positions.minOf { it.x }
        val maxX = positions.maxOf { it.x }
        val minY = positions.minOf { it.y }
        val maxY = positions.maxOf { it.y }

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

        graphics.pose().scale(1f / mc.window.guiScale)

        graphics.horizontalLine(minX, maxX, minY, color.toInt())
        graphics.horizontalLine(minX, maxX, maxY, color.toInt())
        graphics.verticalLine(minX, minY, maxY, color.toInt())
        graphics.verticalLine(maxX, minY, maxY, color.toInt())

        graphics.pose().scale(mc.window.guiScale.toFloat())
    }
}