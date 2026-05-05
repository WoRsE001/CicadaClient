package cicada.client.utils.math

import org.joml.Vector2f

// SCWGxD regrets everything he did. 04.04.2026 17:29.
class Rect(var x: Float, var y: Float, var w: Float, var h: Float) {
    fun isCollide(pointX: Float, pointY: Float) = pointX in x .. x + w && pointY in y .. y + h

    fun isCollide(point: Vector2f) = isCollide(point.x, point.y)
}