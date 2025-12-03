package net.cicada.module.impl.movement;

import net.cicada.event.impl.*;
import net.cicada.module.setting.impl.ListSetting;
import net.cicada.module.setting.impl.NumberSetting;
import net.cicada.event.api.Event;
import net.cicada.module.api.Category;
import net.cicada.module.api.Module;
import net.cicada.module.api.ModuleInfo;
import net.cicada.utility.Player.MovementUtil;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@ModuleInfo(name = "LongJump", category = Category.Movement)
public class LongJump extends Module {
    ListSetting mode = new ListSetting("Mode", "Motion", List.of("Motion", "MatrixFlag", "MatrixLong"), () -> true, this);
    NumberSetting speed = new NumberSetting("Speed", 1.4, 0, 10, 0.01, () -> this.mode.is("MatrixLong"), this);
    NumberSetting tick = new NumberSetting("Ticks", 0, 0, 50, 1, () -> this.mode.is("MatrixLong"), this);
    NumberSetting timerSpeed = new NumberSetting("TimerSpeed", 1, 1, 10, 0.01, () -> this.mode.is("MatrixLong"), this);

    List<Packet<?>> packets = new CopyOnWriteArrayList<>();
    private boolean canBoost;
    private boolean flag;
    private boolean sent;
    private boolean noBlink = true;
    private double x;
    private double z;
    private double y;
    private double firstDir;
    private int ticks;

    public void onEnable() {
        this.canBoost = false;
        this.flag = false;
        this.sent = false;
        this.ticks = 0;
        this.x = mc.thePlayer.posX;
        this.z = mc.thePlayer.posZ;
        this.y = mc.thePlayer.posY;
        this.firstDir = mc.thePlayer.rotationYaw;
    }

    public void onDisable() {
        this.blink();
        this.noBlink = true;
        mc.timer.setTimer(1);
    }

    @Override
    public void listen(Event event) {
        if (event instanceof MovementEvent m){
            if (!this.canBoost) {
                m.setMoveForward(0);
                m.setMoveStrafe(0);
            }
        }


        if (event instanceof PacketEvent p && p.getType() == PacketEvent.Type.Send) {
            if (p.getPacket() instanceof C0FPacketConfirmTransaction) {
                event.cancel();
                return;
            }

            if (p.getPacket() instanceof C00PacketKeepAlive) return;

            if (!this.noBlink) {
                //this.packets.add(p.getPacket());
                //event.cancel();
            }
        }

        if (event instanceof LookEvent l){
            if (!this.sent) {
                l.setRotationYaw((float) this.firstDir);
                l.setRotationPitch(1);
            }

        }

        if (event instanceof MotionEvent m) {
            m.setOnGround(false);
            if (!this.sent) {
                m.setPosX(this.x);
                m.setPosY(this.y);
                m.setPosZ(this.z);
                if (m.getPriority() == Event.Priority.High) {
                    this.x += -Math.sin(Math.toRadians(this.firstDir)) * (0.2496 - (this.ticks % 3 == 0 ? 0.0806 : (double) 0.0F));
                    this.z += Math.cos(Math.toRadians(this.firstDir)) * (0.2496 - (this.ticks % 3 == 0 ? 0.0806 : (double) 0.0F));
                }
            }

        }

        if (event instanceof UpdateEvent){
            if (!this.sent) {
                mc.thePlayer.motionX = mc.thePlayer.motionY = mc.thePlayer.motionZ = 0;
                mc.timer.setTimer((float) this.timerSpeed.getValue());
                this.noBlink = false;
                if ((double) this.ticks > (Double) this.tick.getValue()) {
                    this.blink();
                    this.noBlink = true;
                    this.sent = true;
                    this.ticks = 0;
                    this.canBoost = true;
                    mc.timer.setTimer(1);
                }
            }

            if (this.canBoost) {
                MovementUtil.strafe(this.speed.getValue(), 1);
                mc.thePlayer.motionY = 0.42;
                if (this.flag) {
                    this.toggle();
                }
            }

            ++this.ticks;
        }

        if (event instanceof PacketEvent p && p.getType() == PacketEvent.Type.Receive){
            if (p.getPacket() instanceof S08PacketPlayerPosLook && this.canBoost) {
                this.flag = true;
            }
        }
    }

    private void blink() {
        for (Packet<?> packet : this.packets) {
            mc.thePlayer.sendQueue.addToSendQueue(packet);
        }
        this.packets.clear();
    }
}
