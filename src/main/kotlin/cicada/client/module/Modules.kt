package cicada.client.module

import cicada.client.CicadaClient
import cicada.client.module.impl.combat.ModuleAntiKB
import cicada.client.module.impl.combat.ModuleAttackAura
import cicada.client.module.impl.combat.ModuleAutoClicker
import cicada.client.module.impl.combat.ModuleMaceHelper
import cicada.client.module.impl.combat.ModulePing
import cicada.client.module.impl.combat.ModuleSprintReset
import cicada.client.module.impl.misc.ModuleFixes
import cicada.client.module.impl.misc.ModuleMurderMysteryHelper
import cicada.client.module.impl.movement.ModuleFastClimb
import cicada.client.module.impl.movement.ModuleFlight
import cicada.client.module.impl.movement.ModuleNoSlowDown
import cicada.client.module.impl.movement.ModuleSpeed
import cicada.client.module.impl.movement.ModuleSprint
import cicada.client.module.impl.player.ModuleAutoSoup
import cicada.client.module.impl.player.ModuleAutoSwap
import cicada.client.module.impl.player.ModuleExtraC03
import cicada.client.module.impl.player.ModuleMultiAction
import cicada.client.module.impl.visual.*
import cicada.client.module.impl.world.AutoBridge
import cicada.client.setting.SaveLoadable
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonObject

// SCWGxD regrets everything he did. 30.03.2026 11:38.
@Suppress("UNUSED_EXPRESSION")
object Modules : SaveLoadable {
    private val mutableList = mutableListOf<Module>()

    val list: List<Module>
        get() = mutableList

    init {
        // combat
        ModuleAntiKB
        ModuleAttackAura
        ModuleAutoClicker
        ModuleMaceHelper
        ModulePing
        ModuleSprintReset

        // misc
        ModuleFixes
        ModuleMurderMysteryHelper

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
        ModuleMultiAction

        // visual
        ModuleAspectRatio
        ModuleClientSettingsGUI
        ModuleESP
        ModuleFreelook
        ModuleHandPosition
        ModuleNoRender
        ModuleOverlay
        ModuleSecret

        // world
        AutoBridge
    }

    internal operator fun plusAssign(module: Module) {
        if (module !in mutableList)
            mutableList += module
    }

    fun getModule(name: String) = list.find { it.name.equals(name, true) }

    override var json: JsonObject
        get() = buildJsonObject {
            for (module in list) {
                put(module.name, module.json)
            }
        }
        set(value) {
            for (module in list) {
                module.json = value[module.name]?.jsonObject ?: run {
                    CicadaClient.warn("Module with name \"${module.name}\" wasn't found")
                    continue
                }
            }
        }
}