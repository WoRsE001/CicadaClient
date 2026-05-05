package cicada.client.module.impl.visual

import cicada.client.gui.SecretScreen
import cicada.client.key.Keybind
import cicada.client.module.Category
import cicada.client.module.Module
import cicada.client.utils.mc
import org.lwjgl.glfw.GLFW

// SCWGxD regrets everything he did. 31.03.2026 7:00.
object ModuleSecret : Module("ClientSettingsGUI", Category.VISUAL, Keybind(GLFW.GLFW_KEY_HOME)) {
    override fun onEnable() {
        mc.setScreen(SecretScreen)
        toggle()
    }
}