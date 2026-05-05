package cicada.client.module.impl.visual

import cicada.client.module.Category
import cicada.client.module.Module

// испорченно SCWGxD в 27.12.2025:20:49
object ModuleAspectRatio : Module(
    "AspectRatio",
    Category.VISUAL
) {
    val factor by float("Factor", 1.3f, 0f..2f)
}