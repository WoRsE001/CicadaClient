package net.cicada.ui.windowClickGui;

import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;

public class WindowClickGui extends GuiScreen {
    public static WindowClickGui INSTANCE;
    private Window window;

    public static  void init() {
        INSTANCE = new WindowClickGui();
    }

    @Override
    public void initGui() {
        window = new Window(50, 50, mc.displayWidth / 2 - 100, mc.displayHeight / 2 - 100);
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
