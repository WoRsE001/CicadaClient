package net.cicada.module.impl.render;

import net.cicada.utility.Render.RenderUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Vec3;
import net.cicada.event.api.Event;
import net.cicada.event.impl.Render2DEvent;
import net.cicada.module.api.Category;
import net.cicada.module.api.Module;
import net.cicada.module.api.ModuleInfo;
import net.cicada.module.setting.impl.NumberSetting;
import org.lwjgl.opengl.GL11;

import java.awt.*;

@ModuleInfo(name = "PointerEsp", category = Category.Render)
public class PointerEsp extends Module {
    NumberSetting radius = new NumberSetting("Radius", 30, 0, 1080, 1, () -> true, this);

    double angle;

    @Override
    public void listen(Event event) {
        if (event instanceof Render2DEvent) {
            for (Entity entity : mc.theWorld.getLoadedEntityList()) {
                if (entity == mc.thePlayer || !(entity instanceof EntityPlayer)) continue;
                Vec3 diff = entity.getPositionVector().subtract(mc.thePlayer.getPositionVector());
                angle = Math.atan2(diff.zCoord, diff.xCoord) - Math.toRadians(mc.thePlayer.rotationYaw + 180);
                float x = (float) (radius.getValue() * Math.cos(angle) + mc.displayWidth / 4D);
                float y = (float) (radius.getValue() * Math.sin(angle) + mc.displayHeight / 4D);

                RenderUtil.setGlColor(new Color(0, 0, 0, 255));
                GlStateManager.translate(x, y, 0);
                GlStateManager.rotate((float) Math.toDegrees(angle) + 90, 0, 0, 1);
                poligon(0, 0, 10, 10);
                GlStateManager.rotate((float) -(Math.toDegrees(angle) + 90), 0, 0, 1);
                GlStateManager.translate(-x, -y, 0);
            }
        }
    }

    private void poligon(float x, float y, float width, float height) {
        RenderUtil.start2D();
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_TEXTURE_2D);

        GL11.glBegin(GL11.GL_POLYGON);
        GL11.glVertex2f(x, y - height / 2);
        GL11.glVertex2f(x - width / 2, y + height / 2);
        GL11.glVertex2f(x, y + height / 4);
        GL11.glVertex2f(x + width / 2, y + height / 2);
        GL11.glEnd();

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glPopMatrix();
        RenderUtil.stop2D();
    }
}
