package net.cicada.module.impl.world;

import net.cicada.event.impl.MovementInputEvent;
import net.minecraft.block.BlockAir;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.cicada.event.api.Event;
import net.cicada.event.impl.MovementEvent;
import net.cicada.module.api.Category;
import net.cicada.module.api.Module;
import net.cicada.module.api.ModuleInfo;

@ModuleInfo(name = "BridgeAssist", category = Category.World)
public class BridgeAssist extends Module {
    @Override
    public void listen(Event event) {
        if (event instanceof MovementInputEvent e && mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.getPositionVector().add(new Vec3(0, -0.5, 0)))).getBlock() instanceof BlockAir) {
            e.setSneak(true);
        }
    }
}
