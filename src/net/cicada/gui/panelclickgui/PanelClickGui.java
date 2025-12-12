package net.cicada.gui.panelclickgui;

import net.cicada.module.api.Category;
import net.cicada.module.api.ModuleManager;
import net.cicada.utility.GuiUtil;
import net.cicada.utility.Render.RenderUtil;
import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PanelClickGui extends GuiScreen {
    List<Panel> panels = new ArrayList<>();
    List<SettingWindow> settingWindows = new ArrayList<>();

    @Override
    public void initGui() {
        panels.clear();
        float offset = 380;
        for (Category value : Category.values()) {
            panels.add(new Panel(width / 2F - offset, height / 2F - 110, value));
            offset -= 110;
        }
        settingWindows.clear();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        RenderUtil.setGlColor(ModuleManager.CLICK_GUI.BGColor.getColor());
        RenderUtil.drawRect(0, 0, width, height);

        for (Panel panel : panels) {
            panel.draw(mouseX, mouseY);
        }

        for (SettingWindow settingWindow : settingWindows) {
            settingWindow.draw(mouseX, mouseY);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        settingWindows.removeIf(settingWindow -> GuiUtil.mouseOver(settingWindow.getPosX() + settingWindow.getWidth() - 15, settingWindow.getPosY() + 5, 10, 10, mouseX, mouseY));

        for (SettingWindow settingWindow : settingWindows) {
            if (settingWindow.mousePressed(mouseX, mouseY, mouseButton)) return;
        }

        for (Panel panel : panels) {
            if (panel.mousePressed(mouseX, mouseY, mouseButton)) return;
        }

        if (mouseButton == 1) {
            for (Panel panel : panels) {
                for (ModuleButton moduleButton : panel.moduleButtons) {
                    if (moduleButton.mouseOver(mouseX, mouseY)) {
                        for (SettingWindow settingWindow : settingWindows) {
                            if (settingWindow.getModule().equals(moduleButton.getModule())) {
                                return;
                            }
                        }
                        settingWindows.add(new SettingWindow(width / 2F - 75, height / 2F - 100, moduleButton.module));
                        return;
                    }
                }
            }
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        for (SettingWindow settingWindow : settingWindows) {
            if (settingWindow.mouseReleased(mouseX, mouseY, state)) return;
        }
    }
}
