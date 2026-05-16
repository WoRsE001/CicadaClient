package cicada.client.feature.module.modules.visual

import cicada.client.feature.module.ModuleCategory
import cicada.client.feature.module.ClientModule

// испорченно SCWGxD в 27.12.2025:20:49
object ModuleAspectRatio : ClientModule(
    "AspectRatio",
    ModuleCategory.VISUAL
) {
    val factor by float("Factor", 1.3f, 0f..2f)
}