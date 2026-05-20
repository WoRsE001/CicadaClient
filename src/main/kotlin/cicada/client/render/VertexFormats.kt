package cicada.client.render

import com.mojang.blaze3d.vertex.VertexFormat
import com.mojang.blaze3d.vertex.VertexFormat.builder
import com.mojang.blaze3d.vertex.VertexFormatElement.COLOR
import com.mojang.blaze3d.vertex.VertexFormatElement.POSITION
import com.mojang.blaze3d.vertex.VertexFormatElement.UV0

// SCWGxD regrets everything he did. 17.05.2026 10:18.
val RECT_FORMAT: VertexFormat = builder()
    .add("Position", POSITION)
    .add("Color", COLOR)
    .add("Rounding", ROUNDING_ELEMENT)
    .add("Dimensions", DIMENSIONS_ELEMENT)
    .build()

val IMAGE_FORMAT: VertexFormat = builder()
    .add("Position", POSITION)
    .add("UV0", UV0)
    .add("Color", COLOR)
    .add("Rounding", ROUNDING_ELEMENT)
    .add("Dimensions", DIMENSIONS_ELEMENT)
    .build()

val SHADER_RECT_FORMAT: VertexFormat = builder()
    .add("Position", POSITION)
    .add("Color", COLOR)
    .add("Time", TIME_ELEMENT)
    .build()