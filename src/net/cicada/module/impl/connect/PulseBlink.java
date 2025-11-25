package net.cicada.module.impl.connect;

import net.cicada.event.api.Event;
import net.cicada.event.impl.*;
import net.cicada.module.api.Category;
import net.cicada.module.api.Module;
import net.cicada.module.api.ModuleInfo;
import net.cicada.module.setting.impl.BooleanSetting;
import net.cicada.module.setting.impl.MultiBooleanSetting;
import net.cicada.module.setting.impl.NumberSetting;
import net.cicada.utility.Render.RenderUtil;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;

import java.awt.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@ModuleInfo(name = "PulseBlink", category = Category.Connect)
public class PulseBlink extends Module {
    NumberSetting delay = new NumberSetting("Delay", 500, 0, 1000, 1, () -> true, this);
    MultiBooleanSetting blinkAction = new MultiBooleanSetting("BlinkAction", () -> true, this)
            .add("Attack", false)
            .add("BlockPlace", false)
            .add("InGUI", false)
            .add("UseItem", false);
    BooleanSetting showServerPos = new BooleanSetting("ShowServerPos", false, () -> true, this);
    BooleanSetting onlyOnF5 = new BooleanSetting("onlyOnF5", true, () -> this.showServerPos.isValue(), this);

    List<Packet<?>> packetQueue = new CopyOnWriteArrayList<>();
    List<Vec3> posQueue = new CopyOnWriteArrayList<>();

    long timer;

    @Override
    protected void onEnable() {
        this.timer = System.currentTimeMillis();
        super.onEnable();
    }

    @Override
    protected void onDisable() {
        this.blink();
        super.onDisable();
    }

    @Override
    public void listen(Event event) {
        if (event instanceof AttackEvent && this.blinkAction.is("Attack")) {
            this.blink();
        }

        if (event instanceof GameLoopEvent) {
            if (System.currentTimeMillis() - this.timer >= this.delay.getValue()) {
                this.blink();
                this.timer = System.currentTimeMillis();
            }
        }

        if (event instanceof PacketEvent p && p.getType() == PacketEvent.Type.Send) {
            if (p.getPacket() instanceof C01PacketChatMessage) return;

            if (p.getPacket() instanceof C08PacketPlayerBlockPlacement && this.blinkAction.is("BlockPlace")) {
                this.blink();
                return;
            }

            event.cancel();
            packetQueue.add(p.getPacket());
            if (p.getPacket() instanceof C03PacketPlayer c03 && c03.isMoving()) {
                posQueue.add(new Vec3(c03.getPositionX(), c03.getPositionY(), c03.getPositionZ()));
            }
        }

        if (event instanceof Render3DEvent r && r.getPriority() == Event.Priority.High && this.showServerPos.isValue() && (!this.onlyOnF5.isValue() || mc.gameSettings.thirdPersonView > 0)) {
            if (!posQueue.isEmpty()) {
                RenderUtil.setGlColor(new Color(0, 255, 0, 255));
                float width = (float) (mc.thePlayer.getEntityBoundingBox().minX - mc.thePlayer.getEntityBoundingBox().maxX);
                float height = (float) (mc.thePlayer.getEntityBoundingBox().minY - mc.thePlayer.getEntityBoundingBox().maxY);
                float length = (float) (mc.thePlayer.getEntityBoundingBox().minZ - mc.thePlayer.getEntityBoundingBox().maxZ);
                RenderUtil.render3DHitBox(new AxisAlignedBB(-width / 2, 0, -length / 2, width / 2, -height, length / 2).offset(posQueue.getFirst().xCoord, posQueue.getFirst().yCoord, posQueue.getFirst().zCoord), 4);
            }
        }

        if (event instanceof TickEvent) {
            //if (mc.currentScreen != null && this.blinkAction.is("InGUI")) this.blink();
        }
    }

    private void blink() {
        for (Packet<?> packet : this.packetQueue)
            mc.getNetHandler().getNetworkManager().sendInvisiblePacket(packet);
        this.packetQueue.clear();
        this.posQueue.clear();
    }
}
