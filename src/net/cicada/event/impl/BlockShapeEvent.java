package net.cicada.event.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.cicada.event.api.Event;

@Getter @Setter @AllArgsConstructor
public class BlockShapeEvent extends Event {
    private BlockPos pos;
    private IBlockState state;
    private AxisAlignedBB boundingBox;
}
