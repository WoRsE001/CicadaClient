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
    private Doubles<String, Boolean> curValue;

    public MultiBooleanSetting(String name, BooleanSupplier visible, Module module) {
        super(name, 100, 8, visible, module);
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

    public void next() {
        for (int i = 0; i < values.size(); i++) {
            if (curValue.equals(values.get(i))) {
                curValue = values.get((i + 1) % values.size());
                return;
            }
        }
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        if (curValue == null) curValue = values.getFirst();
        mc.fontRendererObj.drawString(name + ": ", posX, posY, 0xFFFFFFFF);
        mc.fontRendererObj.drawString(curValue.getT(), posX + mc.fontRendererObj.getStringWidth(name + ": "), posY, curValue.getE() ? 0xFFFFFFFF : 0xFF808080);
    }

    @Override
    public boolean mousePressed(int mouseX, int mouseY, int mouseButton) {
        if (curValue != null && GuiUtil.mouseOver(posX + mc.fontRendererObj.getStringWidth(name + ": "), posY, mc.fontRendererObj.getStringWidth(curValue.getT()), height, mouseX, mouseY)) {
            if (mouseButton == 0) {
                next();
                return true;
            } else if (mouseButton == 1) {
                for (Doubles<String, Boolean> value : values) {
                    if (value.equals(curValue)) {
                        value.setE(!value.getE());
                        return true;
                    }
                }
            }
        }

        return false;
    }
}
