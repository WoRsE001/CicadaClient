package net.cicada.gui.altmanager;

import net.cicada.Cicada;
import net.cicada.utility.Account;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.Session;

import java.io.IOException;

public class AddAccount extends GuiScreen {
    private GuiTextField username;
    private final boolean isDirect;

    public AddAccount(boolean isDirect) {
        this.isDirect = isDirect;
    }

    @Override
    public void initGui() {
        this.buttonList.clear();
        this.username = new GuiTextField(0, mc.fontRendererObj, this.width / 2 - 90, this.height / 2 - 35, 180, 20);
        this.username.setFocused(true);
        this.buttonList.add(new GuiButton(0, this.width / 2 - 90, this.height / 2 - 10, 180, 20, this.isDirect ? "Login" : "Add"));
        this.buttonList.add(new GuiButton(1, this.width / 2 - 90, this.height / 2 + 15, 180, 20, "Back"));
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
            if (!this.isDirect) {
                Cicada.ALT_MANAGER.accountList.add(new Account(username.getText()));
            } else {
                Account account = new Account(username.getText());
                mc.setSession(new Session(account.getUsername(), account.getUuid(), account.getAccessToken(), account.getAccessToken().equals("0") ? "legacy" : "mojang"));
            }
            mc.displayGuiScreen(Cicada.ALT_MANAGER);
        } else if (button.id == 1) {
            mc.displayGuiScreen(Cicada.ALT_MANAGER);
        }
    }
}
