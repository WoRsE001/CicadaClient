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
        this.width = mc.fontRendererObj.getStringWidth(this.name + ": false");
        this.value = value;
    }

    public void toggle() {
        this.value = !this.value;
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        mc.fontRendererObj.drawStringWithShadow(this.name + ":", this.posX, this.posY + 2, 0xFFFFFFFF);
        mc.fontRendererObj.drawStringWithShadow(String.valueOf(this.value), this.posX + mc.fontRendererObj.getStringWidth(this.name + ": "), this.posY + 2, value ? 0xFF00FF00 : 0xFFFF0000);
    }

    @Override
    public boolean mousePressed(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0) {
            if (GuiUtil.mouseOver(this.posX + mc.fontRendererObj.getStringWidth(this.name + ": "), this.posY, this.width, this.height, mouseX, mouseY)) {
                this.toggle();
                return true;
            }
        }

        return false;
    }
}
