package cicada.client.feature.module.modules.visual

import cicada.client.feature.module.ModuleCategory
import cicada.client.feature.module.ClientModule
import cicada.client.gui.setting.SettingScreen
import cicada.client.key.Keybind
import cicada.client.utils.client.mc
import org.lwjgl.glfw.GLFW

// SCWGxD regrets everything he did. 30.03.2026 16:09.
object ModuleClientSettingsGUI : ClientModule("ClientSettingsGUI", ModuleCategory.VISUAL, Keybind(GLFW.GLFW_KEY_RIGHT_SHIFT)) {
    override fun onEnable() {
        mc.setScreen(SettingScreen)
        toggle()
    }
}