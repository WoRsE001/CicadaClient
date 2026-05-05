package cicada.client.module

import cicada.client.event.Event
import cicada.client.event.EventListener
import cicada.client.key.KeyListener
import cicada.client.key.Keybind
import cicada.client.setting.ToggleableConfigureable
import cicada.client.sound.SIGMA_TOGGLE
import cicada.client.sound.play
import cicada.client.utils.input.FrameInput
import cicada.client.utils.math.Rect
import cicada.client.utils.nullCheck
import cicada.client.utils.render.rect
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import net.minecraft.client.gui.GuiGraphicsExtractor

// SCWGxD regrets everything he did. 30.03.2026 11:30.
abstract class Module(
    name: String,
    val category: Category,
    override var keybind: Keybind = Keybind(),
    defaultToggled: Boolean = false
) : ToggleableConfigureable(name, defaultToggled), EventListener, KeyListener {
    override var json: JsonObject
        get() = buildJsonObject {
            super.json
        }
        set(value) {
            super.json = value
        }

    override var toggled = defaultToggled
        set(value) {
            if (field != value) {
                field = value

                if (field)
                    onEnable()
                else
                    onDisable()
            }
        }

    override val rect = Rect(0f, 0f, 150f, 29f)
    override val rect1 = Rect(0f, 0f, 150f, 29f)

    init {
        registerToEvents()
        registerToKeybinds()
        Modules += this
        category += this
    }

    override fun onEvent(event: Event) {}
    
    override fun listenEvents() = toggled && nullCheck()

    override fun onKey(action: Int) {
        if (action == 2)
            return

        val pressed = action == 1

        if (keybind.hold) {
            toggled = pressed
        } else if (!pressed) {
            toggle()
        }
    }

    override fun listenKeybinds() = nullCheck()

    override fun render(graphics: GuiGraphicsExtractor) {
        rect1.x = rect.x
        rect1.y = rect.y

        rect.h = 29f
        if (isShowSettings) {
            rect.h += 2
            for (setting in inner) {
                if (!setting.visible.invoke()) continue
                rect.h += setting.rect.h + 2
            }
        }

        if (rect1.isCollide(FrameInput.MPos)) {
            if (FrameInput.clicked[0])
                toggle()
            if (inner.isNotEmpty() && FrameInput.clicked[1])
                isShowSettings = !isShowSettings
        }

        val textColor = if (toggled) 0xFF3776FF.toInt() else -1
        //graphics.drawText(name, rect1.x + rect1.w / 2 - mc.font.width(name) / 2, rect1.y + 5, textColor)
        if (inner.isNotEmpty()) {
            val suffix = if (isShowSettings) "-" else "+"
            //graphics.drawText(suffix, rect1.x + rect1.w - mc.font.width(suffix) / 2 - 10, rect1.y + 5, -1)

            if (isShowSettings) {
                var offsetY = 2f
                for (setting in inner) {
                    if (!setting.visible.invoke()) continue
                    setting.rect.x = rect.x + 7
                    setting.rect.y = rect.y + rect1.h + offsetY
                    setting.render(graphics)
                    offsetY += setting.rect.h + 2
                }
            }

            graphics.rect(rect.x, rect.y + rect1.h, 2f, rect.h - rect1.h - 2, 0xFF3776FF.toInt())
        }
    }
}