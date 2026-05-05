package cicada.client.utils.render

import com.mojang.blaze3d.vertex.VertexFormatElement
import com.mojang.blaze3d.vertex.VertexFormatElement.Type
import com.mojang.blaze3d.vertex.VertexFormatElement.register

private const val LAST_VANILLA_ELEMENT_ID = 6
private var nextId = LAST_VANILLA_ELEMENT_ID

private fun register(index: Int, type: Type, normalized: Boolean, count: Int): VertexFormatElement {
    nextId++
    return register(nextId, index, type, normalized, count)
}

val ROUNDING_ELEMENT = register(0, Type.FLOAT, false, 4)
val DIMENSIONS_ELEMENT = register(0, Type.FLOAT, false, 4)
val TIME_ELEMENT = register(0, Type.FLOAT, false, 1)