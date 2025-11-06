package net.cicada.module.impl.movement;

import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.cicada.event.api.Event;
import net.cicada.event.impl.PacketEvent;
import net.cicada.event.impl.UpdateEvent;
import net.cicada.module.api.Category;
import net.cicada.module.api.Module;
import net.cicada.module.api.ModuleInfo;
import net.cicada.utility.MovementUtil;

@ModuleInfo(name = "LongJump", category = Category.Movement)
public class LongJump extends Module {
    private boolean receivedFlag;
    private boolean canBoost;
    private boolean boosted;
    private boolean touchGround;

    @Override
    protected void onDisable() {
        this.boosted = false;
        this.canBoost = false;
        this.receivedFlag = false;
        this.touchGround = false;
        super.onDisable();
    }

    @Override
    public void listen(Event event) {
        if (event instanceof PacketEvent e && e.getPacket() instanceof S08PacketPlayerPosLook) {
            this.receivedFlag = true;
        }

        if (event instanceof UpdateEvent) {
            if (!mc.thePlayer.onGround && this.touchGround) {
                this.touchGround = false;
            }

            if (mc.thePlayer.onGround && !this.touchGround) {
                mc.thePlayer.jump();
                this.boosted = false;
            }

            if (mc.thePlayer.fallDistance >= (double)0.25F && !this.boosted) {
                this.canBoost = true;
            }

            if (this.canBoost) {
                MovementUtil.strafe(2.7, 1);
                mc.thePlayer.motionY = 0.42;
                this.boosted = true;
            }

            if (this.receivedFlag && this.boosted) {
                this.toggle();
                this.canBoost = false;
                this.receivedFlag = false;
            }
        }
    }
}
