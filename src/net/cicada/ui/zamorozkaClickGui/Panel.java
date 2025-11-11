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
        this.width = 110;
        this.height = 25;
        this.category = category;
        for (Module module : ModuleManager.MODULES) {
            if (!module.getCategory().equals(this.category)) continue;
            moduleButtons.add(new ModuleButton(module));
        }
    }

    public void draw(int mouseX, int mouseY, float partialTicks, FontRenderer font) {
        RenderUtil.setGlColor(new Color(40, 40, 40, 255));
        RenderUtil.render2DRect(this.posX, this.posY, this.width, this.height);
        font.drawCenteredStringWithShadow(this.category.name(), this.posX + this.width / 2, this.posY + this.height / 2 - font.getHeight() / 4F, 0xFFFFFFFF);
        if (isSelected) {
            float offsetY = mc.displayHeight / 4 - (moduleButtons.getFirst().getHeight() + 10) * moduleButtons.size() / 2;
            for (ModuleButton moduleButton : moduleButtons) {
                moduleButton.setPosX(this.width + 20);
                moduleButton.setPosY(offsetY);
                moduleButton.draw(mouseX, mouseY, partialTicks, font);
                offsetY += moduleButton.getHeight() + 10;
            }
        }
    }

    @Override
    public void mousePressed(int mouseX, int mouseY, int mouseButton) {
        if (!this.isSelected) return;
        for (ModuleButton moduleButton : moduleButtons) {
            if (moduleButton.mouseOver(mouseX, mouseY)) {
                if (mouseButton == 0) {
                    moduleButton.getModule().toggle();
                    return;
                } else if  (mouseButton == 1) {
                    for (ModuleButton MB : moduleButtons) {
                        MB.setOpenSettings(MB.equals(moduleButton) && !MB.isOpenSettings());
                    }
                    return;
                }
            }
            moduleButton.mousePressed(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        if (!this.isSelected) return;
        for (ModuleButton moduleButton : moduleButtons) {
            moduleButton.mouseReleased(mouseX, mouseY, mouseButton);
        }
    }
}
