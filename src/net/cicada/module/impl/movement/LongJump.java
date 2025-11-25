package net.cicada.module.impl.movement;

import net.cicada.module.setting.impl.ListSetting;
import net.cicada.module.setting.impl.NumberSetting;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.cicada.event.api.Event;
import net.cicada.event.impl.PacketEvent;
import net.cicada.event.impl.UpdateEvent;
import net.cicada.module.api.Category;
import net.cicada.module.api.Module;
import net.cicada.module.api.ModuleInfo;
import net.cicada.utility.Player.MovementUtil;

import java.util.List;

@ModuleInfo(name = "LongJump", category = Category.Movement)
public class LongJump extends Module {
    ListSetting mode = new ListSetting("Mode", "Motion", List.of("Motion", "MatrixFlag", "MatrixLong"), () -> true, this);
    NumberSetting timerSpeed = new NumberSetting("TimerSpeed", 1, 0.1, 1, 0.01, () -> this.mode.is("MatrixLong"), this);
    NumberSetting lagSpeed = new NumberSetting("LagSpeed", 1.4, -2.4, 2.4, 0.01, () -> this.mode.is("MatrixLong"), this);
    NumberSetting ticks = new NumberSetting("Ticks", 5, 1, 12, 1, () -> this.mode.is("MatrixLong"), this);
    NumberSetting boostSpeed = new NumberSetting("BoostSpeed", 8.3, 0, 10, 0.01, () -> this.mode.is("MatrixLong"), this);

    int offGroundTicks;
    boolean flag1, flag2, flag3, flag4, flag5, flag6;

    @Override
    protected void onDisable() {
        this.offGroundTicks = 0;
        this.flag1 = this.flag2 = this.flag3 = this.flag4 = this.flag5 = this.flag6 = false;
        mc.timer.setTimer(1);
        super.onDisable();
    }

    @Override
    public void listen(Event event) {
        if (event instanceof PacketEvent e) {
            if (this.mode.is("MatrixLong")) {
                if (e.getType() == PacketEvent.Type.Receive) {
                    if (e.getPacket() instanceof S08PacketPlayerPosLook) {
                        if (!this.flag4) {
                            this.flag3 = true;
                        } else if (this.flag2) {
                            this.flag3 = true;
                        }
                    }
                }
            }
        }

        if (event instanceof UpdateEvent) {
            if (mc.thePlayer.onGround) {
                this.offGroundTicks++;
                if (this.flag6) {
                    this.offGroundTicks = 0;
                    this.flag6 = false;
                }
            } else if (!this.flag6) {
                this.flag6 = true;
            }

            if (!this.flag4) {
                if (this.flag1 && this.offGroundTicks >= this.ticks.getValue()) {
                    this.flag4 = true;
                }

                if (mc.thePlayer.fallDistance > 0.5F && !this.flag1) {
                    this.flag5 = true;
                }

                if (this.flag5 && !this.flag3) {
                    MovementUtil.strafe(this.lagSpeed.getValue(), 1);
                    mc.thePlayer.motionY = 0.42;
                }

                if (this.flag3 && this.flag5) {
                    MovementUtil.strafe(this.lagSpeed.getValue(), 1);
                    mc.thePlayer.motionY = 0.42;
                    this.offGroundTicks = 0;
                    this.flag1 = true;
                    this.flag3 = false;
                    this.flag5 = false;
                    mc.thePlayer.fallDistance = 0.0F;
                    mc.timer.setTimer((float) this.timerSpeed.getValue());
                }
            }

            if (this.flag4) {
                MovementUtil.strafe(this.boostSpeed.getValue(), 1);
                mc.thePlayer.motionY = 0.42;
                this.flag2 = true;
                if (this.flag3) this.toggle();
            }
        }
    }
}
