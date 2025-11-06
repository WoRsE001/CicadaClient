package net.cicada.module.impl.world;

import net.minecraft.block.BlockBed;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.cicada.event.api.Event;
import net.cicada.event.impl.UpdateEvent;
import net.cicada.module.api.Category;
import net.cicada.module.api.Module;
import net.cicada.module.api.ModuleInfo;
import org.lwjgl.input.Keyboard;

@ModuleInfo(name = "Fucker", category = Category.World, key = Keyboard.KEY_X)
public class Fucker extends Module {
    BlockPos bedPos = null;
    float breakProgress;

    @Override
    protected void onEnable() {
        breakProgress = 0;
        super.onEnable();
    }

    @Override
    public void listen(Event event) {
        if (event instanceof UpdateEvent) {
            this.bedPos = this.getBedPos();
            if (this.bedPos != null) {
                if (this.breakProgress == 0) {
                    mc.thePlayer.swingItem();
                    mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, this.bedPos, EnumFacing.UP));
                } else if (this.breakProgress >= 1) {
                    mc.thePlayer.swingItem();
                    mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, this.bedPos, EnumFacing.UP));
                } else {
                    mc.thePlayer.swingItem();
                }
                this.breakProgress += mc.theWorld.getBlockState(this.bedPos).getBlock().getPlayerRelativeBlockHardness(mc.thePlayer, mc.theWorld, this.bedPos);
                mc.theWorld.sendBlockBreakProgress(mc.thePlayer.getEntityId(), this.bedPos, (int) (this.breakProgress * 10));
            }
        }
    }

    private BlockPos getBedPos() {
        BlockPos playerPos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY + 1, mc.thePlayer.posZ);
        BlockPos blockPos = null;
        for (int x = playerPos.getX() - 4; x <= playerPos.getX() + 4; x++) {
            for (int y = playerPos.getY() - 4; y <= playerPos.getY() + 4; y++) {
                for (int z = playerPos.getZ() - 4; z <= playerPos.getZ() + 4; z++) {
                    if (!(mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock() instanceof BlockBed)) continue;
                    if (blockPos == null) blockPos = new BlockPos(x, y, z);
                    if (mc.thePlayer.getDistance(x + 0.5, y + 0.5, z + 0.5) < mc.thePlayer.getDistance(blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5)) blockPos = new BlockPos(x, y, z);
                }
            }
        }
        return blockPos;
    }
}
