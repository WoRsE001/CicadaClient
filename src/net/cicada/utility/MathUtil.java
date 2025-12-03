package net.cicada.utility;

import lombok.experimental.UtilityClass;

@UtilityClass
public class MathUtil implements Access {
    public double interpolate(double old, double now, float partialTicks) {
        return old + (now - old) * partialTicks;
    }

    public int random(int min, int max) {
        return (int) (Math.random() * (max - min) + min);
    }

    public float random(float min, float max) {
        return (float) (Math.random() * (max - min) + min);
    }

    public double random(double min, double max) {
        return Math.random() * (max - min) + min;
    }
}
