package net.cicada.ui.altmanager;

import lombok.Getter;
import lombok.Setter;
import net.cicada.ui.ComponentGui;
import net.cicada.utility.RenderUtil;

import java.awt.*;
import java.util.UUID;

@Getter @Setter
public class Account extends ComponentGui {
    private String username;
    private String uuid;
    private boolean isSelected;

    public Account(String username, String uuid) {
        this.width = 150;
        this.height = 25;
        this.username = username;
        this.uuid = uuid;
    }

    public Account(String username) {
        this.width = 150;
        this.height = 25;
        this.username = username;
        this.uuid = getUUID(username);
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        RenderUtil.setGlColor(new Color(0, 0, 0, 255));
        RenderUtil.render2DRect(this.posX, this.posY, this.width, this.height);
        mc.fontRendererObj.drawStringWithShadow(this.username, this.posX + this.width / 2 - mc.fontRendererObj.getStringWidth(this.username) / 2F, this.posY + this.height / 2 - 5, this.isSelected ? 0xFFFFFFFF : 0xFF808080);
    }

    public static String getUUID(String name) {
        String s = "OfflinePlayer:" + name;
        return UUID.nameUUIDFromBytes(s.getBytes()).toString().replace("-", "");
    }
}
