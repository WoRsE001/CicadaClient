package net.cicada.ui.windowClickGui;

import lombok.Getter;
import lombok.Setter;
import net.cicada.utility.RenderUtil;
import net.minecraft.client.gui.Gui;
import net.cicada.module.api.Category;
import net.cicada.ui.ComponentGui;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class Window extends ComponentGui {
    private List<Panel> panels = new ArrayList<>();

    public Window(float posX, float posY, float width, float height) {
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
        for (Category category : Category.values()) {
            panels.add(new Panel(category));
        }
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        RenderUtil.setGlColor(new  Color(0, 0, 0, 128));
        RenderUtil.render2DRect(this.posX, this.posY, this.width, this.height);

        float x = this.posX + 89;
        for (Panel panel : this.panels) {
            panel.posX = x;
            panel.posY = this.posY;
            panel.draw(mouseX, mouseY, partialTicks, this);
            x += panel.width;
        }
    }

    @Override
    public void mousePressed(int mouseX, int mouseY, int mouseButton) {
        for (Panel panel : this.panels) {
            if (mouseButton == 0 && panel.mouseOver(mouseX, mouseY)) {
                for (Panel p : panels) {
                    p.setSelected(false);
                }
                panel.setSelected(true);
                return;
            }
            panel.mousePressed(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        for (Panel panel : this.panels) {
            panel.mouseReleased(mouseX, mouseY, mouseButton);
        }
    }
}
