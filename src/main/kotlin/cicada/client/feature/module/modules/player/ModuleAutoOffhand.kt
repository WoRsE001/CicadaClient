package cicada.client.feature.module.modules.player

import cicada.client.event.Event
import cicada.client.event.events.EventTick
import cicada.client.feature.module.ClientModule
import cicada.client.feature.module.ModuleCategory
import cicada.client.feature.module.modules.combat.attackaura.ModuleAttackAura
import cicada.client.setting.value.ToggleableConfigurable
import cicada.client.utils.client.gameMode
import cicada.client.utils.client.player
import cicada.client.utils.player.inventory.slotBy
import cicada.utility.player.inventory.isTotemOfUndying
import net.minecraft.world.inventory.ContainerInput
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.Items

// SCWGxD regrets everything he did. 18.06.2026 4:44.
object ModuleAutoOffhand : ClientModule("AutoOffhand", ModuleCategory.PLAYER) {
    private val swappers = mutableListOf<Swapper>()

    private object Totem : Swapper("Totem", true) {
        val healthThreshold by int("HealthThreshold", 6, 0..20)

        override fun doSwap() = player.health <= healthThreshold

        override fun slotToSwap(): Slot? = player.inventoryMenu.slotBy { it.isTotemOfUndying }
    }

    private object GoldenApple : Swapper("GoldenApple", true) {
        val ifToggledAttackAura by boolean("IfToggledAttackAura", true)
        val ifAttackAuraHasTarget by boolean("IfAttackAuraHasTarget", true).visible { ifToggledAttackAura }

        override fun doSwap() = ifToggledAttackAura && (ModuleAttackAura.target != null || !ifAttackAuraHasTarget)

        override fun slotToSwap(): Slot? = player.inventoryMenu.slotBy { it.`is`(Items.GOLDEN_APPLE) }
    }

    init {
        tree(Totem)
        tree(GoldenApple)
    }

    override fun onEvent(event: Event) {
        if (event is EventTick.Pre) {
            for (swapper in swappers) {
                if (swapper.toggled && swapper.doSwap()) {
                    swapper.slotToSwap()?.let { slot ->
                        gameMode.handleContainerInput(
                            player.inventoryMenu.containerId,
                            slot.index,
                            40,
                            ContainerInput.SWAP,
                            player
                        )

                        return
                    }
                }
            }
        }
    }

    private abstract class Swapper(name: String, defaultToggled: Boolean) : ToggleableConfigurable(name, defaultToggled) {
        init {
            swappers += this
        }

        abstract fun doSwap(): Boolean
        abstract fun slotToSwap(): Slot?
    }
}