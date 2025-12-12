package net.cicada.module.setting.impl;

import lombok.Getter;
import lombok.Setter;
import net.cicada.module.api.Module;
import net.cicada.module.setting.Setting;
import net.cicada.utility.FixedColor;
import net.cicada.utility.GuiUtil;
import net.cicada.utility.MathUtil;
import net.cicada.utility.Render.RenderUtil;
import net.minecraft.util.MathHelper;

import java.awt.*;
import java.util.function.BooleanSupplier;

@Getter @Setter
public class ColorSetting extends Setting {
    FixedColor color;
    boolean opening = false, hueDragging, SBDragging, alphaDragging;

    public ColorSetting(String name, FixedColor color, BooleanSupplier visible, Module module) {
        super(name, 9, 9, visible, module);
        this.color = color;
    }

    @Override
    public float getWidth() {
        return opening ? 98 - mc.fontRendererObj.getStringWidth(name + " ") : width;
    }

    @Override
    public float getHeight() {
        return opening ? 54 : height;
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        if (hueDragging) {
            double hue = MathHelper.clamp_double(MathUtil.map(mouseX, posX, posX + 106.5, 0, 1), 0, 1);
            color.setHSBA(hue, color.getSaturation(), color.getBrightness(), color.getAlpha());
        }

        if (SBDragging) {
            double saturation = MathHelper.clamp_double(MathUtil.map(mouseX, posX + 72, posX + 106.5, 0, 1), 0, 1);
            double brightness = 1 - MathHelper.clamp_double(MathUtil.map(mouseY, posY + 11, posY + 45.5, 0, 1), 0, 1);
            color.setHSBA(color.getHue(), saturation, brightness, color.getAlpha());
        }

        if (alphaDragging) {
            double alpha = 1 - MathHelper.clamp_double(MathUtil.map(mouseY, posY + 11, posY + 45.5, 0, 1), 0, 1);
            color.setHSBA(color.getHue(), color.getSaturation(), color.getBrightness(), alpha);
        }

        mc.fontRendererObj.drawString(name, posX, posY, 0xFFFFFFFF);
        RenderUtil.setGlColor(color);
        RenderUtil.drawRoundRect(posX + mc.fontRendererObj.getStringWidth(name + " "), posY, width, height, 2);
        RenderUtil.setGlColor(new FixedColor(0, 0, 0, 1));
        RenderUtil.drawRoundRectOutline(posX + mc.fontRendererObj.getStringWidth(name + " "), posY, width, height, 2, 2);

        if (opening) {
            int r = (int) (color.getRed() * 255);
            int g = (int) (color.getGreen() * 255);
            int b = (int) (color.getBlue() * 255);
            int a = (int) (color.getAlpha() * 255);
            mc.fontRendererObj.drawString(String.format("#%02x%02x%02x", r, g, b).toUpperCase().replace("#", ""), posX, posY + 11, 0xFFFFFFFF);
            mc.fontRendererObj.drawString("Copy", posX + 6, posY + 22, 0xFFFFFFFF);
            mc.fontRendererObj.drawString("Paste", posX + 4, posY + 35, 0xFFFFFFFF);
            mc.fontRendererObj.drawString("R " + r, posX + 38, posY + 11, 0xFFFFFFFF);
            mc.fontRendererObj.drawString("G " + g, posX + 38, posY + 20, 0xFFFFFFFF);
            mc.fontRendererObj.drawString("B " + b, posX + 38, posY + 29, 0xFFFFFFFF);
            mc.fontRendererObj.drawString("A " + a, posX + 38, posY + 38, 0xFFFFFFFF);

            RenderUtil.drawGradient2(posX + 66, posY + 11, 4, 34.5F, color);
            float alphaOffset = (float) MathUtil.map((1 - color.getAlpha()) * 34.5, 0, 34.5, 2, 32.5F);
            RenderUtil.setGlColor(new FixedColor(color.getHue(), color.getSaturation(), color.getBrightness(), 1, true));
            RenderUtil.drawCircle(posX + 68, posY + 11 + alphaOffset, 2);

            RenderUtil.drawGradient1(posX + 72, posY + 11, 34.5F, 34.5F, color);
            float saturationOffset = (float) MathUtil.map(color.getSaturation() * 34.5, 0, 34.5, 3, 32);
            float brightnessOffset = (float) MathUtil.map((1 - color.getBrightness()) * 34.5, 0, 34.5, 3, 32);
            RenderUtil.setGlColor(new FixedColor(color.getHue(), color.getSaturation(), color.getBrightness(), 1, true));
            RenderUtil.drawCircle(posX + 72 + saturationOffset, posY + 11 + brightnessOffset, 3);

            RenderUtil.drawGradient(posX, posY + 49.5F, 106.5F, 2);
            RenderUtil.setGlColor(new FixedColor(color.getHue(), 1, 1, 1, true));
            float hueOffset = (float) MathUtil.map(color.getHue() * 106.5, 0, 106.5, 3, 104);
            RenderUtil.drawCircle(posX + hueOffset, posY + 50.5F, 3);
        }
    }

    @Override
    public boolean mousePressed(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0) {
            if (GuiUtil.mouseOver(posX + mc.fontRendererObj.getStringWidth(name + " "), posY, width, height, mouseX, mouseY)) {
                opening = !opening;
                return true;
            }

            if (opening) {
                if (GuiUtil.mouseOver(posX - 3, posY + 46, 106.5F, 6, mouseX, mouseY)) {
                    hueDragging = true;
                    return true;
                }

                if (GuiUtil.mouseOver(posX + 72, posY + 11, 34.5F, 34.5F, mouseX, mouseY)) {
                    SBDragging = true;
                    return true;
                }

                if (GuiUtil.mouseOver(posX + 66, posY + 11, 4, 34.5F, mouseX, mouseY)) {
                    alphaDragging = true;
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean mouseReleased(int mouseX, int mouseY, int mouseButton) {
        hueDragging = false;
        SBDragging = false;
        alphaDragging = false;
        return false;
    }
}