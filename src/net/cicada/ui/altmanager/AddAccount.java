package net.cicada.ui.altmanager;

import net.cicada.Cicada;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.Session;

import java.io.IOException;

public class AddAccount extends GuiScreen {
    private GuiTextField username;
    private boolean isDirect;

    public AddAccount(boolean isDirect) {
        this.isDirect = isDirect;
    }

    @Override
    public void initGui() {
        this.buttonList.clear();
        this.username = new GuiTextField(0, mc.fontRendererObj, this.width / 2 - 50, this.height / 2 - 40, 100, 20);
        this.username.setFocused(true);
        this.buttonList.add(new GuiButton(0, this.width / 2 - 50, this.height / 2 - 10, 100, 20, "Add"));
        this.buttonList.add(new GuiButton(1, this.width / 2 - 50, this.height / 2 + 20, 100, 20, "Back"));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.username.drawTextBox();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        this.username.textboxKeyTyped(typedChar, keyCode);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 0) {
            if (!this.isDirect) Cicada.ALT_MANAGER.accountList.add(new Account(username.getText(), Account.getUUID(username.getText())));
            else mc.setSession(new Session(username.getText(), Account.getUUID(username.getText()), "accessToken", "mojang"));
            mc.displayGuiScreen(Cicada.ALT_MANAGER);
        } else if (button.id == 1) {
            mc.displayGuiScreen(Cicada.ALT_MANAGER);
        }
    }
}
