package cicada.client.gui.setting

import cicada.client.gui.setting.modules.SettingModulesScreen
import cicada.client.utils.render.Renderable
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component

// SCWGxD regrets everything he did. 30.03.2026 16:14.
object SettingScreen : Screen(Component.empty()) {
    val settingScreen: Renderable = SettingModulesScreen

    override fun extractRenderState(graphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int, a: Float) {
        settingScreen.render(graphics)
    }
}