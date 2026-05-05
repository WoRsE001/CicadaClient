package cicada.client.setting.preset.targetrenderer.impl

import cicada.client.CicadaClient
import cicada.client.event.Event
import cicada.client.event.impl.RenderEvent
import cicada.client.setting.preset.targetrenderer.TargetRenderMode
import cicada.client.utils.math.projectWorldToScreen
import cicada.client.utils.mc
import cicada.client.utils.render.image
import net.minecraft.world.entity.Entity
import org.lwjgl.glfw.GLFW
import kotlin.math.sin

// SCWGxD regrets everything he did. 13.04.2026 7:51.
class TargetRender2DRectMode : TargetRenderMode("2D rect") {
    private val size by int("Size", 150, 1..500)

    override fun render(event: Event, target: Entity) {
        if (event !is RenderEvent.Gui.Post)
            return

        val graphics = event.graphics
        val cameraPos = mc.gameRenderer.mainCamera.position().toVector3f()
        val renderPosition = projectWorldToScreen(target.boundingBox.center.toVector3f(), cameraPos) ?: return

        graphics.pose().pushMatrix()

        graphics.pose()
            .scale(1f / mc.window.guiScale)
            .rotateAbout(sin(GLFW.glfwGetTime().toFloat() / 100) * 360, renderPosition.x.toFloat(), renderPosition.y.toFloat())
            //.rotateAbout(theta.toFloat(), 0f, 0f)

        graphics.image(
            renderPosition.x - size / 2f, renderPosition.y - size / 2f, size.toFloat(), size.toFloat(),
            CicadaClient.of("images/targetmarkers/target_marker_0.png")
        )

        graphics.pose().popMatrix()
    }
}