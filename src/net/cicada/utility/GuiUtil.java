package net.cicada.utility;

public class GuiUtil implements Access {
    public static boolean mouseOver(float posX, float posY, float width, float height, float mouseX, float mouseY) {
        return mouseX > posX && mouseX < posX + width && mouseY > posY && mouseY < posY + height;
    }
}
