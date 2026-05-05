package cicada.client.utils.render

import com.mojang.blaze3d.vertex.VertexFormat
import com.mojang.blaze3d.vertex.VertexFormat.builder
import com.mojang.blaze3d.vertex.VertexFormatElement.*

val ROUNDED_RECT_FORMAT: VertexFormat = builder()
    .add("Position", POSITION)
    .add("Color", COLOR)
    .add("Rounding", ROUNDING_ELEMENT)
    .add("Dimensions", DIMENSIONS_ELEMENT)
    .build()

val ROUNDED_IMAGE_FORMAT: VertexFormat = builder()
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