package cicada.client.event.impl

import cicada.client.event.Event
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.shapes.VoxelShape

// SCWGxD regrets everything he did. 24.06.2026 14:49.
object EventBlockShape : Event {
    lateinit var state: BlockState
    lateinit var pos: BlockPos
    lateinit var shape: VoxelShape
}