package net.cicada.module.impl.render;

import net.minecraft.block.BlockBed;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.cicada.event.api.Event;
import net.cicada.event.impl.Render3DEvent;
import net.cicada.event.impl.RenderChunkEvent;
import net.cicada.event.impl.TickEvent;
import net.cicada.module.api.Category;
import net.cicada.module.api.Module;
import net.cicada.module.api.ModuleInfo;
import net.cicada.utility.RenderUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@ModuleInfo(name = "BlockESP", category = Category.Render)
public class BlockESP extends Module {
    List<BlockPos> foundBlocks = new ArrayList<>();

    @Override
    public void listen(Event event) {
        if (event instanceof TickEvent) {
            foundBlocks.removeIf(foundBlock -> !(mc.theWorld.getBlockState(foundBlock).getBlock() instanceof BlockBed));
        }

        if (event instanceof RenderChunkEvent e) {
            if (e.getIBlockState().getBlock() instanceof BlockBed && !foundBlocks.contains(e.getBlockPos())) {
                foundBlocks.add(e.getBlockPos());
            }
        }

        if (event instanceof Render3DEvent) {
            for (BlockPos foundBlock : foundBlocks) {
                RenderUtil.start3D();
                RenderUtil.render3DBox(new AxisAlignedBB(foundBlock.getX(), foundBlock.getY(), foundBlock.getZ(), foundBlock.getX() + 1, foundBlock.getY() + 1, foundBlock.getZ() + 1), false, new Color(255, 0, 0, 80));
                RenderUtil.stop3D();
            }
        }
    }
}
