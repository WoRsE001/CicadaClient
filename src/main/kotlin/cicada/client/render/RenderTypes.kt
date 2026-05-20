package cicada.client.render

import cicada.client.CicadaClient
import net.minecraft.client.renderer.rendertype.RenderSetup
import net.minecraft.client.renderer.rendertype.RenderType

// SCWGxD regrets everything he did. 17.05.2026 10:13.
val FILLED_QUAD_TYPE: RenderType = RenderType.create(
    "${CicadaClient.NAME}:filledQuad",
    RenderSetup.builder(FILLED_QUAD_PIPELINE).createRenderSetup()
)

val FILLED_DEPTH_QUAD_TYPE: RenderType = RenderType.create(
    "${CicadaClient.NAME}:filledDepthQuad",
    RenderSetup.builder(FILLED_DEPTH_QUAD_PIPELINE).createRenderSetup()
)