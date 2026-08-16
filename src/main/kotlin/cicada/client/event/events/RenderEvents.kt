package cicada.client.event.events

import cicada.client.event.Event
import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.DeltaTracker
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.renderer.MultiBufferSource

interface EventRender {
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