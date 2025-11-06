package net.cicada.module.setting;

import lombok.Getter;
import lombok.Setter;
import net.cicada.module.api.Module;
import net.cicada.utility.Access;

import java.util.function.BooleanSupplier;

@Getter @Setter
public abstract class Setting implements Access {
    protected String name;
    protected BooleanSupplier visible;
    protected float posX, posY;
    protected float width = 100, height = 15;

    public Setting(String name, BooleanSupplier visible, Module module) {
        this.name = name;
        this.visible = visible;
        module.getSettings().add(this);
    }

    public abstract void draw(int mouseX, int mouseY);
    public abstract void mousePressed(int mouseX, int mouseY, int mouseButton);
    public abstract void mouseReleased(int mouseX, int mouseY, int mouseButton);
}
