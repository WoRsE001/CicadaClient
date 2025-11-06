package net.cicada.module.impl.render;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.server.S01PacketJoinGame;
import net.cicada.event.api.Event;
import net.cicada.event.impl.PacketEvent;
import net.cicada.event.impl.Render3DEvent;
import net.cicada.event.impl.TickEvent;
import net.cicada.module.api.Category;
import net.cicada.module.api.Module;
import net.cicada.module.api.ModuleInfo;
import net.cicada.utility.RenderUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@ModuleInfo(name = "MurderHighlighter", category = Category.Render)
public class MurderHighlighter extends Module {
    List<String> murders = new ArrayList<>();
    List<String> sheriffs = new ArrayList<>();

    @Override
    public void listen(Event event) {
        if (event instanceof TickEvent) {
            for (Entity entity : mc.theWorld.getLoadedEntityList()) {
                if (entity instanceof EntityPlayer player && player != mc.thePlayer && player.getHeldItem() != null) {
                    if (player.getHeldItem().getItem() instanceof ItemSword && !murders.contains(player.getName())) murders.add(player.getName());
                    if (player.getHeldItem().getItem() instanceof ItemBow && !sheriffs.contains(player.getName())) sheriffs.add(player.getName());
                }
            }
        }

        if (event instanceof Render3DEvent) {
            for (Entity entity : mc.theWorld.getLoadedEntityList()) {
                RenderUtil.start3D();
                if (murders.contains(entity.getName())) RenderUtil.render3DEntityBox(entity, false, true, new Color(255, 0, 0, 255));
                else if (sheriffs.contains(entity.getName())) RenderUtil.render3DEntityBox(entity, false, true, new Color(255, 255, 0, 255));
                RenderUtil.stop3D();
            }
        }

        if (event instanceof PacketEvent e && e.getPacket() instanceof S01PacketJoinGame) {
            murders.clear();
            sheriffs.clear();
        }
    }
}
