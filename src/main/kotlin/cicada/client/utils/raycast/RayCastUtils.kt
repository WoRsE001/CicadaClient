package cicada.client.utils.raycast

import cicada.client.mixin.accessors.AccessorLocalPlayer
import net.minecraft.client.Minecraft
import net.minecraft.client.player.LocalPlayer
import net.minecraft.core.component.DataComponents
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntitySelector
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.EntityHitResult
import net.minecraft.world.phys.HitResult

// SCWGxD regrets everything he did. 17.06.2026 17:05.
fun Minecraft.customStartAttack(entityInteractRange: Double): Boolean {
    val player = player
    if (level == null || player == null)
        return false

    if (missTime > 0 || player.isHandsBusy)
        return false

    val heldItem = player.getItemInHand(InteractionHand.MAIN_HAND)

    if (!heldItem.isItemEnabled(level!!.enabledFeatures()) || player.cannotAttackWithItem(heldItem, 0))
        return false

    val piercingWeapon = heldItem.get(DataComponents.PIERCING_WEAPON)

    if (piercingWeapon != null) {
        gameMode!!.piercingAttack(piercingWeapon)
        player.swing(InteractionHand.MAIN_HAND)
        return true
    }

    val hitResult = player.rayCastHitResult(cameraEntity!!, 1f, entityInteractRange)
    var endAttack = false

    if (hitResult == null) {
        if (gameMode!!.hasMissTime()) {
            missTime = 10
        }

        return false
    }

    if (gameMode!!.isSpectator) {
        if (hitResult is EntityHitResult) {
            gameMode!!.spectate(hitResult.entity)
        }

        return true
    }

    when (hitResult.type) {
        HitResult.Type.ENTITY -> {
            val customItemRange = heldItem.get(DataComponents.ATTACK_RANGE)

            if (customItemRange == null || customItemRange.isInRange(player, hitResult.getLocation())) {
                gameMode!!.attack(player, (hitResult as EntityHitResult).entity)
            }
        }

        HitResult.Type.BLOCK -> {
            val blockHit = hitResult as BlockHitResult
            val pos = blockHit.blockPos
            if (!level!!.getBlockState(pos).isAir) {
                gameMode!!.startDestroyBlock(pos, blockHit.direction)
                if (level!!.getBlockState(pos).isAir) {
                    endAttack = true
                }
            }
        }

        HitResult.Type.MISS -> {
            if (gameMode!!.hasMissTime()) {
                missTime = 10
            }

            player.resetAttackStrengthTicker()
        }
    }

    player.swing(InteractionHand.MAIN_HAND)

    return endAttack
}

fun LocalPlayer.rayCastHitResult(
    cameraEntity: Entity,
    a: Float,
    entityInteractRange: Double = entityInteractionRange()
): HitResult? {
    val itemStack = activeItem
    val itemAttackRange = itemStack.get(DataComponents.ATTACK_RANGE)
    val blockInteractionRange = blockInteractionRange()
    var hitResult: HitResult? = null

    if (itemAttackRange != null) {
        hitResult = itemAttackRange.getClosesetHit(cameraEntity, a, EntitySelector.CAN_BE_PICKED)

        if (hitResult is BlockHitResult) {
            hitResult = (this as AccessorLocalPlayer).invokeFilterHitResult(
                hitResult,
                cameraEntity.getEyePosition(a),
                blockInteractionRange
            )
        }
    }

    if (hitResult == null || hitResult.type == HitResult.Type.MISS) {
        hitResult = (this as AccessorLocalPlayer).invokePick(
            cameraEntity,
            blockInteractionRange,
            entityInteractRange,
            a
        )
    }

    return hitResult
}
