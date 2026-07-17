package cicada.client.feature.module.modules.visual

import cicada.client.feature.module.ClientModule
import cicada.client.feature.module.ModuleCategory
import cicada.client.key.Keybind
import cicada.client.rotation.CameraRotation
import cicada.client.rotation.Rotator
import com.mojang.blaze3d.platform.InputConstants

object ModuleFreelook : ClientModule(
    "Freelook",
    ModuleCategory.VISUAL,
    Keybind(InputConstants.KEY_LALT, true)
), Rotator {
    override val rotatePriority = 0

    init {
        registerToRotations()
    }

    override fun onDisable() {
        CameraRotation.unlocked = false
    }

    override fun rotate() {}
    override fun willRotate() = listenEvents()
}