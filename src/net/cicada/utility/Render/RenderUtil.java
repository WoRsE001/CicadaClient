package net.cicada.utility.Render;

import lombok.experimental.UtilityClass;
import net.cicada.utility.Access;
import net.cicada.utility.FixedColor;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

@UtilityClass
public class RenderUtil implements Access {
    public void setGlColor(Color color) {
        GL11.glColor4f(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, color.getAlpha() / 255F);
    }

    public void setGlColor(FixedColor color) {
        GL11.glColor4f((float) color.getRed(), (float) color.getGreen(), (float) color.getBlue(), (float) color.getAlpha());
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

    public void drawRect(double x, double y, double width, double height) {
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

    public void drawCircle(float x, float y, float radius) {
        start2D();
        GL11.glBegin(GL11.GL_TRIANGLE_FAN);
        for (int i = 0; i < 360; i++) {
            GL11.glVertex2f((float) (Math.sin(Math.toRadians(i)) * radius + x), (float) (Math.cos(Math.toRadians(i)) * radius + y));
        }
        GL11.glEnd();
        stop2D();
    }

    public void drawRoundRect(float x, float y, float width, float height, float radius) {
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

    public void drawRoundRectOutline(float x, float y, float width, float height, float radius, float lineWidth) {
        start2D();
        GL11.glLineWidth(lineWidth);
        GL11.glBegin(GL_LINE_LOOP);
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

    public static void drawLine(float x, float y, float x2, float y2, float width) {
        start2D();
        GL11.glLineWidth(width);
        GL11.glBegin(GL11.GL_LINES);
        GL11.glVertex2f(x, y);
        GL11.glVertex2f(x2, y2);
        GL11.glEnd();
        GL11.glLineWidth(1.0f);
        stop2D();
    }

    public static void drawGradient(float x, float y, float width, float height) {
        start2D();
        GL11.glBegin(GL11.GL_QUADS);
        for (int i = 0; i < width; i++) {
            setGlColor(new FixedColor(i / width, 1, 1, 1, true));
            GL11.glVertex2f(x + i, y);
            GL11.glVertex2f(x + i + 1, y);
            GL11.glVertex2f(x + i + 1, y + height);
            GL11.glVertex2f(x + i, y + height);
        }
        GL11.glEnd();
        stop2D();
    }

    public static void drawGradient1(float x, float y, float width, float height, FixedColor color) {
        start2D();
        GL11.glBegin(GL11.GL_QUADS);
        for (int xI = 0; xI < width; xI++) {
            for (int yI = 0; yI < height; yI++) {
                setGlColor(new FixedColor(color.getHue(), xI / width, 1 - yI / height, 1, true));
                GL11.glVertex2f(x + xI, y + yI);
                GL11.glVertex2f(x + xI + 1, y + yI);
                GL11.glVertex2f(x + xI + 1, y + yI + 1);
                GL11.glVertex2f(x + xI, y + yI + 1);
            }
        }
        GL11.glEnd();
        stop2D();
    }

    public static void drawGradient2(float x, float y, float width, float height, FixedColor color) {
        start2D();
        GL11.glBegin(GL11.GL_QUADS);
        for (int i = 0; i < height; i++) {
            setGlColor(new FixedColor(color.getRed(), color.getGreen(), color.getBlue(), 1 - i / height));
            GL11.glVertex2f(x, y + i);
            GL11.glVertex2f(x + width, y + i);
            GL11.glVertex2f(x + width, y + 1 + i);
            GL11.glVertex2f(x, y + 1 + i);
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
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        Gui.drawModalRectWithCustomSizedTexture((int) x, (int) y, 0, 0, width, height, width, height);
        GlStateManager.disableBlend();
        glDepthMask(true);
        glDisable(GL_BLEND);
        glEnable(GL_DEPTH_TEST);
    }

    public static void drawPlayerHead(float x, float y, float headSize, Entity entity) {
        NetworkPlayerInfo playerInfo = mc.getNetHandler().getPlayerInfo(entity.getUniqueID());
        ResourceLocation skinLoc = playerInfo != null ? playerInfo.getLocationSkin() : mc.thePlayer.getLocationSkin();
        mc.getTextureManager().bindTexture(skinLoc);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        Gui.drawScaledCustomSizeModalRect((int) x, (int) y, 4F, 4F, 4, 4, (int) headSize, (int) headSize, 32, 32);
        GlStateManager.resetColor();
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
