package net.cicada.ui.windowClickGui;

import lombok.Getter;
import lombok.Setter;
import net.cicada.module.api.Module;
import net.cicada.module.setting.Setting;
import net.cicada.ui.ComponentGui;

@Getter @Setter
public class ModuleButton extends ComponentGui {
    private Module module;
    private boolean openSettings = false;

    public ModuleButton(Module module) {
        this.width = 89;
        this.height = 10;
        this.module = module;
    }

    public void draw(int mouseX, int mouseY, float partialTicks, Window window) {
        mc.fontRendererObj.drawStringWithShadow(this.module.getName(), this.posX, this.posY, this.module.isState() ? 0xFFFFFFFF : 0xFF808080);
        if (this.isOpenSettings()) {
            float y = window.getPosY() + 10;
            for (Setting setting : this.module.getSettings()) {
                if (setting.getVisible().getAsBoolean()) {
                    setting.setPosX(window.getPosX() + 89);
                    setting.setPosY(y);
                    setting.draw(mouseX, mouseY);
                    y += setting.getHeight();
                }
            }
        }
    }

    @Override
    public void mousePressed(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0 && this.mouseOver(mouseX, mouseY)) {
            this.module.toggle();
            return;
        }
        if (this.openSettings) {
            for (Setting setting : this.module.getSettings()) {
                if (!setting.getVisible().getAsBoolean()) continue;
                setting.mousePressed(mouseX, mouseY, mouseButton);
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        if (this.openSettings) {
            for (Setting setting : this.module.getSettings()) {
                if (!setting.getVisible().getAsBoolean()) continue;
                setting.mouseReleased(mouseX, mouseY, mouseButton);
            }
        }
    }
}
