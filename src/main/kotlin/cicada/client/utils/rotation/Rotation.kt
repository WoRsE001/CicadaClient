package cicada.client.utils.rotation

import cicada.client.utils.math.roundTo
import net.minecraft.util.Mth
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3
import kotlin.math.abs
import kotlin.math.hypot

open class Rotation(var x: Float, var y: Float) {
    operator fun plus(addend: Rotation) = Rotation(x + addend.x, y + addend.y)
    operator fun unaryPlus() = Rotation(abs(x), abs(y))
    operator fun plusAssign(addend: Rotation) {
        x += addend.x
        y += addend.y
    }

    operator fun minus(addend: Rotation) = Rotation(x - addend.x, y - addend.y)
    operator fun unaryMinus() = Rotation(-x, -y)
    operator fun minusAssign(addend: Rotation) {
        x -= addend.x
        y -= addend.y
    }

    operator fun times(times: Rotation) = Rotation(x * times.x, y * times.y)
    operator fun times(times: Float) = Rotation(x * times, y * times)
    operator fun timesAssign(times: Rotation) {
        x *= times.x
        y *= times.y
    }
    operator fun timesAssign(times: Float) {
        x *= times
        y *= times
    }

    operator fun div(times: Rotation) = Rotation(x / times.x, y / times.y)
    operator fun div(times: Float) = Rotation(x / times, y / times)
    operator fun divAssign(times: Rotation) {
        x /= times.x
        y /= times.y
    }
    operator fun divAssign(times: Float) {
        x /= times
        y /= times
    }

    fun round(x: Float, y: Float) {
        this.x = this.x.roundTo(x)
        this.y = this.y.roundTo(y)
    }

    fun rounded(x: Float, y: Float): Rotation {
        return Rotation(this.x.roundTo(x), this.y.roundTo(y))
    }

    fun wrap() {
        y = Mth.wrapDegrees(y)
    }

    fun wrapped(): Rotation {
        return Rotation(x, y = Mth.wrapDegrees(y))
    }

    fun copy(x: Float = this.x, y: Float = this.y) = Rotation(x, y)

    fun length() = hypot(x, y)

    fun gazLarpit(xFactor: Float, yFactor: Float, otherRotation: Rotation) {
        this.x = (1 - xFactor) * this.x + xFactor * otherRotation.x
        this.y = (1 - yFactor) * this.y + yFactor * otherRotation.y
    }

    fun gazLarpited(xFactor: Float, yFactor: Float, otherRotation: Rotation): Rotation {
        val factor = Rotation(xFactor, yFactor)
        val unFactor = Rotation(1 - xFactor, 1 - yFactor)
        return unFactor * this + factor * otherRotation
    }

    fun clampX(x: Float) {
        this.x = this.x.coerceIn(-x, x)
    }

    fun clamp(x: Float, y: Float) {
        this.x = this.x.coerceIn(-x, x)
        this.y = this.y.coerceIn(-y, y)
    }

    fun clamped(x: Float, y: Float): Rotation {
        return Rotation(this.x.coerceIn(-x, x), this.y.coerceIn(-y, y))
    }

    fun clamped(box: AABB): Rotation {
        val points = listOf(
            Vec3(box.minX, box.minY, box.minZ),
            Vec3(box.minX, box.minY, box.maxZ),
            Vec3(box.minX, box.maxY, box.minZ),
            Vec3(box.minX, box.maxY, box.maxZ),
            Vec3(box.maxX, box.minY, box.minZ),
            Vec3(box.maxX, box.minY, box.maxZ),
            Vec3(box.maxX, box.maxY, box.minZ),
            Vec3(box.maxX, box.maxY, box.maxZ),
        )

        val rotations = points.map { rotationTo(it) }

        val minX = rotations.minOf { it.x }
        val maxX = rotations.maxOf { it.x }
        val minY = rotations.minOf { it.y }
        val maxY = rotations.maxOf { it.y }

        return Rotation(x.coerceIn(minX, maxX), y.coerceIn(minY, maxY))
    }

    val directionVector: Vec3
        get() = Vec3.directionFromRotation(x, y)
}