package cicada.client.feature.hud

import cicada.client.feature.hud.huds.HUDDebug
import cicada.client.feature.hud.huds.HUDKeystrokes
import cicada.client.feature.hud.huds.modulelist.HUDModuleList
import cicada.client.feature.hud.huds.HUDTargetInfo
import cicada.client.feature.module.Modules.iterator
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonObject
import net.minecraft.client.gui.GuiGraphicsExtractor

// SCWGxD regrets everything he did. 02.04.2026 11:22.
@Suppress("UNUSED_EXPRESSION")
object HUDs : ArrayList<HUD>() {
    init {
        HUDDebug
        HUDKeystrokes
        HUDModuleList
        HUDTargetInfo
    }

    operator fun plusAssign(HUD: HUD) {
        if (HUD !in this)
            add(HUD)
    }

    fun render(graphics: GuiGraphicsExtractor) {
        for (HUD in this)
            if (HUD.shouldRender())
                HUD.render(graphics)
    }

    fun renderInHUDEditor(graphics: GuiGraphicsExtractor) {
        for (HUD in this)
            if (HUD.toggled)
                HUD.renderInHUDEditor(graphics)
    }

    fun serializeTo(): JsonObject = buildJsonObject {
        for (HUD in this@HUDs) {
            put(HUD.name, HUD.asJson())
        }
    }

    fun deserializeFrom(jsonObject: JsonObject) {
        for (HUD in this) {
            HUD.fromJson(jsonObject[HUD.name]?.jsonObject ?: run { continue })
        }
    }

    private fun readResolve(): Any = HUDs
}