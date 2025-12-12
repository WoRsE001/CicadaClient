package net.cicada.utility;

import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.math.RoundingMode;

@UtilityClass
public class MathUtil implements Access {
    public double interpolate(double old, double now, float partialTicks) {
        return old + (now - old) * partialTicks;
    }

    public double round(double number, double step) {
        BigDecimal bdNumber = BigDecimal.valueOf(number);
        BigDecimal bdStep = BigDecimal.valueOf(step);
        BigDecimal divided = bdNumber.divide(bdStep, 0, RoundingMode.HALF_UP);
        return divided.multiply(bdStep).doubleValue();
    }

    public double map(double value, double inputMin, double inputMax, double outputMin, double outputMax) {
        return ((value - inputMin) / (inputMax - inputMin)) * (outputMax - outputMin) + outputMin;
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
