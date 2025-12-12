package net.cicada.module.setting.impl;

import lombok.Getter;
import lombok.Setter;
import net.cicada.module.api.Module;
import net.cicada.module.setting.Setting;
import net.cicada.utility.GuiUtil;

import java.awt.*;
import java.util.List;
import java.util.function.BooleanSupplier;

@Getter @Setter
public class ListSetting extends Setting {
    private List<String> list;
    private String value;
    private boolean isOpen;

    public ListSetting(String name, String value, List<String> list, BooleanSupplier visible, Module module) {
        super(name, 0 ,9, visible, module);
        this.value = list.contains(value) ? value : list.getFirst();
        this.list = list;
    }

    public boolean is(String str) {
        return this.value.equals(str);
    }

    public void next() {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(value)) {
                value = list.get((i + 1) % list.size());
                return;
            }
        }
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        mc.fontRendererObj.drawString(name + ": ", posX, posY, 0xFFFFFFFF);
        mc.fontRendererObj.drawString(value, posX + mc.fontRendererObj.getStringWidth(name + ": "), posY, 0xFFFFFFFF);
    }

    @Override
    public boolean mousePressed(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0 && GuiUtil.mouseOver(posX + mc.fontRendererObj.getStringWidth(name + ": "), posY, mc.fontRendererObj.getStringWidth(value), height, mouseX, mouseY)) {
            next();
            return true;
        }

        return false;
    }
}
