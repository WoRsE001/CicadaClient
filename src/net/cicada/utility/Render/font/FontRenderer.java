package net.cicada.utility.Render.font;

import net.minecraft.client.renderer.GlStateManager;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.Arrays;

import static org.lwjgl.opengl.GL11.*;

public class FontRenderer {
    private static final int[] colorCode = new int[32];
    private final boolean antiAlias;

    static {
        for (int i = 0; i < 32; ++i) {
            int base = (i >> 3 & 1) * 85;
            int r = (i >> 2 & 1) * 170 + base;
            int g = (i >> 1 & 1) * 170 + base;
            int b = (i & 1) * 170 + base;
            if (i == 6) {
                r += 85;
            }

            if (i >= 16) {
                r /= 4;
                g /= 4;
                b /= 4;
            }

            colorCode[i] = (r & 255) << 16 | (g & 255) << 8 | b & 255;
        }
    }

    public final void drawCenteredString(String text, float x, float y, int color) {
        drawString(text, x - (float) getStringWidth(text) / 2, y, color);
    }

    public final void drawCenteredStringWithShadow(String text, float x, float y, int color) {
        drawStringWithShadow(text, x - (float) getStringWidth(text) / 2, y, color);
    }


    private final byte[][] charwidth = new byte[256][];
    private final int[] textures = new int[256];
    private final FontRenderContext context = new FontRenderContext(new AffineTransform(), true, true);
    private Font font = null;

    private float size = 0;
    private int fontWidth = 0;
    private int fontHeight = 0;
    private int textureWidth = 0;
    private int textureHeight = 0;

    public FontRenderer(Font font) {
        this.antiAlias = true;
        this.font = font;
        size = font.getSize2D();
        Arrays.fill(textures, -1);
        Rectangle2D maxBounds = font.getMaxCharBounds(context);
        this.fontWidth = (int) Math.ceil(maxBounds.getWidth());
        this.fontHeight = (int) Math.ceil(maxBounds.getHeight());
        if (fontWidth > 127 || fontHeight > 127) throw new IllegalArgumentException("Font size to large!");
        this.textureWidth = resizeToOpenGLSupportResolution(fontWidth * 16);
        this.textureHeight = resizeToOpenGLSupportResolution(fontHeight * 16);
    }

    public FontRenderer(Font font,boolean antiAlias) {
        this.antiAlias = antiAlias;
        this.font = font;
        size = font.getSize2D();
        Arrays.fill(textures, -1);
        Rectangle2D maxBounds = font.getMaxCharBounds(context);
        this.fontWidth = (int) Math.ceil(maxBounds.getWidth());
        this.fontHeight = (int) Math.ceil(maxBounds.getHeight());
        if (fontWidth > 127 || fontHeight > 127) throw new IllegalArgumentException("Font size to large!");
        this.textureWidth = resizeToOpenGLSupportResolution(fontWidth * 16);
        this.textureHeight = resizeToOpenGLSupportResolution(fontHeight * 16);
    }

    public final int getHeight() {
        return fontHeight / 2;
    }

    protected final int drawChar(char chr, float x, float y) {
        int region = chr >> 8;
        int id = chr & 0xFF;
        int xTexCoord = (id & 0xF) * fontWidth,
                yTexCoord = (id >> 4) * fontHeight;
        int width = getOrGenerateCharWidthMap(region)[id];
        GlStateManager.bindTexture(getOrGenerateCharTexture(region));
        GlStateManager.enableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glBegin(GL_QUADS);
        glTexCoord2d(wrapTextureCoord(xTexCoord, textureWidth), wrapTextureCoord(yTexCoord, textureHeight));
        glVertex2f(x, y);
        glTexCoord2d(wrapTextureCoord(xTexCoord, textureWidth), wrapTextureCoord(yTexCoord + fontHeight, textureHeight));
        glVertex2f(x, y + fontHeight);
        glTexCoord2d(wrapTextureCoord(xTexCoord + width, textureWidth), wrapTextureCoord(yTexCoord + fontHeight, textureHeight));
        glVertex2f(x + width, y + fontHeight);
        glTexCoord2d(wrapTextureCoord(xTexCoord + width, textureWidth), wrapTextureCoord(yTexCoord, textureHeight));
        glVertex2f(x + width, y);
        glEnd();
        return width;
    }

    public void drawString(String str, float x, float y, int color) {
        drawString(str, x, y, color, false);
    }

    public void drawString(String str, double x, double y, int color) {
        drawString(str, (float) x, (float) y, color, false);
    }

    public final void drawString(String str, float x, float y, int color, boolean darken) {
        GlStateManager.color(1F, 1F, 1F, 1F);
        str = str.replace("▬", "=");
        y = y - 2;
        x *= 2;
        y *= 2;
        y -= 2;
        int offset = 0;
        if (darken) {
            color = (color & 0xFCFCFC) >> 2 | color & 0xFF000000;
        }
        float r, g, b, a;
        r = (color >> 16 & 0xFF) / 255f;
        g = (color >> 8 & 0xFF) / 255f;
        b = (color & 0xFF) / 255f;
        a = (color >> 24 & 0xFF) / 255f;
        if (a == 0)
            a = 1;
        GlStateManager.color(r, g, b, a);
        glPushMatrix();
        glScaled(0.5, 0.5, 0.5);
        char[] chars = str.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char chr = chars[i];
            if (chr == '§' && i != chars.length - 1) {
                i++;
                color = "0123456789abcdef".indexOf(chars[i]);
                if (color != -1) {
                    if (darken) color |= 0x10;
                    color = colorCode[color];
                    r = (color >> 16 & 0xFF) / 255f;
                    g = (color >> 8 & 0xFF) / 255f;
                    b = (color & 0xFF) / 255f;
                    GlStateManager.color(r, g, b, a);
                }
                continue;
            }
            offset += drawChar(chr, x + offset, y);
        }
        glPopMatrix();
    }

    public final int getStringWidth(String text) {

        if (text == null) {
            return 0;
        }

        int width = 0;
        char[] currentData = text.toCharArray();

        int size = text.length();
        int i = 0;
        while (i < size) {
            char chr = currentData[i];

            char character = text.charAt(i);
            if (character == '§') {
                ++i;
            } else {
                width += getOrGenerateCharWidthMap(chr >> 8)[chr & 0xFF];
            }
            ++i;
        }
        return width / 2;
    }


    public final float getSize() {
        return size;
    }

    private int generateCharTexture(int id) {
        int textureId = glGenTextures();
        int offset = id << 8;
        BufferedImage img = new BufferedImage(textureWidth, textureHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) img.getGraphics();
        if (antiAlias) {
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        }
        g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g.setFont(font);
        g.setColor(Color.WHITE);
        FontMetrics fontMetrics = g.getFontMetrics();
        for (int y = 0; y < 16; y++)
            for (int x = 0; x < 16; x++) {
                String chr = String.valueOf((char) ((y << 4 | x) | offset));
                g.drawString(chr, x * fontWidth, y * fontHeight + fontMetrics.getAscent());
            }
        glBindTexture(GL_TEXTURE_2D, textureId);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, textureWidth, textureHeight, 0, GL_RGBA, GL_UNSIGNED_BYTE, imageToBuffer(img));
        return textureId;
    }

    private int getOrGenerateCharTexture(int id) {
        if (textures[id] == -1)
            return textures[id] = generateCharTexture(id);
        return textures[id];
    }

    private int resizeToOpenGLSupportResolution(int size) {
        int power = 0;
        while (size > 1 << power) power++;
        return 1 << power;
    }

    private byte[] generateCharWidthMap(int id) {
        int offset = id << 8;
        byte[] widthmap = new byte[256];
        for (int i = 0; i < widthmap.length; i++) {
            widthmap[i] = (byte) Math.ceil(font.getStringBounds(String.valueOf((char) (i | offset)), context).getWidth());
        }
        return widthmap;
    }

    private final byte[] getOrGenerateCharWidthMap(int id) {
        if (charwidth[id] == null)
            return charwidth[id] = generateCharWidthMap(id);
        return charwidth[id];
    }

    private double wrapTextureCoord(int coord, int size) {
        return coord / (double) size;
    }

    private static ByteBuffer imageToBuffer(BufferedImage img) {
        int[] arr = img.getRGB(0, 0, img.getWidth(), img.getHeight(), null, 0, img.getWidth());
        ByteBuffer buf = ByteBuffer.allocateDirect(4 * arr.length);

        for (int i : arr) {
            buf.putInt(i << 8 | i >> 24 & 0xFF);
        }

        buf.flip();
        return buf;
    }

    public final void drawStringWithShadow(String newstr, float i, float i1, int rgb) {
        drawString(newstr, i + 0.5f, i1 + 0.5f, rgb, true);
        drawString(newstr, i, i1, rgb);
    }

    public void drawStringWithShadow(String z, double x, double positionY, int mainTextColor) {
        drawStringWithShadow(z, (float) x, (float) positionY, mainTextColor);
    }

    public void drawStringWithShadow(String text, double x, double y, double sWidth, int color) {
        this.drawString(text, (float) (x + sWidth), (float) (y + sWidth), color, true);
    }
}

