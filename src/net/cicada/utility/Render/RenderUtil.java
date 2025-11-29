package net.cicada.utility.Render;

import lombok.experimental.UtilityClass;
import net.cicada.utility.Access;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;

import static net.minecraft.client.gui.Gui.drawModalRectWithCustomSizedTexture;
import static net.minecraft.client.renderer.vertex.DefaultVertexFormats.POSITION_TEX_COLOR;
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
            float cos = (float) (Math.cos(Math.toRadians(i))) * radius;
            float sin = (float) (Math.sin(Math.toRadians(i))) * radius;
            GL11.glVertex2f(x + cos, y + sin);
        }
        GL11.glEnd();
        stop2D();
    }

    public void render2DRoundRect(float x, float y, float width, float height, float radius) {
        render2DCircle(x + radius, y + radius, radius);
        render2DCircle(x + width - radius, y + radius, radius);
        render2DCircle(x + width - radius, y + height - radius, radius);
        render2DCircle(x + radius, y + height - radius, radius);
        render2DRect(x + radius, y, width - radius * 2, height);
        render2DRect(x, y + radius, width, height - radius * 2);
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

    // Измените сигнатуру, чтобы принять угол вращения
    public static void drawTexture3D(ResourceLocation location, double x, double y, double z, float width, float height, float rotationAngle) {
        mc.getTextureManager().bindTexture(location);

        // Получаем смещение рендера игрока
        double renderPosX = mc.getRenderManager().viewerPosX;
        double renderPosY = mc.getRenderManager().viewerPosY;
        double renderPosZ = mc.getRenderManager().viewerPosZ;

        GL11.glPushMatrix();

        // 1. Перемещаем в 3D позицию (со смещением рендера)
        GL11.glTranslated(x - renderPosX, y - renderPosY, z - renderPosZ);

        // 2. Поворачиваем, чтобы плоскость была обращена к камере
        GL11.glRotatef(-mc.getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(mc.getRenderManager().playerViewX, 1.0F, 0.0F, 0.0F);

        // 3. ПРИМЕНЯЕМ ВРАЩЕНИЕ ВОКРУГ ОСИ Z (Ось, перпендикулярная плоскости)
        GL11.glRotatef(rotationAngle, 0.0F, 0.0F, 1.0F); // <-- НОВОЕ ВРАЩЕНИЕ

        // Начинаем рендеринг
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.enableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();

        // Смещаем, чтобы иконка была отцентрована
        float halfWidth = width / 2.0F;
        float halfHeight = height / 2.0F;

        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();

        worldrenderer.begin(GL11.GL_QUADS, POSITION_TEX_COLOR);

        // Нижний левый угол
        worldrenderer.pos(-halfWidth, -halfHeight, 0.0D).tex(0.0D, 1.0D).color(255, 255, 255, 255).endVertex();
        // Нижний правый угол
        worldrenderer.pos(halfWidth, -halfHeight, 0.0D).tex(1.0D, 1.0D).color(255, 255, 255, 255).endVertex();
        // Верхний правый угол
        worldrenderer.pos(halfWidth, halfHeight, 0.0D).tex(1.0D, 0.0D).color(255, 255, 255, 255).endVertex();
        // Верхний левый угол
        worldrenderer.pos(-halfWidth, halfHeight, 0.0D).tex(0.0D, 0.0D).color(255, 255, 255, 255).endVertex();

        tessellator.draw();

        // Возвращаем настройки OpenGL
        GlStateManager.enableDepth();
        GlStateManager.disableBlend();
        GL11.glPopMatrix();
    }
}
