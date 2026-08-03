package cicada.client.feature.module.modules.visual

import cicada.client.feature.module.ClientModule
import cicada.client.feature.module.ModuleCategory
import cicada.client.gui.SettingScreen
import cicada.client.key.Keybind
import cicada.client.utils.client.mc
import org.lwjgl.glfw.GLFW

// SCWGxD regrets everything he did. 22.06.2026 15:18.
object ModuleSettingGUI : ClientModule(
    "SettingGUI",
    ModuleCategory.VISUAL,
    Keybind(GLFW.GLFW_KEY_RIGHT_SHIFT)
) {
    override fun onEnable() {
        mc.setScreen(SettingScreen)
        toggle()
    }
}