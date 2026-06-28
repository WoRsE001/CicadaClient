package cicada.client.feature.module.modules.misc

import cicada.client.event.Event
import cicada.client.event.impl.TickEvent
import cicada.client.event.impl.WorldChangeEvent
import cicada.client.feature.module.ModuleCategory
import cicada.client.feature.module.ClientModule
import cicada.client.utils.client.level
import cicada.client.utils.client.player
import net.minecraft.core.component.DataComponents
import net.minecraft.util.ARGB.opaque
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player

object ModuleTeams : ClientModule("Teams", ModuleCategory.MISC) {
    private val checks = multiChoice("Checks")
    private val armorCheck = checks.choice("Armor", true)
    private val nameCheck = checks.choice("Name")

    private val _teams = mutableListOf<Player>()
    val teams: List<Player>
        get() = _teams.toList()

    override fun onDisable() {
        _teams.clear()
    }

    override fun onEvent(event: Event) {
        if (event is TickEvent.Pre) {
            for (entity in level.entitiesForRendering()) {
                if (entity !is Player || entity in _teams) continue

                if (armorCheck.toggled) {
                    if (
                        compareArmorColor(player, entity, EquipmentSlot.HEAD) ||
                        compareArmorColor(player, entity, EquipmentSlot.BODY) ||
                        compareArmorColor(player, entity, EquipmentSlot.LEGS) ||
                        compareArmorColor(player, entity, EquipmentSlot.CHEST)
                    ) _teams += entity
                }

                if (nameCheck.toggled) {
                    val playerColorName = player.displayName?.style?.color
                    val entityColorName = entity.displayName?.style?.color

                    if (playerColorName != null && entityColorName != null && playerColorName.value == entityColorName.value) {
                        _teams += entity
                    }
                }
            }
        }

        if (event is WorldChangeEvent) {
            _teams.clear()
        }
    }

    private fun getArmorColor(entity: LivingEntity, slot: EquipmentSlot): Int? {
        val itemStack = entity.getItemBySlot(slot)
        return itemStack[DataComponents.DYED_COLOR]?.rgb?.let { opaque(it) }
    }

    private fun compareArmorColor(entity: LivingEntity, entity1: LivingEntity, slot: EquipmentSlot): Boolean {
        val entityArmorColor = getArmorColor(entity, slot)
        val entity1ArmorColor = getArmorColor(entity1, slot)
        return entityArmorColor != null && entity1ArmorColor != null && entityArmorColor == entity1ArmorColor
    }
}