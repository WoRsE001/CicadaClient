package cicada.client.module.impl.visual.handposition

import com.mojang.blaze3d.vertex.PoseStack
import cicada.client.setting.ToggleableConfigureable
import net.minecraft.world.InteractionHand

// SCWGxD regrets everything he did. 18.04.2026 4:08.
class SettingHandPosition(name: String) : ToggleableConfigureable(name, false) {
    val xOffset by float("X offset", 0f, -3f..3f)
    val yOffset by float("Y offset", 0f, -3f..3f)
    val zOffset by float("Z offset", 0f, -3f..3f)

    fun transform(poseStack: PoseStack) {
        if (!toggled) return
        poseStack.translate(xOffset, yOffset, zOffset)
    }
}