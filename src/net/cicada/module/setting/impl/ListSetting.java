package net.cicada.module.setting.impl;

import lombok.Getter;
import lombok.Setter;
import net.cicada.module.api.Module;
import net.cicada.module.setting.Setting;
import net.cicada.utility.GuiUtil;

import java.util.List;
import java.util.function.BooleanSupplier;

@Getter @Setter
public class ListSetting extends Setting {
    private String value;
    private List<String> list;

    public ListSetting(String name, String value, List<String> list, BooleanSupplier visible, Module module) {
        super(name, visible, module);
        this.width = mc.fontRendererObj.getStringWidth(this.name + ": ");
        this.value = list.contains(value) ? value : list.get(0);
        this.list = list;
        for (String s : this.list) {
            this.width +=  mc.fontRendererObj.getStringWidth(s + " ");
        }
    }

    public boolean is(String str) {
        return this.value.equals(str);
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        mc.fontRendererObj.drawString(this.name + ":", this.posX, this.posY + 2, 0xFFFFFFFF);
        float offsetX = this.posX + mc.fontRendererObj.getStringWidth(this.name + ": ");
        for (String str : this.list) {
            mc.fontRendererObj.drawString(str, offsetX, this.posY + 2, this.value.equals(str) ? 0xFFFFFFFF : 0xFF808080);
            offsetX += mc.fontRendererObj.getStringWidth(str + " ");
        }
    }

    @Override
    public boolean mousePressed(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0) {
            float offsetX = this.posX + mc.fontRendererObj.getStringWidth(this.name + ": ");
            for (String str : this.list) {
                if (GuiUtil.mouseOver(offsetX, this.posY, mc.fontRendererObj.getStringWidth(str + " "), this.height, mouseX, mouseY)) {
                    this.setValue(str);
                    return true;
                }
                offsetX += mc.fontRendererObj.getStringWidth(str + " ");
            }
        }

        return false;
    }
}
