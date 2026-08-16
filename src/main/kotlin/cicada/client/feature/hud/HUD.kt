package cicada.client.feature.hud

import cicada.client.render.engine.Renderable
import cicada.client.setting.value.ToggleableConfigurable
import net.minecraft.client.gui.GuiGraphicsExtractor

// SCWGxD regrets everything he did. 02.04.2026 11:12.
abstract class HUD(
    var x: Float = 0f,
    var y: Float = 0f,
    var w: Float = 0f,
    var h: Float = 0f,
    name: String,
    defaultToggled: Boolean = true
) : ToggleableConfigurable(name, defaultToggled) {
    var isDragging = false

    init {
        HUDs += this
    }

    abstract fun render(graphics: GuiGraphicsExtractor)

    abstract fun renderInHUDEditor(graphics: GuiGraphicsExtractor)

    fun shouldRender() = toggled
}