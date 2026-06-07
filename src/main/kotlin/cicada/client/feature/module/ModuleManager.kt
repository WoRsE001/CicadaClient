package cicada.client.feature.module

import cicada.client.config.types.Value
import cicada.client.feature.module.modules.combat.ModuleAntiKB
import cicada.client.feature.module.modules.combat.ModuleAttackAura
import cicada.client.feature.module.modules.combat.ModuleAutoClicker
import cicada.client.feature.module.modules.combat.ModuleMaceHelper
import cicada.client.feature.module.modules.combat.ModulePing
import cicada.client.feature.module.modules.combat.ModuleSprintReset
import cicada.client.feature.module.modules.combat.ModuleTeleportAura
import cicada.client.feature.module.modules.misc.ModuleFixes
import cicada.client.feature.module.modules.misc.ModuleMurderMysteryHelper
import cicada.client.feature.module.modules.misc.ModuleTeams
import cicada.client.feature.module.modules.movement.ModuleFastClimb
import cicada.client.feature.module.modules.movement.ModuleFlight
import cicada.client.feature.module.modules.movement.ModuleNoSlowDown
import cicada.client.feature.module.modules.movement.ModuleSpeed
import cicada.client.feature.module.modules.movement.ModuleSprint
import cicada.client.feature.module.modules.player.ModuleAutoSoup
import cicada.client.feature.module.modules.player.ModuleAutoSwap
import cicada.client.feature.module.modules.player.ModuleExtraC03
import cicada.client.feature.module.modules.player.ModuleInventoryCleaner
import cicada.client.feature.module.modules.player.ModuleMultiAction
import cicada.client.feature.module.modules.visual.ModuleAmbient
import cicada.client.feature.module.modules.visual.ModuleAspectRatio
import cicada.client.feature.module.modules.visual.ModuleClientSettingsGUI
import cicada.client.feature.module.modules.visual.ModuleESP
import cicada.client.feature.module.modules.visual.ModuleFreelook
import cicada.client.feature.module.modules.visual.ModuleHandPosition
import cicada.client.feature.module.modules.visual.ModuleNoRender
import cicada.client.feature.module.modules.visual.ModuleOverlay
import cicada.client.feature.module.modules.visual.ModuleSecret
import cicada.client.feature.module.modules.world.ModuleAutoBridge
import cicada.client.feature.module.modules.world.ModuleBridgeAssist
import cicada.client.feature.module.modules.world.ModuleFastPlace
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonObject

// SCWGxD regrets everything he did. 30.03.2026 11:38.
@Suppress("UNUSED_EXPRESSION")
object ModuleManager : Value<MutableList<ClientModule>>("ModuleManager", mutableListOf()) {
    init {
        // combat
        ModuleAntiKB
        ModuleAttackAura
        ModuleAutoClicker
        ModuleMaceHelper
        ModulePing
        ModuleSprintReset
        ModuleTeleportAura

        // misc
        ModuleFixes
        ModuleMurderMysteryHelper
        ModuleTeams

        // movement
        ModuleFastClimb
        ModuleFlight
        ModuleNoSlowDown
        ModuleSpeed
        ModuleSprint

        // player
        ModuleAutoSoup
        ModuleAutoSwap
        ModuleExtraC03
        ModuleInventoryCleaner
        ModuleMultiAction

        // visual
        ModuleAmbient
        ModuleAspectRatio
        ModuleClientSettingsGUI
        ModuleESP
        ModuleFreelook
        ModuleHandPosition
        ModuleNoRender
        ModuleOverlay
        ModuleSecret

        // world
        ModuleAutoBridge
        ModuleBridgeAssist
        ModuleFastPlace
    }

    internal operator fun plusAssign(module: ClientModule) {
        if (module !in inner)
            inner += module
    }

    override fun serializeTo(): JsonObject = buildJsonObject {
        for (module in inner) {
            put(module.name, module.serializeTo())
        }
    }

    override fun deserializeFrom(jsonObject: JsonObject) {
        for (module in inner) {
            module.deserializeFrom(jsonObject[module.name]?.jsonObject ?: run { continue })
        }
    }
}