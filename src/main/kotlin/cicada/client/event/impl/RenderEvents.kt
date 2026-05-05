package cicada.client.event.impl

import com.mojang.blaze3d.vertex.PoseStack
import cicada.client.event.Event
import net.minecraft.client.DeltaTracker
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.renderer.MultiBufferSource

interface RenderEvent {
    interface Gui {
        object Pre : Event {
            lateinit var graphics: GuiGraphicsExtractor
            lateinit var deltaTracker: DeltaTracker
        }

        object Post : Event {
            lateinit var graphics: GuiGraphicsExtractor
            lateinit var deltaTracker: DeltaTracker
        }
    }

    object World : Event {
        lateinit var poseStack: PoseStack
        lateinit var bufferSource: MultiBufferSource.BufferSource
        lateinit var deltaTracker: DeltaTracker
    }
}