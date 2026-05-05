package cicada.client.utils.player

import cicada.client.utils.player
import cicada.client.utils.player.isMoving
import net.minecraft.client.player.ClientInput
import net.minecraft.client.player.LocalPlayer
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Input
import net.minecraft.world.phys.Vec3
import org.joml.Math.toRadians
import kotlin.math.cos
import kotlin.math.sin

// SCWGxD regrets everything he did. 21.04.2026 9:53.
fun ClientInput.isMoving() = moveVector.length() > 0

inline val Entity.horizontalSpeed: Double
    get() = deltaMovement.horizontalDistance()

fun Entity.hasHorizontalSpeed() = horizontalSpeed > 0

fun Vec3.withStrafe(
    speed: Double = horizontalDistance(),
    strength: Double = 1.0,
    input: ClientInput = player.input,
    yaw: Float = player.yRot,
): Vec3 {
    if (!input.isMoving()) {
        return Vec3(0.0, y, 0.0)
    }

    val oneMinusStrength = 1.0 - strength
    val prevX = x * oneMinusStrength
    val prevZ = z * oneMinusStrength
    val usedSpeed = speed * strength

    val angle = Math.toRadians(yaw.toDouble())
    val newX = prevX - sin(angle) * usedSpeed
    val newZ = prevZ + cos(angle) * usedSpeed

    return Vec3(newX, y, newZ)
}

