package cicada.client.feature.module.modules.visual

import cicada.client.feature.module.ModuleCategory
import cicada.client.feature.module.ClientModule
import cicada.client.gui.SecretScreen
import cicada.client.key.Keybind
import cicada.client.utils.client.mc
import org.lwjgl.glfw.GLFW

// SCWGxD regrets everything he did. 31.03.2026 7:00.
object ModuleSecret : ClientModule("ClientSettingsGUI", ModuleCategory.VISUAL, Keybind(GLFW.GLFW_KEY_HOME)) {
    override fun onEnable() {
        mc.setScreen(SecretScreen)
        toggle()
    }
}