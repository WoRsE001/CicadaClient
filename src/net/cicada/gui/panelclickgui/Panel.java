package net.cicada.gui.panelclickgui;

import net.cicada.gui.ComponentGui;
import net.cicada.module.api.Category;
import net.cicada.module.api.Module;
import net.cicada.module.api.ModuleManager;
import net.cicada.utility.DeltaTracker;
import net.cicada.utility.GuiUtil;
import net.cicada.utility.Render.RenderUtil;
import net.cicada.utility.Render.StencilUtil;
import net.minecraft.util.MathHelper;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Panel extends ComponentGui {
    List<ModuleButton> moduleButtons = new ArrayList<>();
    Category category;
    float scroll;

    public Panel(float posX, float posY, Category category) {
        this.posX = posX;
        this.posY = posY;
        width = 100;
        height = 200;
        this.category = category;
        float offset = height + 3;
        for (Module module : ModuleManager.MODULES) {
            if (module.getCategory() != category) continue;
            moduleButtons.add(new ModuleButton(posX + 3, posY + offset, module));
            offset += 23;
        }
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        float contentHeight = 3;
        for (ModuleButton moduleButton : moduleButtons) {
            contentHeight += moduleButton.getHeight() + 3;
        }
        float viewableHeight = height - 20;
        if (contentHeight > viewableHeight) {
            if (GuiUtil.mouseOver(posX, posY + 20, width, height, mouseX, mouseY)) {
                scroll += DeltaTracker.deltaScroll / 20;
            }
            scroll = MathHelper.clamp_float(scroll, -(contentHeight - viewableHeight), 0);
        } else {
            scroll = 0;
        }

        StencilUtil.setUpTexture(posX, posY, width, height, 5);
        StencilUtil.writeTexture();
        RenderUtil.setGlColor(ModuleManager.CLICK_GUI.panelBGColor.getColor());
        RenderUtil.drawRect(posX, posY + 20, width, height);
        float offset = posY + 23 + scroll;
        for (ModuleButton moduleButton : moduleButtons) {
            moduleButton.setPosY(offset);
            moduleButton.draw(mouseX, mouseY);
            offset += 23;
        }
        RenderUtil.setGlColor(ModuleManager.CLICK_GUI.panelColor.getColor());
        RenderUtil.drawRect(posX, posY, width, 20);
        mc.fontRendererObj.drawString(category.name(), posX + width / 2 - mc.fontRendererObj.getStringWidth(category.name()) / 2F, posY + 10 - mc.fontRendererObj.FONT_HEIGHT / 2F, 0xFFFFFFFF);
        StencilUtil.endWriteTexture();
    }

    @Override
    public boolean mousePressed(int mouseX, int mouseY, int mouseButton) {
        if (GuiUtil.mouseOver(posX, posY + 20, width, height, mouseX, mouseY)) {
            for (ModuleButton moduleButton : moduleButtons) {
                if (moduleButton.mousePressed(mouseX, mouseY, mouseButton)) return true;
            }
        }
        return false;
    }
}