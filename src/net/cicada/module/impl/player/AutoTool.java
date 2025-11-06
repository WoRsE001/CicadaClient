package net.cicada.module.impl.player;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.cicada.event.api.Event;
import net.cicada.event.impl.TickEvent;
import net.cicada.module.api.Category;
import net.cicada.module.api.Module;
import net.cicada.module.api.ModuleInfo;

import java.util.ArrayList;
import java.util.List;

@ModuleInfo(name = "AutoTool", category = Category.Player, state = true)
public class AutoTool extends Module {
    List<Integer> lastSlot = new ArrayList<>();

    @Override
    public void listen(Event event) {
        if (event instanceof TickEvent) {
            BlockPos targetBlock = mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK ? mc.objectMouseOver.getBlockPos() : null;
            if (targetBlock != null) {
                int bestSlot = findTool(targetBlock);
                if (bestSlot != -1 && mc.gameSettings.keyBindAttack.isKeyDown()) {
                    lastSlot.add(mc.thePlayer.inventory.currentItem);
                    mc.thePlayer.inventory.currentItem = bestSlot;
                } else if (!lastSlot.isEmpty()) {
                    mc.thePlayer.inventory.currentItem = lastSlot.get(0);
                    lastSlot.clear();
                }
            } else if (!lastSlot.isEmpty()) {
                mc.thePlayer.inventory.currentItem = lastSlot.get(0);
                lastSlot.clear();
            }
        }
    }

    private int findTool(BlockPos blockPos) {
        float bestSpeed = 1;
        int bestSlot = -1;

        IBlockState blockState = mc.theWorld.getBlockState(blockPos);

        for (int i = 0; i < 9; i++) {
            ItemStack itemStack = mc.thePlayer.inventory.getStackInSlot(i);

            if (itemStack == null) {
                continue;
            }

            float speed = itemStack.getStrVsBlock(blockState.getBlock());

            if (speed > bestSpeed) {
                bestSpeed = speed;
                bestSlot = i;
            }
        }

        return bestSlot;
    }
}