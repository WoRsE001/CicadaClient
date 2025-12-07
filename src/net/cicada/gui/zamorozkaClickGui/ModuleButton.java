package net.cicada.gui.zamorozkaClickGui;

import lombok.Getter;
import lombok.Setter;
import net.cicada.module.api.Module;
import net.cicada.module.setting.Setting;
import net.cicada.gui.ComponentGui;
import net.cicada.utility.Render.RenderUtil;

import java.awt.*;

@Getter @Setter
public class ModuleButton extends ComponentGui {
    private Module module;
    private boolean isOpenSettings;
    private boolean isListen;

    public ModuleButton(Module module) {
        this.width = mc.fontRendererObj.getStringWidth("MurderHighlighter  ");
        this.height = mc.fontRendererObj.FONT_HEIGHT;
        this.module = module;
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        mc.fontRendererObj.drawString(this.module.getName(), this.posX, this.posY, this.module.isState() ? 0xFFFFFFFF : 0xFF808080);
        mc.fontRendererObj.drawString(this.isOpenSettings ? "-" : "+", this.posX + mc.fontRendererObj.getStringWidth("MurderHighlighter "), this.posY, 0xFFFFFFFF);
        if (this.isOpenSettings) {
            float w = 400;
            float h = mc.displayHeight / 2F - 50;
            RenderUtil.setGlColor(new Color(40, 40, 40));
            RenderUtil.drawRoundRect(mc.displayWidth / 4F - w / 2F, mc.displayHeight / 4F - h / 2, w, h, 5);
            float offsetY = mc.displayHeight / 4F - h / 2 + (this.module.getSettings().isEmpty() ? 0 : this.module.getSettings().size() * this.module.getSettings().getFirst().getHeight());
            for (Setting setting : this.module.getSettings().reversed()) {
                if (setting.getVisible().getAsBoolean()) {
                    setting.setPosX(mc.displayWidth / 4F - w / 2F + 2);
                    setting.setPosY(offsetY);
                    setting.draw(mouseX, mouseY);
                    offsetY -= setting.getHeight();
                }
            }
        }
    }

    @Override
    public boolean keyPressed(char typedChar, int keyCode) {
        if (this.isListen) {
            this.module.setKey(keyCode);
            this.isListen = false;
            return true;
        }

        return false;
    }

    @Override
    public boolean mousePressed(int mouseX, int mouseY, int mouseButton) {
        if (this.mouseOver(mouseX, mouseY)) {
            if (mouseButton == 0) {
                this.module.toggle();
                return true;
            } else if (mouseButton == 2) {
                this.isListen = !this.isListen;
                return true;
            }
        }

        if (this.isOpenSettings) {
            for (Setting setting : this.module.getSettings()) {
                if (!setting.getVisible().getAsBoolean()) continue;
                if (setting.mousePressed(mouseX, mouseY, mouseButton)) return true;
            }
        }

        return false;
    }

    @Override
    public boolean mouseReleased(int mouseX, int mouseY, int mouseButton) {
        if (this.isOpenSettings) {
            for (Setting setting : this.module.getSettings()) {
                if (!setting.getVisible().getAsBoolean()) continue;
                if (setting.mouseReleased(mouseX, mouseY, mouseButton)) return true;
            }
        }

        return false;
    }
}
