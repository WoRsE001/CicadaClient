package net.cicada.ui;

import lombok.Getter;
import lombok.Setter;
import net.cicada.utility.Access;

@Getter @Setter
public class ComponentGui implements Access {
    public float posX, posY, width, height;

    public boolean mouseOver(float mouseX, float mouseY) {
        return mouseX > this.posX && mouseX < this.posX + this.width && mouseY > this.posY && mouseY < this.posY + this.height;
    }

    public void draw(int mouseX, int mouseY, float partialTicks) {}
    public void mousePressed(int mouseX, int mouseY, int mouseButton) {}
    public void mouseReleased(int mouseX, int mouseY,int mouseButton) {}
}
