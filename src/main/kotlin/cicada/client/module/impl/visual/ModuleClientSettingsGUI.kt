package cicada.client.module.impl.visual

import cicada.client.gui.clientsettings.ClientSettingsScreen
import cicada.client.key.Keybind
import cicada.client.module.Category
import cicada.client.module.Module
import cicada.client.utils.mc
import net.minecraft.client.resources.sounds.SimpleSoundInstance
import net.minecraft.sounds.SoundEvents
import org.lwjgl.glfw.GLFW

// SCWGxD regrets everything he did. 30.03.2026 16:09.
object ModuleClientSettingsGUI : Module("ClientSettingsGUI", Category.VISUAL, Keybind(GLFW.GLFW_KEY_RIGHT_SHIFT)) {
    override fun onEnable() {
        mc.setScreen(ClientSettingsScreen)
        mc.soundManager.play(SimpleSoundInstance.forUI(SoundEvents.AMBIENT_CAVE, 1f))
        toggle()
    }
}