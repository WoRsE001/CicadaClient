package cicada.client.module

import cicada.client.utils.input.FrameInput
import cicada.client.utils.math.Rect
import cicada.client.utils.mc
import cicada.client.utils.render.RenderableObject
import cicada.client.utils.render.rect
import net.minecraft.client.gui.GuiGraphicsExtractor

enum class Category : RenderableObject {
    COMBAT,
    MISC,
    MOVEMENT,
    PLAYER,
    VISUAL,
    WORLD;

    private val _modules = mutableListOf<Module>()

    val modules: List<Module>
        get() = _modules

    override val rect = Rect(ordinal * 125f, 0f, 150f, 320f)
    val rect1 = Rect(0f, 0f, 150f, 29f)

    internal operator fun plusAssign(module: Module) {
        if (module !in _modules)
            _modules += module
    }

    var isShowModules = true
    var isDragging = false
    var deltaX = 0f
    var deltaY = 0f

    override fun render(graphics: GuiGraphicsExtractor) {
        if (rect1.isCollide(FrameInput.MPos)) {
            if (!isDragging && FrameInput.clicked[0]) {
                isDragging = true
                deltaX = rect.x - FrameInput.MPos.x
                deltaY = rect.y - FrameInput.MPos.y
            }

            if (FrameInput.clicked[1]) {
                isShowModules = !isShowModules
                if (isShowModules)
                    rect.h = 290f
                else
                    rect.h = 29f
            }
        }

        if (isDragging && FrameInput.released[0])
            isDragging = false

        if (isDragging) {
            rect.x = (FrameInput.MPos.x + deltaX).coerceIn(0f, mc.window.guiScaledWidth - rect.w)
            rect.y = (FrameInput.MPos.y + deltaY).coerceIn(0f, mc.window.guiScaledHeight - rect.h)
        }

        rect1.x = rect.x
        rect1.y = rect.y

        graphics.rect(rect.x, rect.y, rect.w, rect.h, 0xFF000000.toInt(), 5f)
        //graphics.drawText(name, rect.x + 10, rect.y + 10, -1)
        val suffix = if (isShowModules) "-" else "+"
        //graphics.drawText(suffix, rect.x + rect.w - mc.font.width(suffix) - 10, rect.y + 10, -1)

        if (isShowModules) {
            graphics.rect(rect.x, rect.y + 29, rect.w, 2f, 0xFF3776FF.toInt())
            var offsetY = 0f
            for (module in modules) {
                module.rect.x = rect.x
                module.rect.y = rect.y + offsetY + 31
                module.render(graphics)
                offsetY += module.rect.h
            }
        }
    }
}