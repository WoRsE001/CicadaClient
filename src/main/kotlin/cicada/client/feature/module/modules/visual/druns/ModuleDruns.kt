package cicada.client.feature.module.modules.visual.druns

import cicada.client.event.Event
import cicada.client.event.impl.RenderEvent
import cicada.client.event.impl.EventTick
import cicada.client.feature.module.ClientModule
import cicada.client.feature.module.ModuleCategory
import cicada.client.feature.module.modules.combat.attackaura.ModuleAttackAura
import cicada.client.utils.client.mc
import cicada.client.utils.client.player
import cicada.client.utils.rotation.rotation
import net.minecraft.world.item.BlockItem
import org.joml.Vector3f
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

object ModuleDruns : ClientModule("Druns", ModuleCategory.VISUAL) {
    private val druns = List(10) { Drun() }
    private var timer = 0f

    override fun onEvent(event: Event) {
        if (event is EventTick.Pre) {
            for ((i, drun) in druns.withIndex()) {
                val target = if (ModuleAttackAura.target != null) {
                    val position = ModuleAttackAura.target!!.boundingBox.center
                    Vector3f(
                        (position.x + cos(PI / druns.size * 2 * i + timer) * 0.6).toFloat(),
                        (position.y + sin(PI / druns.size * 2 * i + timer * PI) * 0.6).toFloat(),
                        (position.z + sin(PI / druns.size * 2 * i + timer) * 0.6).toFloat(),
                    )
                } // else if (player.mainHandItem.item is BlockItem) {
                    // player.eyePosition.add(player.rotation().directionVector.multiply(4.5, 4.5, 4.5)).toVector3f()
                // }
                else {
                    val position = player.boundingBox.center
                    Vector3f(
                        (position.x + cos(PI / druns.size * 2 * i + timer) * 0.6).toFloat(),
                        (position.y + sin(PI / druns.size * 2 * i + timer * PI) * 0.6).toFloat(),
                        (position.z + sin(PI / druns.size * 2 * i + timer) * 0.6).toFloat(),
                    )
                }

                drun.goto(target, 3f)
            }

            timer += 0.1f
        }

        if (event is RenderEvent.World) {
            for (drun in druns) {
                drun.draw(event.bufferSource, event.poseStack, mc.deltaTracker.getGameTimeDeltaPartialTick(true))
            }
        }
    }
}