package net.cicada.module.setting;

import lombok.Getter;
import lombok.Setter;
import net.cicada.module.api.Module;
import net.cicada.gui.ComponentGui;
import net.cicada.utility.Access;

import java.util.function.BooleanSupplier;

@Getter @Setter
public abstract class Setting extends ComponentGui implements Access {
    protected String name;
    protected BooleanSupplier visible;

    public Setting(String name, float width, float height, BooleanSupplier visible, Module module) {
        this.name = name;
        this.width = width;
        this.height = height;
        this.visible = visible;
        module.getSettings().add(this);
    }
}
