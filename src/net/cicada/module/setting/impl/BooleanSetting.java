package net.cicada.module.setting.impl;

import lombok.Getter;
import lombok.Setter;
import net.cicada.module.api.Module;
import net.cicada.module.setting.Setting;
import net.cicada.utility.GuiUtil;

import java.util.function.BooleanSupplier;

@Getter @Setter
public class BooleanSetting extends Setting {
    private boolean value;

    public BooleanSetting(String name, boolean value, BooleanSupplier visible, Module module) {
        super(name, visible, module);
        this.value = value;
    }

    public void toggle() {
        this.value = !this.value;
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        mc.fontRendererObj.drawStringWithShadow(this.name, this.posX, this.posY + mc.fontRendererObj.FONT_HEIGHT / 2F, 0xFFFFFFFF);
        mc.fontRendererObj.drawStringWithShadow(String.valueOf(this.value), mc.displayWidth / 4F + 200 - mc.fontRendererObj.getStringWidth(String.valueOf(value)), this.posY + mc.fontRendererObj.FONT_HEIGHT / 2F, value ? 0xFF00FF00 : 0xFFFF0000);
    }

    @Override
    public boolean mousePressed(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0) {
            if (GuiUtil.mouseOver(mc.displayWidth / 4F + 200 - mc.fontRendererObj.getStringWidth(String.valueOf(value)), this.posY, mc.fontRendererObj.getStringWidth(String.valueOf(value)), this.height, mouseX, mouseY)) {
                this.toggle();
                return true;
            }
        }

        return false;
    }
}
