package net.cicada.module.impl.movement;

import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.cicada.event.api.Event;
import net.cicada.event.impl.MotionEvent;
import net.cicada.event.impl.SlowDownEvent;
import net.cicada.module.api.Category;
import net.cicada.module.api.Module;
import net.cicada.module.api.ModuleInfo;
import net.cicada.module.setting.impl.ListSetting;
import net.cicada.module.setting.impl.NumberSetting;

import java.util.List;

@ModuleInfo(name = "NoSlow", category = Category.Movement)
public class NoSlow extends Module {
    ListSetting consumeMode = new ListSetting("ConsumeMode", "Intave", List.of("None", "Intave"), () -> true, this);
    NumberSetting consumeForwardMultiplier = new NumberSetting("ConsumeForwardMultiplier", 1, 0.2, 1, 0.01, () -> true, this);
    NumberSetting consumeStrafeMultiplier = new NumberSetting("ConsumeStrafeMultiplier", 1, 0.2, 1, 0.01, () -> true, this);
    ListSetting blockMode = new ListSetting("BlockMode", "Intave", List.of("None", "Intave"), () -> true, this);
    NumberSetting blockForwardMultiplier = new NumberSetting("BlockForwardMultiplier", 1, 0.2, 1, 0.01, () -> true, this);
    NumberSetting blockStrafeMultiplier = new NumberSetting("BlockStrafeMultiplier", 1, 0.2, 1, 0.01, () -> true, this);

    @Override
    public void listen(Event event) {
        if (event instanceof MotionEvent e && mc.thePlayer.isUsingItem() && !mc.thePlayer.isRiding()) {
            Item currentItem = mc.thePlayer.inventory.getCurrentItem().getItem();
            if (currentItem instanceof ItemFood) {
                if (consumeMode.getValue().equals("Intave")) {
                    if (e.getPriority() == Event.Priority.Low) {
                        mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.UP));
                    }
                }
            } else if (currentItem instanceof ItemSword) {
                if (blockMode.getValue().equals("Intave")) {
                    if(e.getPriority() == Event.Priority.Low) {
                        mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                        mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, mc.thePlayer.inventory.getCurrentItem(), 0, 0, 0));
                    }
                }
            }
        }

        if (event instanceof SlowDownEvent e) {
            Item currentItem = mc.thePlayer.inventory.getCurrentItem().getItem();
            if (currentItem instanceof ItemFood) {
                e.setForwardSlowDownFactor((float) consumeForwardMultiplier.getValue());
                e.setStrafeSlowDownFactor((float) consumeStrafeMultiplier.getValue());
            } else if (currentItem instanceof ItemSword) {
                e.setForwardSlowDownFactor((float) blockForwardMultiplier.getValue());
                e.setStrafeSlowDownFactor((float) blockStrafeMultiplier.getValue());
            }
        }
    }
}
