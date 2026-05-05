package cicada.client.gui

import cicada.client.utils.mc
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.input.KeyEvent
import net.minecraft.network.chat.Component
import org.joml.Vector2i
import org.lwjgl.glfw.GLFW
import kotlin.random.Random

// SCWGxD regrets everything he did. 31.03.2026 7:00.
object SecretScreen : Screen(Component.empty()) {
    private val sneak = mutableListOf(Vector2i(0))
    private val dir = Vector2i(0)
    private val apple = Vector2i(0)

    override fun init() {
        sneak.clear()
        sneak += Vector2i(0)
        dir.set(0)
        apple.set(Random.nextInt(-20, 20), Random.nextInt(-10, 10))
    }

    override fun tick() {
        sneak.addFirst(Vector2i(sneak[0].x + dir.x, sneak[0].y + dir.y))

        if (sneak[0] == apple) {
            apple.set(Random.nextInt(-20, 20), Random.nextInt(-10, 10))
        } else {
            sneak.removeLast()
        }
    }

    override fun extractRenderState(graphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int, a: Float) {
        for (element in sneak) {
            graphics.fill(
                mc.window.guiScaledWidth / 2 + element.x * 20 - 10,
                mc.window.guiScaledHeight / 2 + element.y * 20 - 10,
                mc.window.guiScaledWidth / 2 + element.x * 20 + 10,
                mc.window.guiScaledHeight / 2 + element.y * 20 + 10,
                0xFF00FF00.toInt()
            )
        }

        graphics.fill(
            mc.window.guiScaledWidth / 2 + apple.x * 20 - 10,
            mc.window.guiScaledHeight / 2 + apple.y * 20 - 10,
            mc.window.guiScaledWidth / 2 + apple.x * 20 + 10,
            mc.window.guiScaledHeight / 2 + apple.y * 20 + 10,
            0xFFFF0000.toInt()
        )
    }

    override fun keyPressed(event: KeyEvent): Boolean {
        val key = event.key

        when (key) {
            GLFW.GLFW_KEY_W -> dir.set(0, -1)
            GLFW.GLFW_KEY_D -> dir.set(1, 0)
            GLFW.GLFW_KEY_S -> dir.set(0, 1)
            GLFW.GLFW_KEY_A -> dir.set(-1, 0)
        }

        return super.keyPressed(event)
    }
}