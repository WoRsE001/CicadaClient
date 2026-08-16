package cicada.client.feature.module.modules.visual

import cicada.client.event.Event
import cicada.client.event.events.EventRender
import cicada.client.event.events.EventTick
import cicada.client.feature.module.ClientModule
import cicada.client.feature.module.ModuleCategory
import cicada.client.render.Renderer3D
import cicada.client.render.engine.FILLED_QUAD_TYPE
import cicada.client.setting.preset.NoiseSetting
import cicada.client.setting.preset.TargetFinder
import cicada.client.utils.math.Color4f
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3

// SCWGxD regrets everything he did. 19.07.2026 2:04.
object ModuleNoisePointSelect : ClientModule("NoisePointSelect", ModuleCategory.VISUAL) {
    private val targetFinder = tree(TargetFinder())
    private val pointSize by float("PointSize", 0.1f, 0f..1f)

    private val amplitudeX by float("AmplitudeX", 1f, 0f..5f)
    private val amplitudeY by float("AmplitudeY", 1f, 0f..5f)
    private val amplitudeZ by float("AmplitudeZ", 1f, 0f..5f)

    private val xNoiseGenerator = tree(NoiseSetting("XNoiseGenerator"))
    private val yNoiseGenerator = tree(NoiseSetting("YNoiseGenerator"))
    private val zNoiseGenerator = tree(NoiseSetting("ZNoiseGenerator"))

    override fun onEnable() {
        xNoiseGenerator.applySettings()
        yNoiseGenerator.applySettings()
        zNoiseGenerator.applySettings()
    }

    override fun onEvent(event: Event) {
        if (event is EventTick.Pre) {
            targetFinder.updateTarget()
        }

        val target = targetFinder.target
        if (event is EventRender.World && target != null) {
            val time = (System.currentTimeMillis() % Int.MAX_VALUE).toFloat() / 1_000f

            val noiseX = xNoiseGenerator.genNosie(time, 0f  , 0f  )
            val noiseY = yNoiseGenerator.genNosie(0f  , time, 0f  )
            val noiseZ = zNoiseGenerator.genNosie(0f  , 0f  , time)

            val boundingBox = target.boundingBox
            val wHalf = (boundingBox.maxX - boundingBox.minX) / 2
            val hHalf = (boundingBox.maxY - boundingBox.minY) / 2
            val lHalf = (boundingBox.maxZ - boundingBox.minZ) / 2

            val point = Vec3(
                boundingBox.center.x + wHalf * noiseX * amplitudeX,
                boundingBox.center.y + hHalf * noiseY * amplitudeY,
                boundingBox.center.z + lHalf * noiseZ * amplitudeZ,
            )

            Renderer3D.box(
                event.bufferSource, event.poseStack, FILLED_QUAD_TYPE,
                AABB(
                    point.x - pointSize, point.y - pointSize, point.z - pointSize,
                    point.x + pointSize, point.y + pointSize, point.z + pointSize,
                ), Color4f.WHITE
            )
        }
    }
}