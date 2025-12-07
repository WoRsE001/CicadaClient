package net.cicada.gui.zamorozkaClickGui;

import lombok.Getter;
import lombok.Setter;
import net.cicada.module.api.Category;
import net.cicada.module.api.ModuleManager;
import net.cicada.module.impl.render.ClickGui;
import net.cicada.utility.Render.RenderUtil;
import net.cicada.utility.Render.font.FontRenderer;
import net.cicada.utility.Render.font.Fonts;
import net.minecraft.client.gui.GuiScreen;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class ZamorozkaClickGui extends GuiScreen {
    public static ZamorozkaClickGui INSTANCE ;
    public static FontRenderer font = Fonts.Minecraft.get(20);
    private static List<Panel> panels = new ArrayList<>();

    public static void init() {
        INSTANCE = new ZamorozkaClickGui();
        for (Category category : Category.values()) {
            panels.add(new Panel(category));
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (ClickGui.ImagesLoc.get(ModuleManager.CLICK_GUI.image.getValue()) != null)
            RenderUtil.drawImage(ClickGui.ImagesLoc.get(ModuleManager.CLICK_GUI.image.getValue()), this.width - this.width / 4, this.height - this.height / 5 * 3, this.width / 4, this.height / 5 * 3);
        RenderUtil.setGlColor(new Color(0, 0, 0, 40));
        RenderUtil.drawRect(0, 0, mc.displayWidth, mc.displayHeight);
        float offsetY = mc.displayHeight / 4F - (panels.getFirst().getHeight() + 5) * panels.size() / 2;
        for (Panel panel : panels) {
            if (!panel.isSelected()) {
                panel.setPosX(10);
                panel.setPosY(offsetY);
                panel.draw(mouseX, mouseY);
                offsetY += panel.getHeight() + 5;
            } else {
                panel.setPosX(panel.width + 20);
                panel.setPosY(mc.displayHeight / 4F - (panels.getFirst().getHeight() + 5) * panels.size() / 2);
                panel.draw(mouseX, mouseY);
            }
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        for (Panel panel : panels) {
            if (panel.keyPressed(typedChar, keyCode)) return;
        }

        super.keyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        for (Panel panel : panels) {
            if (mouseButton == 0 && panel.mouseOver(mouseX, mouseY)) {
                for (Panel p : panels) {
                    p.setSelected(p.equals(panel) && !p.isSelected());
                }
                return;
            }
            if (panel.mousePressed(mouseX, mouseY, mouseButton)) return;
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        for (Panel panel : panels) {
            if (panel.mouseReleased(mouseX, mouseY, state)) return;
        }
    }
}
