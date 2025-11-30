package net.cicada.utility.Render;

import lombok.experimental.UtilityClass;
import net.cicada.utility.Access;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;

import static net.minecraft.client.gui.Gui.drawModalRectWithCustomSizedTexture;
import static org.lwjgl.opengl.GL11.*;

@UtilityClass
public class RenderUtil implements Access {
    public void setGlColor(Color color) {
        GL11.glColor4f(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, color.getAlpha() / 255F);
    }

    public void start2D() {
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.disableTexture2D();
        GlStateManager.disableCull();
        GlStateManager.disableAlpha();
        GlStateManager.disableDepth();
    }

    public void stop2D() {
        GlStateManager.enableDepth();
        GlStateManager.enableAlpha();
        GlStateManager.enableCull();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.resetColor();
    }

    public void render2DRect(double x, double y, double width, double height) {
        start2D();
        GL11.glPushMatrix();
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2d(x, y);
        GL11.glVertex2d(x + width, y);
        GL11.glVertex2d(x + width, y + height);
        GL11.glVertex2d(x, y + height);
        GL11.glEnd();
        GL11.glPopMatrix();
        stop2D();
    }

    public void render2DCircle(float x, float y, float radius) {
        start2D();
        GL11.glBegin(GL11.GL_TRIANGLE_FAN);
        for (int i = 0; i < 360; i++) {
            GL11.glVertex2f((float) (Math.sin(Math.toRadians(i)) * radius + x), (float) (Math.cos(Math.toRadians(i)) * radius + y));
        }
        GL11.glEnd();
        stop2D();
    }

    public void render2DRoundRect(float x, float y, float width, float height, float radius) {
        start2D();
        GL11.glBegin(GL11.GL_TRIANGLE_FAN);
        int i;
        for (i = 0; i <= 90; i++) {
            GL11.glVertex2f((float) (Math.sin(Math.toRadians(i)) * radius + x + width - radius), (float) (-Math.cos(Math.toRadians(i)) * radius + y + radius));
        }
        for (i = 90; i <= 180; i++) {
            GL11.glVertex2f((float) (Math.sin(Math.toRadians(i)) * radius + x + width - radius), (float) (-Math.cos(Math.toRadians(i)) * radius + y + height - radius));
        }
        for (i = 180; i <= 270; i++) {
            GL11.glVertex2f((float) (Math.sin(Math.toRadians(i)) * radius + x + radius), (float) (-Math.cos(Math.toRadians(i)) * radius + y + height - radius));
        }
        for (i = 270; i <= 360; i++) {
            GL11.glVertex2f((float) (Math.sin(Math.toRadians(i)) * radius + x + radius), (float) (-Math.cos(Math.toRadians(i)) * radius + y + radius));
        }
        GL11.glEnd();
        stop2D();
    }

    public static void drawImage(ResourceLocation image, float x, float y, int width, int height) {
        glDisable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);
        glDepthMask(false);
        OpenGlHelper.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_ONE, GL_ZERO);
        glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(image);
        drawModalRectWithCustomSizedTexture((int) x, (int) y, 0, 0, width, height, width, height);
        GlStateManager.disableBlend();
        glDepthMask(true);
        glDisable(GL_BLEND);
        glEnable(GL_DEPTH_TEST);
    }

    public void start3D() {
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDepthMask(false);
        GlStateManager.disableCull();
        GL11.glTranslated(-mc.getRenderManager().viewerPosX, -mc.getRenderManager().viewerPosY, -mc.getRenderManager().viewerPosZ);
    }

    public void stop3D() {
        GL11.glTranslated(mc.getRenderManager().viewerPosX, mc.getRenderManager().viewerPosY, mc.getRenderManager().viewerPosZ);
        GlStateManager.enableCull();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_BLEND);
    }

    public void render3DBox(AxisAlignedBB box) {
        start3D();
        GL11.glPushMatrix();

        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex3d(box.minX, box.minY, box.maxZ);
        GL11.glVertex3d(box.maxX, box.minY, box.maxZ);
        GL11.glVertex3d(box.maxX, box.minY, box.minZ);
        GL11.glVertex3d(box.minX, box.minY, box.minZ);
        GL11.glEnd();

        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex3d(box.minX, box.maxY, box.maxZ);
        GL11.glVertex3d(box.maxX, box.maxY, box.maxZ);
        GL11.glVertex3d(box.maxX, box.maxY, box.minZ);
        GL11.glVertex3d(box.minX, box.maxY, box.minZ);
        GL11.glEnd();

        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex3d(box.minX, box.minY, box.minZ);
        GL11.glVertex3d(box.minX, box.minY, box.maxZ);
        GL11.glVertex3d(box.minX, box.maxY, box.maxZ);
        GL11.glVertex3d(box.minX, box.maxY, box.minZ);
        GL11.glEnd();

        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex3d(box.maxX, box.minY, box.minZ);
        GL11.glVertex3d(box.maxX, box.minY, box.maxZ);
        GL11.glVertex3d(box.maxX, box.maxY, box.maxZ);
        GL11.glVertex3d(box.maxX, box.maxY, box.minZ);
        GL11.glEnd();

        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex3d(box.minX, box.minY, box.minZ);
        GL11.glVertex3d(box.maxX, box.minY, box.minZ);
        GL11.glVertex3d(box.maxX, box.maxY, box.minZ);
        GL11.glVertex3d(box.minX, box.maxY, box.minZ);
        GL11.glEnd();

        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex3d(box.minX, box.minY, box.maxZ);
        GL11.glVertex3d(box.maxX, box.minY, box.maxZ);
        GL11.glVertex3d(box.maxX, box.maxY, box.maxZ);
        GL11.glVertex3d(box.minX, box.maxY, box.maxZ);
        GL11.glEnd();

        GL11.glColor4f(1F, 1F, 1F, 1F);
        GL11.glPopMatrix();
        stop3D();
    }

    public void render3DHitBox(AxisAlignedBB box, float lineWidth) {
        start3D();
        GL11.glLineWidth(lineWidth);

        GL11.glBegin(GL11.GL_LINE_LOOP);
        GL11.glVertex3d(box.minX, box.minY, box.maxZ);
        GL11.glVertex3d(box.maxX, box.minY, box.maxZ);
        GL11.glVertex3d(box.maxX, box.minY, box.minZ);
        GL11.glVertex3d(box.minX, box.minY, box.minZ);
        GL11.glEnd();

        GL11.glBegin(GL11.GL_LINE_LOOP);
        GL11.glVertex3d(box.minX, box.maxY, box.maxZ);
        GL11.glVertex3d(box.maxX, box.maxY, box.maxZ);
        GL11.glVertex3d(box.maxX, box.maxY, box.minZ);
        GL11.glVertex3d(box.minX, box.maxY, box.minZ);
        GL11.glEnd();

        GL11.glBegin(GL11.GL_LINE_LOOP);
        GL11.glVertex3d(box.minX, box.minY, box.minZ);
        GL11.glVertex3d(box.minX, box.minY, box.maxZ);
        GL11.glVertex3d(box.minX, box.maxY, box.maxZ);
        GL11.glVertex3d(box.minX, box.maxY, box.minZ);
        GL11.glEnd();

        GL11.glBegin(GL11.GL_LINE_LOOP);
        GL11.glVertex3d(box.maxX, box.minY, box.minZ);
        GL11.glVertex3d(box.maxX, box.minY, box.maxZ);
        GL11.glVertex3d(box.maxX, box.maxY, box.maxZ);
        GL11.glVertex3d(box.maxX, box.maxY, box.minZ);
        GL11.glEnd();

        GL11.glBegin(GL11.GL_LINE_LOOP);
        GL11.glVertex3d(box.minX, box.minY, box.minZ);
        GL11.glVertex3d(box.maxX, box.minY, box.minZ);
        GL11.glVertex3d(box.maxX, box.maxY, box.minZ);
        GL11.glVertex3d(box.minX, box.maxY, box.minZ);
        GL11.glEnd();

        GL11.glBegin(GL11.GL_LINE_LOOP);
        GL11.glVertex3d(box.minX, box.minY, box.maxZ);
        GL11.glVertex3d(box.maxX, box.minY, box.maxZ);
        GL11.glVertex3d(box.maxX, box.maxY, box.maxZ);
        GL11.glVertex3d(box.minX, box.maxY, box.maxZ);
        GL11.glEnd();

        GL11.glColor4f(1F, 1F, 1F, 1F);
        GL11.glLineWidth(1);
        stop3D();
    }

    public void render3DEntityBox(Entity entity, float expand) {
        AxisAlignedBB entityBox = entity.getEntityBoundingBox().expand(expand, expand, expand);
        double smoothOffsetX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * mc.timer.renderPartialTicks - entity.posX;
        double smoothOffsetY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * mc.timer.renderPartialTicks - entity.posY;
        double smoothOffsetZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * mc.timer.renderPartialTicks - entity.posZ;

        GL11.glTranslated(smoothOffsetX, smoothOffsetY, smoothOffsetZ);
        render3DBox(entityBox);
        GL11.glTranslated(-smoothOffsetX, -smoothOffsetY, -smoothOffsetZ);
    }

    public void render3DEntityHitBox(Entity entity, float expand, float lineWidth) {
        AxisAlignedBB entityBox = entity.getEntityBoundingBox().expand(expand, expand, expand);
        double smoothOffsetX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * mc.timer.renderPartialTicks - entity.posX;
        double smoothOffsetY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * mc.timer.renderPartialTicks - entity.posY;
        double smoothOffsetZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * mc.timer.renderPartialTicks - entity.posZ;

        GL11.glTranslated(smoothOffsetX, smoothOffsetY, smoothOffsetZ);
        render3DHitBox(entityBox, lineWidth);
        GL11.glTranslated(-smoothOffsetX, -smoothOffsetY, -smoothOffsetZ);
    }
}
