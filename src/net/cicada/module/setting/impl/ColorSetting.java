package net.cicada.module.setting.impl;

import lombok.Getter;
import lombok.Setter;
import net.cicada.module.api.Module;
import net.cicada.module.setting.Setting;
import java.util.function.BooleanSupplier;

@Getter @Setter
public class ColorSetting extends Setting {
    public ColorSetting(String name, BooleanSupplier visible, Module module) {
        super(name, visible, module);
    }
}
