package net.cicada.module.impl.connect;

import net.cicada.event.api.Event;
import net.cicada.event.impl.*;
import net.cicada.module.api.Category;
import net.cicada.module.api.Module;
import net.cicada.module.api.ModuleInfo;
import net.cicada.module.setting.impl.MultiBooleanSetting;
import net.cicada.module.setting.impl.NumberSetting;
import net.cicada.utility.RenderUtil;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;

import java.awt.*;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;

@ModuleInfo(name = "Ping", category = Category.Connect)
public class Ping extends Module {

    private final NumberSetting maxDelay = new NumberSetting("MaxDelay", 100, 50, 1000, 1, () -> true, this);

    private final NumberSetting delayBeforeNextLagAfterReset = new NumberSetting("DelayBeforeNextLagAfterReset", 0, 0, 1000, 1, () -> true, this);
    private final MultiBooleanSetting actions = new MultiBooleanSetting("ActionsToReset", () -> true, this)
            .add("Attack", true)
            .add("Damage", true)
            .add("Velocity", true)
            .add("Flag", true)
            .add("UsingItem", true)
            .add("PlaceBlock", true)
            .add("OpenedGui", true);

    private long lastResetTime;
    private int delayBeforeNextLag;
    private final ConcurrentLinkedQueue<PacketWithTime> buffer = new ConcurrentLinkedQueue<>();
    private final List<VecWithTime> posBuffer = new CopyOnWriteArrayList<>();

    Vec3 lastPos, currentPos;

    @Override
    public void onDisable() {
        resetAllPackets();
    }

    @Override
    public void listen(Event event) {
        if (mc.thePlayer == null || mc.theWorld == null) return;
        if (mc.isIntegratedServerRunning()) return;
        long currentTime = System.currentTimeMillis();
        if ((mc.currentScreen instanceof GuiInventory || mc.currentScreen instanceof GuiChest) && actions.is("OpenedGui"))
            reset();
        switch (event) {
            case PacketEvent e -> {
                if (currentTime - lastResetTime < delayBeforeNextLag) {
                    resetAllPackets();
                    break;
                }

                if (actions.is("Damage") && mc.thePlayer.hurtTime != 0) {
                    reset();
                    break;
                }

                if (actions.is("UsingItem") && mc.thePlayer.isUsingItem()) {
                    reset();
                    break;
                }

                Packet packet = e.getPacket();

                switch (packet) {
                    case C02PacketUseEntity handlingPacket -> {
                        if (actions.is("Attack") && handlingPacket.getAction() == C02PacketUseEntity.Action.ATTACK) {
                            reset();
                            return;
                        }
                    }

                    case S12PacketEntityVelocity handlingPacket -> {
                        if (actions.is("Velocity") && handlingPacket.getEntityID() == mc.thePlayer.getEntityId()) {
                            reset();
                            return;
                        }
                    }

                    case S08PacketPlayerPosLook ignored -> {
                        if (actions.is("Flag")) {
                            reset();
                            return;
                        }
                    }

                    case C08PacketPlayerBlockPlacement ignored -> {
                        if (actions.is("PlaceBlock")) {
                            reset();
                            return;
                        }
                    }

                    default -> {
                    }
                }

                e.cancel();
                buffer.add(new PacketWithTime(packet, currentTime));
                if (packet instanceof C03PacketPlayer c03) {
                    if (c03.isMoving()) {
                        posBuffer.add(new VecWithTime(new Vec3(c03.getPositionX(), c03.getPositionY(), c03.getPositionZ()), currentTime));
                    }
                }
            }
            case GameLoopEvent ignored -> handlePackets();
            case MotionEvent ignored -> {
                while (posBuffer.size() > (maxDelay.getValue() / 50)) {
                    posBuffer.removeFirst();
                }
            }
            case TickEvent ignored -> {
                lastPos = currentPos;
                if (!posBuffer.isEmpty()) currentPos = posBuffer.getFirst().pos;
            }
            case Render3DEvent ignored -> {
                if (!posBuffer.isEmpty()) {
                    RenderUtil.start3D();
                    Vec3 diff = posBuffer.getFirst().pos.subtract(mc.thePlayer.getPositionVector());
                    AxisAlignedBB pingPlayerBox = mc.thePlayer.getEntityBoundingBox().offset(diff.xCoord, diff.yCoord, diff.zCoord);
                    RenderUtil.setGlColor(new Color(0, 0, 0, 100));
                    RenderUtil.render3DBox(pingPlayerBox);
                    RenderUtil.stop3D();
                }
            }
            default -> {
            }
        }
    }

    private void handlePackets() {
        buffer.removeIf(packetWithTime -> {
            if (System.currentTimeMillis() - packetWithTime.time() >= maxDelay.getValue()) {
                mc.getNetHandler().getNetworkManager().sendInvisiblePacket(packetWithTime.packet());
                return true;
            }
            return false;
        });
        posBuffer.removeIf(pos -> System.currentTimeMillis() - pos.time() >= maxDelay.getValue());
    }

    private void resetAllPackets() {
        buffer.forEach(packetWithTime -> mc.getNetHandler().getNetworkManager().sendInvisiblePacket(packetWithTime.packet()));
        buffer.clear();
        posBuffer.clear();
    }

    private void reset() {
        resetAllPackets();
        lastResetTime = System.currentTimeMillis();
        delayBeforeNextLag = (int) this.delayBeforeNextLagAfterReset.getValue();
    }

    private record PacketWithTime(Packet packet, long time) {}
    private record VecWithTime(Vec3 pos, long time) {}
}
