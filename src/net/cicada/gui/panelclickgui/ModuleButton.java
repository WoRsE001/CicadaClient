package net.cicada.gui.panelclickgui;

import lombok.Getter;
import net.cicada.gui.ComponentGui;
import net.cicada.module.api.Module;
import net.cicada.module.api.ModuleManager;
import net.cicada.utility.Render.RenderUtil;

import java.awt.*;

@Getter
public class ModuleButton extends ComponentGui {
    Module module;

    public ModuleButton(float posX, float posY, Module module) {
        this.posX = posX;
        this.posY = posY;
        width = 94;
        height = 20;
        this.module = module;
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        RenderUtil.setGlColor(module.isState() ? ModuleManager.CLICK_GUI.turnedOnModuleColor.getColor() : ModuleManager.CLICK_GUI.turnedOffModuleColor.getColor());
        RenderUtil.drawRoundRect(posX, posY, width, height, 2);
        mc.fontRendererObj.drawString(module.getName(), posX + width / 2 - mc.fontRendererObj.getStringWidth(module.getName()) / 2F, posY + height / 2 - mc.fontRendererObj.FONT_HEIGHT / 2F, 0xFFFFFFFF);
    }

    @Override
    public boolean mousePressed(int mouseX, int mouseY, int mouseButton) {
        if (mouseOver(mouseX, mouseY)) {
            if (mouseButton == 0) {
                module.toggle();
                return true;
            }
        }
        return false;
    }
}
