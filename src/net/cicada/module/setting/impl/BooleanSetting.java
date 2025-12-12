package net.cicada.module.setting.impl;

import lombok.Getter;
import lombok.Setter;
import net.cicada.module.api.Module;
import net.cicada.module.setting.Setting;
import net.cicada.utility.GuiUtil;
import net.cicada.utility.Render.RenderUtil;

import java.awt.*;
import java.util.function.BooleanSupplier;

@Getter @Setter
public class BooleanSetting extends Setting {
    private boolean value;

    public BooleanSetting(String name, boolean value, BooleanSupplier visible, Module module) {
        super(name, 9, 9, visible, module);
        this.value = value;
    }

    public void toggle() {
        value = !value;
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        mc.fontRendererObj.drawString(name, posX, posY, 0xFFFFFFFF);
        RenderUtil.setGlColor(value ? new Color(255, 255, 255) : new Color(128, 128, 128));
        RenderUtil.drawRect(posX + mc.fontRendererObj.getStringWidth(name + " "), posY, width, height);
    }

    @Override
    public boolean mousePressed(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0) {
            if (GuiUtil.mouseOver(posX + mc.fontRendererObj.getStringWidth(name + " ") , posY, width, height, mouseX, mouseY)) {
                this.toggle();
                return true;
            }
        }

        return false;
    }
}
