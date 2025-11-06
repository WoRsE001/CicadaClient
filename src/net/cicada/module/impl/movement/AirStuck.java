package net.cicada.module.impl.movement;

import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.Vec3;
import net.cicada.event.api.Event;
import net.cicada.event.impl.PacketEvent;
import net.cicada.event.impl.UpdateEvent;
import net.cicada.module.api.Category;
import net.cicada.module.api.Module;
import net.cicada.module.api.ModuleInfo;
import org.lwjgl.input.Keyboard;

@ModuleInfo(name = "AirStuck", category = Category.Movement)
public class AirStuck extends Module {
    private Vec3 motion;

    @Override
    public void onEnable() {
        this.motion = new Vec3(mc.thePlayer.motionX, mc.thePlayer.motionY, mc.thePlayer.motionZ);
        super.onEnable();
    }

    @Override
    protected void onDisable() {
        mc.thePlayer.motionX = this.motion.xCoord;
        mc.thePlayer.motionY = this.motion.yCoord;
        mc.thePlayer.motionZ = this.motion.zCoord;
        super.onDisable();
    }

    @Override
    public void listen(Event event) {
        if (event instanceof UpdateEvent) {
            mc.thePlayer.motionX = 0;
            mc.thePlayer.motionY = 0;
            mc.thePlayer.motionZ = 0;
        }

        if (event instanceof PacketEvent e) {
            if (e.getPacket() instanceof C03PacketPlayer) event.cancel();
        }
    }
}
