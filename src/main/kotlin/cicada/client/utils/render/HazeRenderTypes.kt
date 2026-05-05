package cicada.client.utils.render

import cicada.client.CicadaClient
import net.minecraft.client.renderer.rendertype.RenderSetup
import net.minecraft.client.renderer.rendertype.RenderType

val FILLED_QUAD: RenderType = RenderType.create(
    "${CicadaClient.MOD_ID}:filledQuad",
    RenderSetup.builder(FILLED_QUAD_PIPELINE).createRenderSetup()
)

val FILLED_DEPTH_QUAD: RenderType = RenderType.create(
    "${CicadaClient.MOD_ID}:filledDepthQuad",
    RenderSetup.builder(FILLED_DEPTH_QUAD_PIPELINE).createRenderSetup()
)
