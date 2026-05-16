package cicada.client.gui.setting_client.values_renderers

import cicada.client.config.types.BooleanValue
import cicada.client.config.types.Value
import cicada.client.utils.input.FrameInput
import cicada.client.utils.math.Rect
import cicada.client.utils.render.RenderableObject
import cicada.client.utils.render.font.INTER_BOLD_FONT
import cicada.client.utils.render.rect
import net.minecraft.client.gui.GuiGraphicsExtractor

abstract class ValueRenderer<T : Value<*>>(val value: T) : RenderableObject {
    abstract override val rect: Rect
}

class BooleanValueRenderer(value: BooleanValue) : ValueRenderer<BooleanValue>(value) {
    val flagRect = Rect(0f, 0f, INTER_BOLD_FONT.height(9f), INTER_BOLD_FONT.height(9f))
    val textRect = Rect(0f, 0f, INTER_BOLD_FONT.width(value.name, 9f), INTER_BOLD_FONT.height(9f))
    override val rect = Rect(0f, 0f, flagRect.w + textRect.w, flagRect.h + 2f + textRect.h)

    override fun render(graphics: GuiGraphicsExtractor) {
        flagRect.x = rect.x
        flagRect.y = rect.y
        textRect.x = flagRect.x + flagRect.w + 2f
        textRect.y = flagRect.y

        if (FrameInput.clicked[0] && flagRect.isCollide(FrameInput.MPos)) {
            value.toggle()
        }

        val flagColor = if (value.inner) 0xFFA478E8.toInt() else 0xFFFFD700.toInt()
        graphics.rect(flagRect, flagColor, 0f)
        INTER_BOLD_FONT.draw(graphics, value.name, textRect.x, textRect.y, 0f)
    }
}

fun convertValue(value: Value<*>): ValueRenderer<*> {
    when(value) {
        is BooleanValue -> {
            return BooleanValueRenderer(value)
        }

        else -> {
            throw Exception()
        }
    }
}