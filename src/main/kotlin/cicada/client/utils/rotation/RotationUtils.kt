package cicada.client.utils.rotation

import cicada.client.utils.math.minus
import cicada.client.utils.mc
import cicada.client.utils.player
import net.minecraft.client.player.LocalPlayer
import net.minecraft.core.BlockPos
import net.minecraft.util.Mth
import net.minecraft.world.phys.Vec3
import kotlin.math.atan2

fun LocalPlayer.rotation() = Rotation(xRot, yRot)
fun LocalPlayer.rotate(delta: Rotation, clampX: Boolean = true) {
    xRot += delta.x
    yRot += delta.y

    if (clampX) xRot = xRot.coerceIn(-90f, 90f)
}

fun pitchFromDiff(diff: Vec3) = -Math.toDegrees(atan2(diff.y, diff.horizontalDistance())).toFloat()
fun pitchTo(point: Vec3) = pitchFromDiff(point - player.eyePosition)
fun pitchTo(block: BlockPos) =
    pitchFromDiff(Vec3(block.x + 0.5, block.y + 0.5, block.z + 0.5) - player.eyePosition)

fun yawFromDiff(diff: Vec3) = Mth.wrapDegrees(Math.toDegrees(atan2(diff.z, diff.x)).toFloat() - 90f)
fun yawTo(point: Vec3) = yawFromDiff(point - player.eyePosition)
fun yawTo(block: BlockPos) = yawFromDiff(Vec3(block.x + 0.5, block.y + 0.5, block.z + 0.5) - player.eyePosition)

fun rotationFromDiff(diff: Vec3) = Rotation(pitchFromDiff(diff), yawFromDiff(diff))
fun rotationTo(point: Vec3) = Rotation(pitchTo(point), yawTo(point))

fun gcd(): Float {
    val f = mc.options.sensitivity().get().toFloat() * 0.6f + 0.2f
    return f * f * f * 8.0f * 0.15f
}