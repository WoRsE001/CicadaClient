package net.cicada.module.impl.render;

import net.cicada.event.api.Event;
import net.cicada.event.impl.Render2DEvent;
import net.cicada.module.api.Category;
import net.cicada.module.api.Module;
import net.cicada.module.api.ModuleInfo;
import net.cicada.module.api.ModuleManager;
import net.cicada.module.setting.impl.NumberSetting;
import net.cicada.utility.Render.RenderUtil;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;

import java.awt.*;

@ModuleInfo(name = "TargetHud", category = Category.Render)
public class TargetHud extends Module {
    NumberSetting posX = new NumberSetting("PosX", 0.5, 0, 1, 0.01, () -> true, this);
    NumberSetting posY = new NumberSetting("PosY", 0.6, 0, 1, 0.01, () -> true, this);

    @Override
    public void listen(Event event) {
        if (event instanceof Render2DEvent) {
            if (ModuleManager.ATTACK_AURA.target instanceof EntityPlayer target) {
                ScaledResolution sr = new ScaledResolution(mc);
                RenderUtil.setGlColor(new Color(0, 0, 0));
                RenderUtil.drawRoundRect(sr.getScaledWidth() * (float) posX.getValue() - 75, sr.getScaledHeight() * (float) posY.getValue() - 30, 150, 60, 10);
                mc.fontRendererObj.drawString("Name: " + target.getName(), sr.getScaledWidth() * (float) posX.getValue() - 18, sr.getScaledHeight() * (float) posY.getValue() - 25, 0xFFFFFFFF);
                mc.fontRendererObj.drawString("Health: " + String.format("%.2f", target.getHealth()), sr.getScaledWidth() * (float) posX.getValue() - 18, sr.getScaledHeight() * (float) posY.getValue() - 15, 0xFFFFFFFF);
                mc.fontRendererObj.drawString("Armor: " + target.getTotalArmorValue() * 5 + "%", sr.getScaledWidth() * (float) posX.getValue() - 18, sr.getScaledHeight() * (float) posY.getValue() - 5, 0xFFFFFFFF);
                mc.fontRendererObj.drawString("HurtTime: " + target.hurtTime, sr.getScaledWidth() * (float) posX.getValue() - 18, sr.getScaledHeight() * (float) posY.getValue() + 5, 0xFFFFFFFF);
                mc.fontRendererObj.drawString("TicksExisted: " + target.ticksExisted, sr.getScaledWidth() * (float) posX.getValue() - 18, sr.getScaledHeight() * (float) posY.getValue() + 15, 0xFFFFFFFF);
                RenderUtil.drawPlayerHead(sr.getScaledWidth() * (float) posX.getValue() - 70, sr.getScaledHeight() * (float) posY.getValue() - 25, 50, target);
            }
        }
    }
}
