package cicada.client.module.impl.combat

import com.mojang.blaze3d.platform.InputConstants
import cicada.client.event.Event
import cicada.client.event.impl.AttackEvent
import cicada.client.event.impl.KeyEvent
import cicada.client.event.impl.LegitClickTimingEvent
import cicada.client.event.impl.TickEvent
import cicada.client.mixin.accessors.AccessorMinecraft
import cicada.client.module.Category
import cicada.client.module.Module
import cicada.client.rotation.RotationListener
import cicada.client.utils.level
import cicada.client.utils.mc
import cicada.client.utils.player
import cicada.client.utils.rotation.rotate
import cicada.client.utils.rotation.rotation
import cicada.client.utils.rotation.rotationTo
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Items
import net.minecraft.world.phys.Vec3

object ModuleMaceHelper : Module(
    "MaceHelper",
    Category.COMBAT
), RotationListener {
    override val rotatePriority = 1

    private val autoSwitch = toggleableGroup("Auto switch", false)
    private val minFallDistance by float("Min fall distance", 6f, 3f..30f)
    private val switchBackDelay by autoSwitch.int("Switch back delay", 10, 0..10)


    private val attackAura = toggleableGroup("Attack aura", false)

    // TODO: сделать систему фейковых слотов
    private var lastSlot = -1

    private var needSwitchBack = false
    private var switchBackDelayLeft = 0

    private var target: Player? = null

    private var прожим = false

    private var хочу_закончить_прожим = false

    override fun onEvent(event: Event) {
        registerToRotations()
        autoSwitch.toggled = true
        attackAura.toggled = true

        if (autoSwitch.toggled)
            handleAutoSwitch(event)

        if (attackAura.toggled)
            handleAttackAura(event)
    }

    override fun rotate() {
        if (!прожим)
            return

        val eye = player.eyePosition
        val aabb = target!!.boundingBox
        val bestHitVec = Vec3(
            eye.x.coerceIn(aabb.minX..aabb.maxX),
            eye.y.coerceIn(aabb.minY..aabb.maxY),
            eye.z.coerceIn(aabb.minZ..aabb.maxZ)
        )

        val bestRotation = rotationTo(bestHitVec)
        val delta = (bestRotation - player.rotation()).wrapped()

        player.rotate(delta)
        прожим = false
        хочу_закончить_прожим = true
    }

    override fun willRotate() = target != null && прожим

    private fun handleAttackAura(event: Event) {
        if (event is TickEvent.Pre)
            updateTarget()

        if (event is KeyEvent) {
            if (event.input.input() == InputConstants.KEY_R && event.action == 0)
                прожим = true
        }

        if (event is LegitClickTimingEvent && хочу_закончить_прожим) {
            (mc as AccessorMinecraft).invokeStartAttack()
            хочу_закончить_прожим = false
        }
    }

    private fun updateTarget() {
        target = null

        var bestDistance = Float.MAX_VALUE

        for (entity in level.entitiesForRendering()) {
            if (entity == player || entity !is Player)
                continue

            val distance = player.distanceTo(entity)

            if (distance > bestDistance)
                continue

            target = entity
            bestDistance = distance
        }
    }

    private fun handleAutoSwitch(event: Event) {
        val inventory = player.inventory

        if (event is LegitClickTimingEvent && needSwitchBack) {
            if (switchBackDelayLeft > 0) {
                switchBackDelayLeft--
                return
            }

            needSwitchBack = false

            if (lastSlot != -1)
                inventory.selectedSlot = lastSlot
        }

        if (event is AttackEvent.Pre) {
            val maceSlot = findMaceSlot() ?: return

            if (inventory.selectedSlot == maceSlot)
                return

            lastSlot = inventory.selectedSlot
            inventory.selectedSlot = maceSlot
            needSwitchBack = true
            switchBackDelayLeft = switchBackDelay
        }
    }

    private fun findMaceSlot(): Int? {
        val inventory = player.inventory

        for (i in 0..8) {
            val stack = inventory.getItem(i)

            if (stack.item == Items.MACE) {
                return i
            }
        }

        return null
    }

//    private fun maceSlot(): Int? {
//        val player = MinecraftClient.getInstance().player ?: return null
//        val inventory = player.inventory
//
//        // Проходим по слотам хотбара (индексы 0-8)
//        for (i in 0..8) {
//            val stack = inventory.getStack(i)
//            // Проверяем, является ли предмет булавой (mace)
//            if (stack.item == net.minecraft.item.Items.MACE) {
//                return i
//            }
//        }
//
//        return null
//    }
}