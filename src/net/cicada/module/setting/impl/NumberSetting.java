package net.cicada.module.setting.impl;

import lombok.Getter;
import lombok.Setter;
import net.cicada.utility.MathUtil;
import net.cicada.utility.Render.RenderUtil;
import net.minecraft.util.MathHelper;
import net.cicada.module.api.Module;
import net.cicada.module.setting.Setting;
import net.cicada.utility.GuiUtil;

import java.awt.*;
import java.util.function.BooleanSupplier;

@Getter @Setter
public class NumberSetting extends Setting {
    private double value, minValue, maxValue, step;
    private boolean dragging;

    public NumberSetting(String name, double value, double minValue, double maxValue, double step, BooleanSupplier visible, Module module) {
        super(name, 80, 9, visible, module);
        this.value = MathUtil.round(value, step);
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.step = step;
    }

    public void setValue(double value) {
        this.value = MathUtil.round(value, step);
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        if (dragging) {
            float posXWithText = posX + mc.fontRendererObj.getStringWidth(name + " ");
            double convertMousePos = MathUtil.map(mouseX, posXWithText, posXWithText + width, minValue, maxValue);
            setValue(MathHelper.clamp_double(convertMousePos, minValue, maxValue));
        }

        mc.fontRendererObj.drawString(name, posX, posY, 0xFFFFFFFF);
        RenderUtil.setGlColor(new Color(128, 128, 128));
        RenderUtil.drawRoundRect(posX + mc.fontRendererObj.getStringWidth(name + " "), posY + 1, width, 7, 3);
        float convertValue = (float) MathUtil.map(value, minValue, maxValue, 6, width);
        RenderUtil.setGlColor(new Color(255, 255, 255));
        RenderUtil.drawRoundRect(posX + mc.fontRendererObj.getStringWidth(name + " "), posY + 1, convertValue, 7, 3);
        mc.fontRendererObj.drawString(String.format("%.2f", value), posX + mc.fontRendererObj.getStringWidth(name + " ") + width / 2, posY + 1, 0xFF000000);
    }

    @Override
    public boolean mousePressed(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0 && GuiUtil.mouseOver(posX + mc.fontRendererObj.getStringWidth(name + " "), posY, width, 6, mouseX, mouseY)) {
            dragging = true;
            return true;
        }

        return false;
    }

    @Override
    public boolean mouseReleased(int mouseX, int mouseY, int mouseButton) {
        this.dragging = false;
        return false;
    }
}
