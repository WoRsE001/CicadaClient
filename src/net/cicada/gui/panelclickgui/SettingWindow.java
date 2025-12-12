package net.cicada.gui.panelclickgui;

import lombok.Getter;
import lombok.Setter;
import net.cicada.gui.ComponentGui;
import net.cicada.module.api.Module;
import net.cicada.module.api.ModuleManager;
import net.cicada.module.setting.Setting;
import net.cicada.utility.GuiUtil;
import net.cicada.utility.Render.RenderUtil;
import net.cicada.utility.Render.StencilUtil;

import java.awt.*;

@Getter @Setter
public class SettingWindow extends ComponentGui {
    Module module;
    float prevX, prevY;
    boolean dragging, isClosed;

    public SettingWindow(float posX, float posY, Module module) {
        this.posX = posX;
        this.posY = posY;
        width = 150;
        height = 200;
        this.module = module;
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        float biggestWidth = 150;
        height = 22;
        for (Setting setting : module.getSettings()) {
            if (setting.getVisible().getAsBoolean()) {
                biggestWidth = Math.max(setting.getWidth() + mc.fontRendererObj.getStringWidth(setting.getName() + " ") + 4, biggestWidth);
                height += setting.getHeight() + 2;
            }
        }
        width = biggestWidth;

        if (dragging) {
            posX = prevX + mouseX;
            posY = prevY + mouseY;
        }

        StencilUtil.setUpTexture(posX, posY, width, height, 5);
        StencilUtil.writeTexture();
        RenderUtil.setGlColor(ModuleManager.CLICK_GUI.panelBGColor.getColor());
        RenderUtil.drawRect(posX, posY + 20, width, height);
        float offset = 22;
        for (Setting setting : module.getSettings()) {
            if (!setting.getVisible().getAsBoolean()) continue;
            setting.setPosX(posX + 2);
            setting.setPosY(posY + offset);
            setting.draw(mouseX, mouseY);
            offset += setting.getHeight() + 2;
        }
        RenderUtil.setGlColor(ModuleManager.CLICK_GUI.panelColor.getColor());
        RenderUtil.drawRect(posX, posY, width, 20);
        mc.fontRendererObj.drawString(module.getName(), posX + width / 2 - mc.fontRendererObj.getStringWidth(module.getName()) / 2F, posY + 10 - mc.fontRendererObj.FONT_HEIGHT / 2F, 0xFFFFFFFF);
        RenderUtil.setGlColor(new Color(129, 129, 129));
        RenderUtil.drawCircle(posX + width - 10, posY + 10, 5);
        RenderUtil.setGlColor(new Color(255, 255, 255));
        RenderUtil.drawLine(posX + width - 7, posY + 10, posX + width - 13, posY + 10, 2);
        StencilUtil.endWriteTexture();
    }

    @Override
    public boolean mousePressed(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0 && GuiUtil.mouseOver(posX, posY, width, 20, mouseX, mouseY)) {
            dragging = true;
            prevX = posX - mouseX;
            prevY = posY - mouseY;
            return true;
        }

        for (Setting setting : module.getSettings()) {
            if (!setting.getVisible().getAsBoolean()) continue;
            if (setting.mousePressed(mouseX, mouseY, mouseButton)) return true;
        }

        return mouseOver(mouseX, mouseY);
    }

    @Override
    public boolean mouseReleased(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0) dragging = false;

        for (Setting setting : module.getSettings()) {
            if (!setting.getVisible().getAsBoolean()) continue;
            if (setting.mouseReleased(mouseX, mouseY, mouseButton)) return true;
        }

        return mouseOver(mouseX, mouseY);
    }
}
