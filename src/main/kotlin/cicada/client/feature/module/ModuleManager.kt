package cicada.client.feature.module

import cicada.client.feature.module.modules.combat.ModuleAntiKB
import cicada.client.feature.module.modules.combat.ModuleAttributeSwapper
import cicada.client.feature.module.modules.combat.attackaura.ModuleAttackAura
import cicada.client.feature.module.modules.combat.ModuleAutoClicker
import cicada.client.feature.module.modules.combat.ModulePing
import cicada.client.feature.module.modules.combat.ModuleSprintReset
import cicada.client.feature.module.modules.combat.ModuleTPAttackAura
import cicada.client.feature.module.modules.misc.ModuleFixes
import cicada.client.feature.module.modules.misc.ModuleMurderMysteryHelper
import cicada.client.feature.module.modules.misc.ModuleRatkaEXE
import cicada.client.feature.module.modules.misc.ModuleTeams
import cicada.client.feature.module.modules.movement.ModuleFastClimb
import cicada.client.feature.module.modules.movement.ModuleFlight
import cicada.client.feature.module.modules.movement.ModuleNoSlowDown
import cicada.client.feature.module.modules.movement.speed.ModuleSpeed
import cicada.client.feature.module.modules.movement.ModuleSprint
import cicada.client.feature.module.modules.player.ModuleAutoOffhand
import cicada.client.feature.module.modules.player.ModuleAutoTool
import cicada.client.feature.module.modules.player.ModuleExtraC03
import cicada.client.feature.module.modules.player.ModuleInventoryCleaner
import cicada.client.feature.module.modules.player.ModuleMultiAction
import cicada.client.feature.module.modules.visual.ModuleAmbient
import cicada.client.feature.module.modules.visual.ModuleAspectRatio
import cicada.client.feature.module.modules.visual.ModuleESP
import cicada.client.feature.module.modules.visual.ModuleFreelook
import cicada.client.feature.module.modules.visual.ModuleHandPosition
import cicada.client.feature.module.modules.visual.ModuleNoRender
import cicada.client.feature.module.modules.visual.ModuleOverlay
import cicada.client.feature.module.modules.visual.ModuleSettingGUI
import cicada.client.feature.module.modules.world.ModuleAutoBridge
import cicada.client.feature.module.modules.world.ModuleBridgeAssist
import cicada.client.feature.module.modules.world.ModuleFastPlace
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonObject

// SCWGxD regrets everything he did. 30.03.2026 11:38.
@Suppress("UNUSED_EXPRESSION")
object ModuleManager : ArrayList<ClientModule>() {
    init {
        // combat
        ModuleAntiKB
        ModuleAttackAura
        ModuleAttributeSwapper
        ModuleAutoClicker
        ModulePing
        ModuleSprintReset
        ModuleTPAttackAura

        // misc
        ModuleFixes
        ModuleMurderMysteryHelper
        ModuleRatkaEXE
        ModuleTeams

        // movement
        ModuleFastClimb
        ModuleFlight
        ModuleNoSlowDown
        ModuleSpeed
        ModuleSprint

        // player
        ModuleAutoOffhand
        ModuleAutoTool
        ModuleExtraC03
        ModuleInventoryCleaner
        ModuleMultiAction

        // visual
        ModuleAmbient
        ModuleAspectRatio
        ModuleESP
        ModuleFreelook
        ModuleHandPosition
        ModuleNoRender
        ModuleOverlay
        ModuleSettingGUI

        // world
        ModuleAutoBridge
        ModuleBridgeAssist
        ModuleFastPlace
    }

    operator fun plusAssign(module: ClientModule) {
        if (module !in this)
            this.add(module)
    }

    fun serializeTo(): JsonObject = buildJsonObject {
        for (module in this@ModuleManager) {
            put(module.name, module.asJson())
        }
    }

    fun deserializeFrom(jsonObject: JsonObject) {
        for (module in this) {
            module.fromJson(jsonObject[module.name]?.jsonObject ?: run { continue })
        }
    }

    private fun readResolve(): Any = ModuleManager
}