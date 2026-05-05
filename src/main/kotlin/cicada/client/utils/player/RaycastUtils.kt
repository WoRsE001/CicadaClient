package cicada.client.utils.player

import cicada.client.mixin.accessors.AccessorLocalPlayer
import cicada.client.utils.gameMode
import cicada.client.utils.level
import cicada.client.utils.math.withLength
import cicada.client.utils.mc
import cicada.client.utils.player
import cicada.client.utils.rotation.Rotation
import net.minecraft.client.player.LocalPlayer
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.component.DataComponents
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntitySelector
import net.minecraft.world.entity.projectile.ProjectileUtil
import net.minecraft.world.level.ClipContext
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.EntityHitResult
import net.minecraft.world.phys.HitResult
import net.minecraft.world.phys.Vec3
import net.minecraft.world.phys.shapes.CollisionContext
import kotlin.compareTo
import kotlin.math.pow

// SCWGxD regrets everything he did. 09.04.2026 8:19.
fun Entity.rayCast(
    rotation: Rotation,
    range: Float
): BlockHitResult? {
    val from = this.eyePosition
    val viewVector = rotation.directionVector
    val to = from.add(viewVector.x * range, viewVector.y * range, viewVector.z * range);
    return this.level().clip(
        ClipContext(
            from, to,
            ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE,
            this
        )
    )
}

fun LocalPlayer.rayCast(
    cameraEntity: Entity,
    blockInteractionRange: Double,
    entityInteractionRange: Double,
    a: Float
): HitResult {
    return (this as AccessorLocalPlayer).invokePick(cameraEntity, blockInteractionRange, entityInteractionRange, a)
}

fun pickEntity(cameraEntity: Entity, blockInteractionRange: Double, entityInteractionRange: Double, partialTicks: Float): EntityHitResult? {
    val maxDistance = blockInteractionRange.coerceAtLeast(entityInteractionRange)
    val maxDistanceSq = maxDistance.pow(2)
    val from = cameraEntity.getEyePosition(partialTicks)
    val direction = cameraEntity.getViewVector(partialTicks)
    val to = from.add(direction.x * maxDistance, direction.y * maxDistance, direction.z * maxDistance)
    val box = cameraEntity.boundingBox.expandTowards(direction.scale(maxDistance)).inflate(1.0, 1.0, 1.0)
    return ProjectileUtil.getEntityHitResult(cameraEntity, from, to, box, EntitySelector.CAN_BE_PICKED, maxDistanceSq)
}

fun startAttack(entityInteractionRange: Double, entityInteractThroughBlocks: Boolean): Boolean {
    if (mc.missTime > 0 || player.isHandsBusy || mc.cameraEntity == null) {
        return false
    }

    val heldItem = player.getItemInHand(InteractionHand.MAIN_HAND)
    if (!heldItem.isItemEnabled(level.enabledFeatures()) || player.cannotAttackWithItem(heldItem, 0)) {
        return false
    }

    val hitResult = (
        if (entityInteractThroughBlocks)
            pickEntity(
                mc.cameraEntity!!,
                player.blockInteractionRange(),
                entityInteractionRange,
                mc.deltaTracker.getGameTimeDeltaPartialTick(false)
            ) ?: player.rayCast(
                mc.cameraEntity!!,
                player.blockInteractionRange(),
                entityInteractionRange,
                mc.deltaTracker.getGameTimeDeltaPartialTick(false)
            )
        else
            player.rayCast(
                mc.cameraEntity!!,
                player.blockInteractionRange(),
                entityInteractionRange,
                mc.deltaTracker.getGameTimeDeltaPartialTick(false)
            )
    )

    if (gameMode.isSpectator && hitResult is EntityHitResult)
        gameMode.spectate(hitResult.entity)

    var endAttack = false

    val piercingWeapon = heldItem.get(DataComponents.PIERCING_WEAPON)
    if (piercingWeapon != null) {
        gameMode.piercingAttack(piercingWeapon)
        player.swing(InteractionHand.MAIN_HAND)
        return true
    }

    when (hitResult.type) {
        HitResult.Type.ENTITY -> {
            val customItemRange = heldItem.get(DataComponents.ATTACK_RANGE)
            if (customItemRange == null || customItemRange.isInRange(player, hitResult.getLocation()))
                gameMode.attack(player, (hitResult as EntityHitResult).entity)
        }

        HitResult.Type.BLOCK -> {
            val blockHit = hitResult as BlockHitResult
            val pos = blockHit.blockPos
            if (!level.getBlockState(pos).isAir) {
                gameMode.startDestroyBlock(pos, blockHit.direction)
                if (level.getBlockState(pos).isAir)
                    endAttack = true
            }
        }

        HitResult.Type.MISS -> {
            if (gameMode.hasMissTime()) {
                mc.missTime = 10
            }
            player.resetAttackStrengthTicker()
        }
    }

    player.swing(InteractionHand.MAIN_HAND)

    return endAttack
}