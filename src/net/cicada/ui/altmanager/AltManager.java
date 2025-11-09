package net.cicada.ui.altmanager;

import com.google.gson.*;
import net.cicada.Cicada;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.Session;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AltManager extends GuiScreen {
    public List<Account> accountList = new ArrayList<>();

    public AltManager() {
        try {
            JsonObject object = new JsonParser().parse(new FileReader(new File(Cicada.MAIN_DIR, "accounts.json"))).getAsJsonObject();
            if (object.has("Accounts")) {
                for (JsonElement element : object.get("Accounts").getAsJsonArray()) {
                    accountList.add(new Account(element.getAsString()));
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Accounts file not found");
        }
    }

    @Override
    public void initGui() {
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, this.width - 105, 5, 100, 20, "Add Account"));
        this.buttonList.add(new GuiButton(1, this.width - 105, 30, 100, 20, "Login"));
        this.buttonList.add(new GuiButton(2, this.width - 105, 55, 100, 20, "Direct Login"));
        this.buttonList.add(new GuiButton(3, this.width - 105, 80, 100, 20, "Delete"));
        this.buttonList.add(new GuiButton(4, this.width - 105, 105, 100, 20, "Back"));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        mc.fontRendererObj.drawStringWithShadow("Logged as: " + mc.getSession().getUsername(), 5, 5, 0xFFFFFF);
        float offsetY = 5;
        for (Account account : accountList) {
            account.setPosX(this.width / 2F - 100);
            account.setPosY(offsetY);
            account.draw(mouseX, mouseY, partialTicks);
            offsetY += account.getHeight() + 5;
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 0) {
            mc.displayGuiScreen(new AddAccount(false));
        } else if (button.id == 1) {
            for (Account account : accountList) {
                if (account.isSelected()) {
                    mc.setSession(new Session(account.getUsername(), account.getUuid(), "accessToken", "mojang"));
                }
            }
        } else if (button.id == 2) {
            mc.displayGuiScreen(new AddAccount(true));
        } else if (button.id == 3) {
            Account delAcc = null;
            for (Account account : this.accountList) {
                if (account.isSelected()) {
                    delAcc = account;
                }
            }
            this.accountList.remove(delAcc);
        } else if (button.id == 4) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonObject jsonObject = new JsonObject();
            JsonArray accountsArray = new JsonArray();
            for (Account account : this.accountList) {
                accountsArray.add(new JsonPrimitive(account.getUsername()));
            }
            jsonObject.add("Accounts", accountsArray);
            try (PrintWriter printWriter = new PrintWriter(new File(Cicada.MAIN_DIR, "accounts.json"))) {
                printWriter.println(gson.toJson(jsonObject));
            } catch (IOException e) {
                e.printStackTrace();
            }
            mc.displayGuiScreen(new GuiMultiplayer());
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        for (Account account : accountList) {
            if (account.mouseOver(mouseX, mouseY)) {
                if (mouseButton == 0) {
                    for (Account acc : accountList) {
                        acc.setSelected(false);
                    }
                    account.setSelected(true);
                    return;
                }
            } else {
                for (Account acc : accountList) {
                    acc.setSelected(false);
                }
            }
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
    }
}
