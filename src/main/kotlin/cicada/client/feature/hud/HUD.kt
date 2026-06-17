package cicada.client.feature.hud

import cicada.client.setting.ToggleableConfigurable
import cicada.client.render.Renderable
import net.minecraft.client.gui.GuiGraphicsExtractor

// SCWGxD regrets everything he did. 02.04.2026 11:12.
abstract class HUD(
    var x: Float,
    var y: Float,
    var w: Float,
    var h: Float,
    name: String,
    defaultToggled: Boolean = true
) : ToggleableConfigurable(name, defaultToggled), Renderable {
    var isDragging = false

    init {
        HUDManager += this
    }

    override fun render(graphics: GuiGraphicsExtractor) {}

    fun shouldRender() = toggled
}