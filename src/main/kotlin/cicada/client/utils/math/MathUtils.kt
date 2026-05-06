package cicada.client.utils.math

import net.minecraft.client.Minecraft
import net.minecraft.util.Mth
import net.minecraft.world.entity.Entity
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3
import org.joml.Matrix4f
import org.joml.Vector2i
import org.joml.Vector3d
import org.joml.Vector3f
import org.joml.Vector4f
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.E
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.log
import kotlin.math.roundToInt
import kotlin.math.sqrt
import kotlin.random.Random

lateinit var lastProjectionMatrix: Matrix4f
lateinit var lastModelViewMatrix: Matrix4f

fun Float.roundTo(step: Float): Float {
    val bdThis = BigDecimal.valueOf(this.toDouble())
    val bdStep = BigDecimal.valueOf(step.toDouble())
    val divided = bdThis.divide(bdStep, 0, RoundingMode.HALF_UP)
    return divided.multiply(bdStep).toFloat()
}

fun Double.roundTo(step: Double): Double {
    val bdThis = BigDecimal.valueOf(this)
    val bdStep = BigDecimal.valueOf(step)
    val divided = bdThis.divide(bdStep, 0, RoundingMode.HALF_UP)
    return divided.multiply(bdStep).toDouble()
}

/**
 * @return screen coordinates (in pixels)
 */
fun projectWorldToScreen(position: Vector3f, cameraPosition: Vector3f): Vector2i? {
    val client = Minecraft.getInstance()
    val window = client.window

    val projectionMatrix = lastProjectionMatrix
    val modelViewMatrix = lastModelViewMatrix

    // (Proj * ModelView)
    val mulMatrix = Matrix4f(projectionMatrix).mul(modelViewMatrix)

    val localX = position.x - cameraPosition.x
    val localY = position.y - cameraPosition.y
    val localZ = position.z - cameraPosition.z

    val screenPos = Vector4f(localX, localY, localZ, 1.0f)

    mulMatrix.transform(screenPos)

    if (screenPos.w <= 0) return null

    val ndcX = screenPos.x / screenPos.w
    val ndcY = screenPos.y / screenPos.w

    val px = (ndcX + 1) / 2 * window.width
    val py = (1 - ndcY) / 2 * window.height

    return Vector2i(px.roundToInt(), py.roundToInt())
}

fun Entity.boundingBox(partialTicks: Float): AABB {
    val x = (x - xo) * (1 - partialTicks)
    val y = (y - yo) * (1 - partialTicks)
    val z = (z - zo) * (1 - partialTicks)

    return boundingBox.move(-x, -y, -z)
}

fun Vec3.toVector3d() = Vector3d(x, y, z)

fun Int.normalize(start: Int, end: Int) = (this - start) / (end - start)

fun Float.normalize(start: Float, end: Float) = (this - start) / (end - start)

fun Int.map(valueStart: Int, valueEnd: Int, returnStart: Int, returnEnd: Int) =
    this.normalize(valueStart, valueEnd) * (returnEnd - returnStart) + returnStart

fun Float.map(valueStart: Float, valueEnd: Float, returnStart: Float, returnEnd: Float) =
    this.normalize(valueStart, valueEnd) * (returnEnd - returnStart) + returnStart

operator fun Vec3.plus(another: Vec3) = add(another)
operator fun Vec3.minus(another: Vec3) = subtract(another)
operator fun Vec3.unaryMinus() = Vec3(-x, -y, -z)
operator fun Vec3.times(another: Vec3) = multiply(another)
operator fun Vec3.times(times: Double) = multiply(times, times, times)

fun Vec3.coerceIn(box: AABB) = Vec3(
    x.coerceIn(box.minX, box.maxX),
    y.coerceIn(box.minY, box.maxY),
    z.coerceIn(box.minZ, box.maxZ)
)

fun randomDouble(from: Number, to: Number) = Math.random() * (to.toDouble() - from.toDouble()) + from.toDouble()
fun randomDouble(radius: Number) = randomDouble(-radius.toDouble(), radius)

fun randomFloat(from: Number, to: Number) = Math.random().toFloat() * (to.toFloat() - from.toFloat()) + from.toFloat()
fun randomFloat(radius: Number) = randomFloat(-radius.toFloat(), radius.toFloat())

fun ClosedRange<Float>.random() = randomFloat(start, endInclusive)

fun ClosedRange<Float>.gaussianRandom(): Float {
    val stdDev = endInclusive - start
    val mean = (endInclusive + start) / 2
    val u1 = Random.nextDouble(1.0)
    val u2 = Random.nextDouble(1.0)
    val z0 = sqrt(-2.0 * log(u1, E)) * cos(2.0 * PI * u2)
    return (z0 * stdDev + mean).toFloat()
}

fun ClosedRange<Double>.random() = randomDouble(start, endInclusive)

fun ClosedRange<Double>.gaussianRandom(): Double {
    val stdDev = endInclusive - start
    val mean = (endInclusive + start) / 2
    val u1 = Random.nextDouble(1.0)
    val u2 = Random.nextDouble(1.0)
    val z0 = sqrt(-2.0 * log(u1, E)) * cos(2.0 * PI * u2)
    return z0 * stdDev + mean
}

fun gazLarpit(factor: Float, start: Float, end: Float) = start + (end - start) * factor

fun AABB.mul(xz: Double, y: Double): AABB {
    val xzHalf = xz / 2.0
    val yHalf = y / 2.0

    val center = center

    return AABB(
        center.x - xsize * xzHalf,
        center.y - ysize * yHalf,
        center.z - zsize * xzHalf,
        center.x + xsize * xzHalf,
        center.y + ysize * xzHalf,
        center.z + zsize * xzHalf
    )
}

fun Vec3.withLength(newLength: Double): Vec3 {
    val lengthSq = lengthSqr()
    return if (Mth.equal(lengthSq, 0.0)) Vec3.ZERO else scale(newLength / sqrt(lengthSq))
}