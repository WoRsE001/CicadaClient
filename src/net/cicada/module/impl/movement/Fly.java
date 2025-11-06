package net.cicada.module.impl.movement;

import net.cicada.event.impl.MotionEvent;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;
import net.cicada.event.api.Event;
import net.cicada.event.impl.PacketEvent;
import net.cicada.event.impl.UpdateEvent;
import net.cicada.module.api.Category;
import net.cicada.module.api.Module;
import net.cicada.module.api.ModuleInfo;
import net.cicada.module.setting.impl.BooleanSetting;
import net.cicada.module.setting.impl.ListSetting;
import net.cicada.module.setting.impl.NumberSetting;
import net.cicada.utility.MovementUtil;
import org.lwjgl.input.Keyboard;

import java.util.List;

@ModuleInfo(name = "Fly", category = Category.Movement, key = Keyboard.KEY_T)
public class Fly extends Module {
    ListSetting mode = new ListSetting("Mode", "Motion", List.of("Motion", "IntaveFlag", "MatrixJump"), () -> true, this);
    NumberSetting motionSpeed = new NumberSetting("Speed", 2, 0, 20, 0.01, () -> mode.getValue().equals("Motion"), this);
    BooleanSetting spigotBypass = new BooleanSetting("spigotBypass", true, () -> mode.getValue().equals("Motion"), this);
    BooleanSetting saveMotion = new BooleanSetting("saveMotion", false, () -> mode.getValue().equals("Motion"), this);

    boolean boosting;
    int boostTicks = 0;
    int ticks;
    boolean active;
    boolean receiveFlag;
    boolean jumped;
    boolean canBoost = false;

    @Override
    protected void onDisable() {
        if (mode.getValue().equals("Motion") && !saveMotion.isValue()) {
            mc.thePlayer.motionX = 0;
            mc.thePlayer.motionY = 0;
            mc.thePlayer.motionZ = 0;
        }

        this.boosting = this.active = this.receiveFlag = this.jumped = this.canBoost = false;
        this.boostTicks = this.ticks = 0;
        mc.timer.setTimer(1);
        super.onDisable();
    }

    @Override
    public void listen(Event event) {
        if (event instanceof UpdateEvent) {
            if (mode.is("Motion")) {
                MovementUtil.strafe(motionSpeed.getValue(), 1);
                mc.thePlayer.motionY = spigotBypass.isValue() ? -0.04 : 0;
                if (mc.gameSettings.keyBindJump.isKeyDown()) mc.thePlayer.motionY = motionSpeed.getValue();
                if (mc.gameSettings.keyBindSneak.isKeyDown()) mc.thePlayer.motionY = -motionSpeed.getValue();
            }

            if (mode.is("IntaveFlag")) {
                if (boosting) {
                    double boostValue = 2.2 + Math.random() * 0.25;
                    double yawRad = Math.toRadians(mc.thePlayer.rotationYaw) + Math.PI;
                    mc.thePlayer.motionX = -Math.sin(yawRad) * boostValue + ((Math.random() - 0.5) * 0.07);
                    mc.thePlayer.motionZ = Math.cos(yawRad) * boostValue + ((Math.random() - 0.5) * 0.07);
                    mc.thePlayer.motionY = 0.42 + (Math.random() - 0.5) * 0.08;
                    mc.thePlayer.fallDistance = 0f;
                    boostTicks++;
                    if (boostTicks >= 5) {
                        boosting = false;
                        mc.thePlayer.motionX = 0.0;
                        mc.thePlayer.motionZ = 0.0;
                    }
                }
            }

            if (mode.is("MatrixJump")) {
                mc.timer.setTimer(0.5F);
                if (mc.thePlayer.isCollidedVertically) {
                    if (!MovementUtil.isMoving()) {
                        MovementUtil.strafe(0.15, 1);
                    }

                    this.active = true;
                }

                if (this.ticks == 1) {
                    mc.thePlayer.motionY = 0.8;
                    mc.thePlayer.onGround = false;
                }

                if (this.active) {
                    ++this.ticks;
                }

                if (!mc.thePlayer.onGround) {
                    if (this.active) {
                        this.jumped = true;
                    }
                } else if (this.jumped) {
                    this.canBoost = true;
                }

                if (this.canBoost) {
                    MovementUtil.strafe(5.7, 1);
                    mc.thePlayer.motionY = 0.42;
                }

                if (this.receiveFlag) {
                    this.toggle();
                }
            }
        }

        if (event instanceof MotionEvent e && e.getPriority() == Event.Priority.Low) {
            if (mode.is("MatrixJump")) {
                if (this.ticks >= 1) {
                    e.setOnGround(false);
                }
            }
        }

        if (event instanceof PacketEvent e) {
            if (mode.is("IntaveFlag") && e.getPacket() instanceof S27PacketExplosion) {
                boosting = true;
                boostTicks = 0;
            }

            if (mode.is("MatrixJump")) {
                if (e.getPacket() instanceof S08PacketPlayerPosLook) {
                    if (this.canBoost) {
                        this.receiveFlag = true;
                    } else {
                        this.toggle();
                    }
                }

                if (e.getPacket() instanceof S12PacketEntityVelocity v && v.getEntityID() == mc.thePlayer.getEntityId()) {
                    event.cancel();
                }
            }
        }
    }
}
