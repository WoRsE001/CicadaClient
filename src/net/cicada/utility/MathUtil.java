package net.cicada.utility;

import lombok.experimental.UtilityClass;
import net.minecraft.client.renderer.GLAllocation;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

@UtilityClass
public class MathUtil implements Access {
    public double interpolate(double old, double now, float partialTicks) {
        return old + (now - old) * partialTicks;
    }

    public float[] project2D(float x, float y, float z, int scaleFactor) {
        GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, GLAllocation.createDirectFloatBuffer(16));
        GL11.glGetFloat(GL11.GL_PROJECTION_MATRIX, GLAllocation.createDirectFloatBuffer(16));
        GL11.glGetInteger(GL11.GL_VIEWPORT, GLAllocation.createDirectIntBuffer(16));
        if (GLU.gluProject(x, y, z, GLAllocation.createDirectFloatBuffer(16), GLAllocation.createDirectFloatBuffer(16), GLAllocation.createDirectIntBuffer(16), GLAllocation.createDirectFloatBuffer(4))) {
            return new float[]{GLAllocation.createDirectFloatBuffer(4).get(0) / scaleFactor,
            (Display.getHeight() - GLAllocation.createDirectFloatBuffer(4).get(1)) / scaleFactor,
            GLAllocation.createDirectFloatBuffer(4).get(2)};
        }
        return null;
    }
}
