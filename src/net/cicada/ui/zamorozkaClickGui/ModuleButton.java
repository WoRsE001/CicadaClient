package net.cicada.ui.zamorozkaClickGui;

import lombok.Getter;
import lombok.Setter;
import net.cicada.module.api.Module;
import net.cicada.module.setting.Setting;
import net.cicada.ui.ComponentGui;
import net.cicada.utility.Render.RenderUtil;
import net.cicada.utility.Render.font.FontRenderer;

import java.awt.*;

@Getter @Setter
public class ModuleButton extends ComponentGui {
    private Module module;
    private boolean isOpenSettings;

    public ModuleButton(Module module) {
        this.width = 110;
        this.height = 25;
        this.module = module;
    }

    public void draw(int mouseX, int mouseY, float partialTicks, FontRenderer font) {
        RenderUtil.setGlColor(new Color(40, 40, 40, 255));
        RenderUtil.render2DRect(this.posX, this.posY, this.width, this.height);
        font.drawCenteredStringWithShadow(this.module.getName(), this.posX + this.width / 2, this.posY + this.height / 2 - font.getHeight() / 4F, this.module.isState() ? 0xFFFFFFFF : 0xFF808080);
        if (this.isOpenSettings) {
            float w = 400;
            float h = mc.displayHeight / 2F - 50;
            RenderUtil.setGlColor(new Color(40, 40, 40, 255));
            RenderUtil.render2DRect(mc.displayWidth / 4F - w / 2F, mc.displayHeight / 4F - h / 2, w, h);
            float offsetY = mc.displayHeight / 4F - h / 2;
            for (Setting setting : this.module.getSettings()) {
                if (setting.getVisible().getAsBoolean()) {
                    setting.setPosX(mc.displayWidth / 4F - w / 2F + 2);
                    setting.setPosY(offsetY);
                    setting.draw(mouseX, mouseY);
                    offsetY += setting.getHeight();
                }
            }
        }
    }

    @Override
    public void mousePressed(int mouseX, int mouseY, int mouseButton) {
        if (this.isOpenSettings) {
            for (Setting setting : this.module.getSettings()) {
                if (setting.getVisible().getAsBoolean()) {
                    setting.mousePressed(mouseX, mouseY, mouseButton);
                }
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        if (this.isOpenSettings) {
            for (Setting setting : this.module.getSettings()) {
                if (setting.getVisible().getAsBoolean()) {
                    setting.mouseReleased(mouseX, mouseY, mouseButton);
                }
            }
        }
    }
}
