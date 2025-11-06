package net.cicada.module.setting.impl;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.MathHelper;
import net.cicada.module.api.Module;
import net.cicada.module.setting.Setting;
import net.cicada.utility.DeltaTracker;
import net.cicada.utility.GuiUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.function.BooleanSupplier;

@Getter @Setter
public class NumberSetting extends Setting {
    private double value, minValue, maxValue, step;
    private boolean dragging;

    public NumberSetting(String name, double value, double minValue, double maxValue, double step, BooleanSupplier visible, Module module) {
        super(name, visible, module);
        this.width = mc.fontRendererObj.getStringWidth(this.name + ": ") + 100;
        this.value = MathHelper.round(value, step);
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.step = step;
    }

    public void setValue(double value) {
        this.value = MathHelper.round(value, this.step);
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        if (GuiUtil.mouseOver(this.posX, this.posY, this.width, this.height, mouseX, mouseY)) {
            this.setValue(MathHelper.clamp_double(this.getValue() + DeltaTracker.deltaScroll / 120 * this.step, this.minValue, this.maxValue));
        }
        if (this.dragging) this.setValue(MathHelper.clamp_double(MathHelper.map(mouseX, this.posX + mc.fontRendererObj.getStringWidth(this.name + ": "), this.posX + this.width, this.minValue, this.maxValue), this.minValue, this.maxValue));

        mc.fontRendererObj.drawStringWithShadow(this.getName() + ":", this.posX, this.posY + 2, 0xFFFFFFFF);
        Gui.drawRect(this.posX + mc.fontRendererObj.getStringWidth(this.name + ": "), this.posY + 2,
                (float) MathHelper.map(this.value, this.minValue, this.maxValue, this.posX + mc.fontRendererObj.getStringWidth(this.name + ": "), this.posX + this.width), this.posY + this.height - 2, 0xFF808080);
        mc.fontRendererObj.drawStringWithShadow(String.valueOf(this.value), this.posX + mc.fontRendererObj.getStringWidth(this.name + ": ") + 50, this.posY + 2, 0xFFFFFFFF);
    }

    @Override
    public void mousePressed(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0) {
            if (GuiUtil.mouseOver(this.posX + mc.fontRendererObj.getStringWidth(this.name + ": "), this.posY + 2, this.width, this.height - 2, mouseX, mouseY)) {
                this.dragging = true;
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        this.dragging = false;
    }
}
