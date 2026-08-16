package cicada.client.feature.module

import cicada.client.feature.module.modules.combat.*
import cicada.client.feature.module.modules.combat.antikb.ModuleAntiKB
import cicada.client.feature.module.modules.combat.attackaura.ModuleAttackAura
import cicada.client.feature.module.modules.combat.sprintreset.ModuleSprintReset
import cicada.client.feature.module.modules.misc.ModuleAutoAuth
import cicada.client.feature.module.modules.misc.ModuleKTLeave
import cicada.client.feature.module.modules.misc.ModuleMurderMysteryHelper
import cicada.client.feature.module.modules.misc.ModuleTeams
import cicada.client.feature.module.modules.movement.ModuleMovementHelper
import cicada.client.feature.module.modules.movement.noslowdown.ModuleNoSlowDown
import cicada.client.feature.module.modules.movement.ModuleSprint
import cicada.client.feature.module.modules.movement.fastclimb.ModuleFastClimb
import cicada.client.feature.module.modules.movement.flight.ModuleFlight
import cicada.client.feature.module.modules.movement.speed.ModuleSpeed
import cicada.client.feature.module.modules.player.*
import cicada.client.feature.module.modules.player.nofalldamage.ModuleNoFallDamage
import cicada.client.feature.module.modules.player.phase.ModulePhase
import cicada.client.feature.module.modules.visual.*
import cicada.client.feature.module.modules.visual.druns.ModuleDruns
import cicada.client.feature.module.modules.world.ModuleBridgeAssist
import cicada.client.feature.module.modules.world.ModuleErsaBypass
import cicada.client.feature.module.modules.world.ModuleFastBreak
import cicada.client.feature.module.modules.world.autobridge.ModuleAutoBridge
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonObject

// SCWGxD regrets everything he did. 30.03.2026 11:38.
@Suppress("UNUSED_EXPRESSION")
object Modules : ArrayList<ClientModule>() {
    init {
        // combat
        ModuleAntiKB
        ModuleAimAssist
        ModuleAttackAura
        ModuleAttributeSwapper
        ModuleAutoAttack
        ModuleAutoClicker
        ModulePing
        ModulePulseBlink
        ModuleSprintReset
        ModuleTPAttackAura

        // misc
        ModuleAutoAuth
        ModuleKTLeave
        ModuleMurderMysteryHelper
        ModuleTeams

        // movement
        ModuleFastClimb
        ModuleFlight
        ModuleMovementHelper
        ModuleNoSlowDown
        ModuleSpeed
        ModuleSprint

        // player
        ModuleAutoOffhand
        ModuleAutoTool
        ModuleChestStealer
        ModuleExtraC03
        ModuleNoFallDamage
        ModuleInventoryCleaner
        ModuleMultiAction
        ModulePhase

        // visual
        ModuleAmbient
        ModuleAspectRatio
        ModuleDruns
        ModuleESP
        ModuleFreelook
        ModuleHandPosition
        ModuleNoRender
        ModuleOverlay
        ModuleSettingGUI
        ModuleNoisePointSelect

        // world
        ModuleAutoBridge
        ModuleBridgeAssist
        ModuleErsaBypass
        ModuleFastBreak
    }

    operator fun plusAssign(module: ClientModule) {
        if (module !in this)
            add(module)
    }

    fun serializeTo(): JsonObject = buildJsonObject {
        for (module in this@Modules) {
            put(module.name, module.asJson())
        }
    }

    fun deserializeFrom(jsonObject: JsonObject) {
        for (module in this) {
            module.fromJson(jsonObject[module.name]?.jsonObject ?: run { continue })
        }
    }

    private fun readResolve(): Any = Modules
}