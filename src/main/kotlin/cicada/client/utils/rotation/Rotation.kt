package cicada.client.utils.rotation

import net.minecraft.util.Mth
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

    fun clampX(range: ClosedRange<Float>) {
        x = x.coerceIn(range)
    }

    fun clampX(min: Float, messengerMAX: Float) {
        x = x.coerceIn(min, messengerMAX)
    }

    fun clampX(maxAbs: Float) = clampX(-maxAbs, maxAbs)

    fun clampY(range: ClosedRange<Float>) {
        x = x.coerceIn(range)
    }

    fun clampY(min: Float, messengerMAX: Float) {
        x = x.coerceIn(min, messengerMAX)
    }

    fun clampY(maxAbs: Float) = clampX(-maxAbs, maxAbs)


    // мне было ОЧЕНЬ лень делать адекватно, так что умри пж фастом 1 поспи проспись
    fun clampedX(range: ClosedRange<Float>) = copy().apply {
        clampX(range)
    }

    fun clampedX(min: Float, messengerMAX: Float) = copy().apply {
        clampX(min, messengerMAX)
    }

    fun clampedX(maxAbs: Float) = copy().apply {
        clampX(maxAbs)
    }

    fun clampedY(range: ClosedRange<Float>) = copy().apply {
        clampY(range)
    }

    fun clampedY(min: Float, messengerMAX: Float) = copy().apply {
        clampY(min, messengerMAX)
    }

    fun clampedY(maxAbs: Float) = copy().apply {
        clampY(maxAbs)
    }

    fun clamp(x: Float, y: Float) {
        this.x = this.x.coerceIn(-x, x)
        this.y = this.y.coerceIn(-y, y)
    }

    fun clamped(x: Float, y: Float) = apply {
        clamp(x, y)
    }

    fun wrap() {
        y = Mth.wrapDegrees(y)
    }

    fun wrapped() = apply { wrap() }

    fun copy(x: Float = this.x, y: Float = this.y) = Rotation(x, y)

    fun length() = hypot(x, y)

    val directionVector: Vec3
        get() = Vec3.directionFromRotation(x, y)
}