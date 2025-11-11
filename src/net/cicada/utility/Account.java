package net.cicada.utility;

import lombok.Getter;
import lombok.Setter;
import net.cicada.ui.ComponentGui;
import net.cicada.utility.Render.RenderUtil;

import java.awt.*;
import java.util.UUID;

@Getter @Setter
public class Account extends ComponentGui {
    private String username;
    private String uuid;
    private String accessToken;
    private boolean isSelected;

    public Account(String username) {
        this.width = 150;
        this.height = 25;
        this.username = username;
        this.uuid = UUID.nameUUIDFromBytes(("OfflinePlayer:" + this.username).getBytes()).toString().replace("-", "");
        this.accessToken = "0";
    }

    public Account(String username, String uuid,  String accessToken) {
        this.width = 150;
        this.height = 25;
        this.username = username;
        this.uuid = uuid;
        this.accessToken = accessToken;
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        RenderUtil.setGlColor(new Color(0, 0, 0, 255));
        RenderUtil.render2DRect(this.posX, this.posY, this.width, this.height);
        mc.fontRendererObj.drawStringWithShadow(this.username, this.posX + this.width / 2 - mc.fontRendererObj.getStringWidth(this.username) / 2F, this.posY + this.height / 2 - 5, this.isSelected ? 0xFFFFFFFF : 0xFF808080);
    }
}
