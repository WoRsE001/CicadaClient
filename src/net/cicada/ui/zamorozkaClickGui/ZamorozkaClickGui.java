package net.cicada.ui.zamorozkaClickGui;

import lombok.Getter;
import lombok.Setter;
import net.cicada.module.api.Category;
import net.cicada.utility.RenderUtil;
import net.minecraft.client.gui.GuiScreen;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class ZamorozkaClickGui extends GuiScreen {
    private List<Panel> panels = new ArrayList<>();

    @Override
    public void initGui() {
        for (Category category : Category.values()) {
            panels.add(new Panel(category));
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        RenderUtil.setGlColor(new Color(128, 128, 255, 30));
        RenderUtil.render2DRect(0, 0, mc.displayWidth, mc.displayHeight);
        float offsetY = mc.displayHeight / 4F - (panels.getFirst().getHeight() + 10) * panels.size() / 2;
        for (Panel panel : this.panels) {
            panel.setPosX(10);
            panel.setPosY(offsetY);
            panel.draw(mouseX, mouseY, partialTicks);
            offsetY += panel.getHeight() + 10;
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        for (Panel panel : this.panels) {
            if (mouseButton == 0 && panel.mouseOver(mouseX, mouseY)) {
                for (Panel p : this.panels) {
                    p.setSelected(false);
                }
                panel.setSelected(true);
                return;
            }
            panel.mousePressed(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        for (Panel panel : this.panels) {
            panel.mouseReleased(mouseX, mouseY, state);
        }
    }
}
