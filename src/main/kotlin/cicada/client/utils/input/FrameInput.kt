package cicada.client.utils.input

import cicada.client.utils.mc
import org.joml.Vector2f
import org.lwjgl.glfw.GLFW

// created by dicves_recode on 21.02.2026
object FrameInput {
    var MPos = Vector2f(0f, 0f)
    val pressed = booleanArrayOf(false, false, false)
    private val lastPressed = booleanArrayOf(false, false, false)
    val clicked = booleanArrayOf(false, false, false)
    val released = booleanArrayOf(false, false, false)
    val scroll = Vector2f(0f, 0f)

    fun update() {
        MPos.set(mc.mouseHandler.getScaledXPos(mc.window), mc.mouseHandler.getScaledYPos(mc.window))

        val handle = mc.window.handle()

        fill(handle, pressed)

        clicked[0] = !lastPressed[0] && pressed[0]
        clicked[1] = !lastPressed[1] && pressed[1]
        clicked[2] = !lastPressed[2] && pressed[2]

        released[0] = lastPressed[0] && !pressed[0]
        released[1] = lastPressed[1] && !pressed[1]
        released[2] = lastPressed[2] && !pressed[2]

        lastPressed[0] = pressed[0]
        lastPressed[1] = pressed[1]
        lastPressed[2] = pressed[2]
    }

    private fun fill(handle: Long, array: BooleanArray) {
        for (i in 0..2)
            array[i] = GLFW.glfwGetMouseButton(handle, i) > 0
    }
}