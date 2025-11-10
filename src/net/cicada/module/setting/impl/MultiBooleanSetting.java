package net.cicada.module.setting.impl;

import lombok.Getter;
import lombok.Setter;
import net.cicada.module.api.Module;
import net.cicada.module.setting.Setting;
import net.cicada.utility.Doubles;
import net.cicada.utility.GuiUtil;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.BooleanSupplier;

@Getter @Setter
public class MultiBooleanSetting extends Setting {
    private CopyOnWriteArrayList<Doubles<String, Boolean>> values = new CopyOnWriteArrayList<>();

    public MultiBooleanSetting(String name, BooleanSupplier visible, Module module) {
        super(name, visible, module);
        this.width = mc.fontRendererObj.getStringWidth(this.name + ": ");
    }

    public MultiBooleanSetting add(String name, boolean value) {
        this.values.add(new Doubles<>(name, value));
        return this;
    }

    public boolean is(String str) {
        for (Doubles<String, Boolean> value : this.values) {
            if (value.getT().equals(str)) return value.getE();
        }
        return false;
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        mc.fontRendererObj.drawString(this.name + ":", this.posX, this.posY + 2, 0xFFFFFFFF);
        float offsetX = this.posX + mc.fontRendererObj.getStringWidth(this.name + ": ");
        for (Doubles<String, Boolean> value : this.values) {
            mc.fontRendererObj.drawString(value.getT(), offsetX, this.posY + 2, value.getE() ? 0xFFFFFFFF : 0xFF808080);
            offsetX += mc.fontRendererObj.getStringWidth(value.getT() + " ");
        }
    }

    @Override
    public void mousePressed(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0) {
            float offsetX = this.posX + mc.fontRendererObj.getStringWidth(this.name + ": ");
            for (Doubles<String, Boolean> value : this.values) {
                if (GuiUtil.mouseOver(offsetX, this.posY, mc.fontRendererObj.getStringWidth(value.getT() + " "), this.height, mouseX, mouseY)) value.setE(!value.getE());
                offsetX += mc.fontRendererObj.getStringWidth(value.getT() + " ");
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
    }
}
