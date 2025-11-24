package net.cicada.ui.zamorozkaClickGui;

import lombok.Getter;
import lombok.Setter;
import net.cicada.module.api.Category;
import net.cicada.module.api.Module;
import net.cicada.module.api.ModuleManager;
import net.cicada.ui.ComponentGui;
import net.cicada.utility.Render.RenderUtil;
import net.cicada.utility.Render.font.FontRenderer;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class Panel extends ComponentGui {
    private Category category;
    private List<ModuleButton> moduleButtons = new ArrayList<>();
    private boolean isSelected;

    public Panel(Category category) {
        this.width = mc.fontRendererObj.getStringWidth("MurderHighlighter +");
        this.height = 25;
        this.category = category;
        for (Module module : ModuleManager.MODULES) {
            if (!module.getCategory().equals(this.category)) continue;
            moduleButtons.add(new ModuleButton(module));
        }
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        RenderUtil.setGlColor(new Color(40, 40, 40, 255));
        RenderUtil.render2DRect(this.posX, this.posY, this.width,  this.isSelected ? (this.height + 5) * 8 - 5 : this.height);
        mc.fontRendererObj.drawString(this.category.name(), this.posX + this.width / 2 - mc.fontRendererObj.getStringWidth(this.category.name()) / 2F, this.posY + this.height / 2 - mc.fontRendererObj.FONT_HEIGHT / 2F, 0xFFFFFFFF);
        if (isSelected) {
            float offsetY = this.posY + this.height;
            for (ModuleButton moduleButton : moduleButtons) {
                moduleButton.setPosX(this.posX);
                moduleButton.setPosY(offsetY);
                moduleButton.draw(mouseX, mouseY);
                offsetY += moduleButton.getHeight() + 3;
            }
        }
    }

    @Override
    public boolean mousePressed(int mouseX, int mouseY, int mouseButton) {
        if (!this.isSelected) return false;
        for (ModuleButton moduleButton : moduleButtons) {
            if (moduleButton.mouseOver(mouseX, mouseY)) {
                if  (mouseButton == 1) {
                    for (ModuleButton MB : moduleButtons) {
                        MB.setOpenSettings(MB.equals(moduleButton) && !MB.isOpenSettings());
                    }
                    return true;
                }
            }

            if (moduleButton.mousePressed(mouseX, mouseY, mouseButton)) return true;
        }
        return false;
    }

    @Override
    public boolean mouseReleased(int mouseX, int mouseY, int mouseButton) {
        if (!this.isSelected) return false;
        for (ModuleButton moduleButton : moduleButtons) {
            if (moduleButton.mouseReleased(mouseX, mouseY, mouseButton)) return true;
        }
        return false;
    }
}
