package cicada.client.utils.render

import com.mojang.blaze3d.vertex.BufferBuilder
import com.mojang.blaze3d.vertex.VertexConsumer
import com.mojang.blaze3d.vertex.VertexFormatElement
import cicada.client.mixin.accessors.AccessorBufferBuilder
import org.lwjgl.system.MemoryUtil

private fun VertexConsumer.beginElement(element: VertexFormatElement): Long {
    if (this !is BufferBuilder)
        throw IllegalStateException("Not a BufferBuilder!")

    return (this as AccessorBufferBuilder).begin(element)
}

fun VertexConsumer.setRounding(r1: Float, r2: Float, r3: Float, r4: Float) = apply {
    val ptr = beginElement(ROUNDING_ELEMENT)

    if (ptr != -1L) {
        MemoryUtil.memPutFloat(ptr, r1)
        MemoryUtil.memPutFloat(ptr + 4, r2)
        MemoryUtil.memPutFloat(ptr + 8, r3)
        MemoryUtil.memPutFloat(ptr + 12, r4)
    }
}

fun VertexConsumer.setRounding(radius: Float) = setRounding(radius, radius, radius, radius)

fun VertexConsumer.setDimensions(x: Float, y: Float, w: Float, h: Float) = apply {
    val ptr = beginElement(DIMENSIONS_ELEMENT)

    if (ptr != -1L) {
        MemoryUtil.memPutFloat(ptr, x)
        MemoryUtil.memPutFloat(ptr + 4, y)
        MemoryUtil.memPutFloat(ptr + 8, w)
        MemoryUtil.memPutFloat(ptr + 12, h)
    }
}

fun VertexConsumer.setTime(t: Float) = apply {
    val ptr = beginElement(TIME_ELEMENT)

    if (ptr != -1L) {
        MemoryUtil.memPutFloat(ptr, t)
    }
}