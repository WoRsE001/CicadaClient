package cicada.client.utils.raycast

import cicada.client.rotation.Rotation
import cicada.client.utils.client.mc
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntitySelector
import net.minecraft.world.entity.projectile.ProjectileUtil
import net.minecraft.world.level.ClipContext
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.EntityHitResult
import java.util.function.Predicate

fun Entity.findEntityInCrosshair(
    range: Double,
    rotation: Rotation,
    predicate: Predicate<Entity>? = null,
): EntityHitResult? {
    val cameraVec = eyePosition
    val rotationVec = rotation.directionVector

    val vec3d3 = cameraVec.add(rotationVec.x * range, rotationVec.y * range, rotationVec.z * range)
    val box = boundingBox.expandTowards(rotationVec.scale(range)).inflate(1.0, 1.0, 1.0)

    val hitResult = ProjectileUtil.getEntityHitResult(
        this,
        cameraVec,
        vec3d3,
        box,
        if (predicate != null) EntitySelector.CAN_BE_PICKED.or(predicate) else EntitySelector.CAN_BE_PICKED,
        range * range
    )

    return hitResult
}

fun findEntityInCrosshair(
    range: Double,
    rotation: Rotation,
    predicate: Predicate<Entity>? = null,
): EntityHitResult? = mc.cameraEntity?.findEntityInCrosshair(range, rotation, predicate)

fun Entity.rayCast(
    rotation: Rotation,
    range: Float
): BlockHitResult {
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