package net.cicada.ui.windowClickGui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;

import java.io.IOException;

public class WindowClickGui extends GuiScreen {
    ScaledResolution scaledResolution;
    private Window window;

    @Override
    public void initGui() {
        scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        window = new Window(scaledResolution.getScaledWidth() / 2F - 400, scaledResolution.getScaledHeight() / 2F - 200, 800, 400);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        window.draw(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        window.mousePressed(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        window.mouseReleased(mouseX, mouseY, state);
    }
}
