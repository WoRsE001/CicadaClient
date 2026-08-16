package cicada.client.render.engine

import com.mojang.blaze3d.vertex.BufferBuilder
import com.mojang.blaze3d.vertex.VertexConsumer
import com.mojang.blaze3d.vertex.VertexFormatElement
import org.lwjgl.system.MemoryUtil

// SCWGxD regrets everything he did. 17.05.2026 10:14.
private fun VertexConsumer.begin(element: VertexFormatElement): Long {
    if (this !is BufferBuilder)
        throw IllegalStateException("Not a BufferBuilder!")

    return this.beginElement(element)
}

fun VertexConsumer.setRounding(r1: Float, r2: Float, r3: Float, r4: Float) = apply {
    val ptr = begin(ROUNDING_ELEMENT)

    if (ptr != -1L) {
        MemoryUtil.memPutFloat(ptr, r1)
        MemoryUtil.memPutFloat(ptr + 4, r2)
        MemoryUtil.memPutFloat(ptr + 8, r3)
        MemoryUtil.memPutFloat(ptr + 12, r4)
    }
}

fun VertexConsumer.setRounding(radius: Float) = setRounding(radius, radius, radius, radius)

fun VertexConsumer.setDimensions(x: Float, y: Float, w: Float, h: Float) = apply {
    val ptr = begin(DIMENSIONS_ELEMENT)

    if (ptr != -1L) {
        MemoryUtil.memPutFloat(ptr, x)
        MemoryUtil.memPutFloat(ptr + 4, y)
        MemoryUtil.memPutFloat(ptr + 8, w)
        MemoryUtil.memPutFloat(ptr + 12, h)
    }
}

fun VertexConsumer.setTime(t: Float) = apply {
    val ptr = begin(TIME_ELEMENT)

    if (ptr != -1L) {
        MemoryUtil.memPutFloat(ptr, t)
    }
}