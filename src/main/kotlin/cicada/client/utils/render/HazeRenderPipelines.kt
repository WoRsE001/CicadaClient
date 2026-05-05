package cicada.client.utils.render

import com.mojang.blaze3d.pipeline.BlendFunction
import com.mojang.blaze3d.pipeline.ColorTargetState
import com.mojang.blaze3d.pipeline.DepthStencilState
import com.mojang.blaze3d.pipeline.RenderPipeline
import com.mojang.blaze3d.pipeline.RenderPipeline.builder
import com.mojang.blaze3d.platform.CompareOp
import com.mojang.blaze3d.vertex.DefaultVertexFormat
import com.mojang.blaze3d.vertex.VertexFormat
import cicada.client.CicadaClient
import net.minecraft.client.renderer.RenderPipelines.MATRICES_PROJECTION_SNIPPET
import net.minecraft.client.renderer.RenderPipelines.register

val ROUNDED_RECT_PIPELINE: RenderPipeline = register(builder(MATRICES_PROJECTION_SNIPPET)
    .withLocation(CicadaClient.of("pipeline/rounded_rect"))
    .withVertexShader(CicadaClient.of("core/rounded_rect"))
    .withFragmentShader(CicadaClient.of("core/rounded_rect"))
    .withColorTargetState(ColorTargetState(BlendFunction.TRANSLUCENT))
    .withVertexFormat(ROUNDED_RECT_FORMAT, VertexFormat.Mode.QUADS)
    .build()
)

val SHADER_RECT_PIPELINE: RenderPipeline = register(builder(MATRICES_PROJECTION_SNIPPET)
    .withLocation(CicadaClient.of("pipeline/shader_rect"))
    .withVertexShader(CicadaClient.of("core/warping"))
    .withFragmentShader(CicadaClient.of("core/warping"))
    .withColorTargetState(ColorTargetState(BlendFunction.TRANSLUCENT))
    .withVertexFormat(SHADER_RECT_FORMAT, VertexFormat.Mode.QUADS)
    .build()
)

val ROUNDED_IMAGE_PIPELINE: RenderPipeline = register(builder(MATRICES_PROJECTION_SNIPPET)
    .withLocation(CicadaClient.of("pipeline/textured_rect"))
    .withVertexShader(CicadaClient.of("core/rounded_image"))
    .withFragmentShader(CicadaClient.of("core/rounded_image"))
    .withSampler("Sampler0")
    .withColorTargetState(ColorTargetState(BlendFunction.TRANSLUCENT))
    .withVertexFormat(ROUNDED_IMAGE_FORMAT, VertexFormat.Mode.QUADS)
    .build()
)

val FILLED_DEPTH_QUAD_PIPELINE: RenderPipeline = register(builder(MATRICES_PROJECTION_SNIPPET)
    .withLocation(CicadaClient.of("pipeline/filled_quad"))
    .withVertexShader("core/position_color")
    .withFragmentShader("core/position_color")
    .withVertexFormat(DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS)
    .build()
)

val FILLED_QUAD_PIPELINE: RenderPipeline = register(builder(MATRICES_PROJECTION_SNIPPET)
    .withLocation(CicadaClient.of("pipeline/filled_quad"))
    .withVertexShader("core/position_color")
    .withFragmentShader("core/position_color")
    .withVertexFormat(DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS)
    .withDepthStencilState(DepthStencilState(CompareOp.ALWAYS_PASS, false))
    .build()
)

val FILLED_TRIANGLES_PIPELINE: RenderPipeline = register(builder(MATRICES_PROJECTION_SNIPPET)
    .withLocation(CicadaClient.of("pipeline/filled_triangles"))
    .withVertexShader("core/position_color")
    .withFragmentShader("core/position_color")
    .withColorTargetState(ColorTargetState(BlendFunction.TRANSLUCENT))
    .withVertexFormat(DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.TRIANGLES)
    .build()
)

val MSDF_PIPELINE: RenderPipeline = register(builder(MATRICES_PROJECTION_SNIPPET)
    .withLocation(CicadaClient.of("pipeline/msdf"))
    .withVertexShader("core/position_tex_color")
    .withFragmentShader(CicadaClient.of("core/msdf"))
    .withSampler("Sampler0")
    .withColorTargetState(ColorTargetState(BlendFunction.TRANSLUCENT))
    .withVertexFormat(DefaultVertexFormat.POSITION_TEX_COLOR, VertexFormat.Mode.QUADS)
    .build()
)