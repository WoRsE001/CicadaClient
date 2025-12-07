package net.cicada.module.setting.impl;

import lombok.Getter;
import lombok.Setter;
import net.cicada.module.api.Module;
import net.cicada.module.setting.Setting;
import net.cicada.utility.GuiUtil;
import net.cicada.utility.Render.RenderUtil;

import java.awt.*;
import java.util.Comparator;
import java.util.List;
import java.util.function.BooleanSupplier;

@Getter @Setter
public class ListSetting extends Setting {
    private List<String> list;
    private String value;
    private boolean isOpen;

    public ListSetting(String name, String value, List<String> list, BooleanSupplier visible, Module module) {
        super(name, visible, module);
        this.value = list.contains(value) ? value : list.getFirst();
        this.list = list;
    }

    public boolean is(String str) {
        return this.value.equals(str);
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        int maxSize = mc.fontRendererObj.getStringWidth(list.stream().sorted(Comparator.comparingInt(str -> mc.fontRendererObj.getStringWidth(str))).toList().getLast());
        int maxHeight = mc.fontRendererObj.FONT_HEIGHT * list.size();
        RenderUtil.setGlColor(new Color(89, 80, 89));
        RenderUtil.drawRoundRect(mc.displayWidth / 4F + 200 - maxSize - 4, posY - 1, maxSize + 4, isOpen ? maxHeight : height - 2, 5);
        mc.fontRendererObj.drawString(value, mc.displayWidth / 4F + 200 - maxSize - 2, this.posY, 0xFFFFFFFF);
        mc.fontRendererObj.drawString(this.name, this.posX, this.posY + mc.fontRendererObj.FONT_HEIGHT / 2F, 0xFFFFFFFF);
        if (isOpen) {
            float offsetY = mc.fontRendererObj.FONT_HEIGHT;
            for (String str : list) {
                if (str.equals(value)) continue;
                mc.fontRendererObj.drawString(str, mc.displayWidth / 4F + 200 - maxSize - 2, this.posY + offsetY, 0xFF808080);
                offsetY += mc.fontRendererObj.FONT_HEIGHT;
            }
        }
    }

    @Override
    public boolean mousePressed(int mouseX, int mouseY, int mouseButton) {
        int maxSize = mc.fontRendererObj.getStringWidth(list.stream().sorted(Comparator.comparingInt(str -> mc.fontRendererObj.getStringWidth(str))).toList().getLast());
        if (mouseButton == 0) {
            if (GuiUtil.mouseOver(mc.displayWidth / 4F + 200 - maxSize - 2, posY, maxSize, mc.fontRendererObj.FONT_HEIGHT, mouseX, mouseY)) {
                isOpen = !isOpen;
                return true;
            }

            if (isOpen) {
                float offsetY = mc.fontRendererObj.FONT_HEIGHT;
                for (String str : list) {
                    if (str.equals(value)) continue;
                    if (GuiUtil.mouseOver(mc.displayWidth / 4F + 200 - maxSize - 2, posY + offsetY, maxSize, mc.fontRendererObj.FONT_HEIGHT, mouseX, mouseY)) {
                        value = str;
                        isOpen = false;
                    }
                    offsetY += mc.fontRendererObj.FONT_HEIGHT;
                }
            }
        }

        return false;
    }
}
