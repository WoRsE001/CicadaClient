package net.cicada.ui.windowClickGui;

import lombok.Getter;
import lombok.Setter;
import net.cicada.module.api.Category;
import net.cicada.module.api.Module;
import net.cicada.module.api.ModuleManager;
import net.cicada.ui.ComponentGui;
import net.cicada.utility.Render.font.FontRenderer;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class Panel extends ComponentGui {
    private Category category;
    private List<ModuleButton> moduleButtons = new ArrayList<>();
    private boolean selected = false;

    public Panel(Category category) {
        this.width = 60;
        this.height = 10;
        this.category = category;
        for (Module module : ModuleManager.MODULES) {
            if (module.getCategory().equals(category)) {
                this.moduleButtons.add(new ModuleButton(module));
            }
        }
    }

    public void draw(int mouseX, int mouseY, float partialTicks, FontRenderer font, Window window) {
        font.drawStringWithShadow(this.category.name(), this.posX, this.posY, 0xFFFFFFFF);
        if (this.selected) {
            float y = window.getPosY() + 10;
            for (ModuleButton moduleButton : this.moduleButtons) {
                moduleButton.setPosX(window.getPosX());
                moduleButton.setPosY(y);
                moduleButton.draw(mouseX, mouseY, partialTicks, font, window);
                y += moduleButton.getHeight();
            }
        }
    }

    @Override
    public void mousePressed(int mouseX, int mouseY, int mouseButton) {
        if (this.selected) {
            for (ModuleButton moduleButton : this.moduleButtons) {
                if (mouseButton == 1 && moduleButton.mouseOver(mouseX, mouseY)) {
                    for (ModuleButton MB : this.moduleButtons) {
                        MB.setOpenSettings(false);
                    }
                    moduleButton.setOpenSettings(true);
                    return;
                }
                moduleButton.mousePressed(mouseX, mouseY, mouseButton);
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        if (this.selected) {
            for (ModuleButton moduleButton : this.moduleButtons) {
                moduleButton.mouseReleased(mouseX, mouseY, mouseButton);
            }
        }
    }
}
