package cicada.client.module.impl.visual

import com.mojang.blaze3d.platform.InputConstants
import cicada.client.key.Keybind
import cicada.client.module.Category
import cicada.client.module.Module
import cicada.client.rotation.CameraRotation
import cicada.client.rotation.RotationListener

object ModuleFreelook : Module(
    "Freelook",
    Category.VISUAL,
    Keybind(InputConstants.KEY_LALT, true)
), RotationListener {
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