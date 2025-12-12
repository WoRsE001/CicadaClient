package net.cicada.utility;

import lombok.Getter;
import lombok.Setter;

import java.awt.*;

@Getter @Setter
public class FixedColor {
    private double red, green, blue, hue, saturation, brightness, alpha;

    public FixedColor(double red, double green, double blue, double alpha) {
        setRGBA(red, green, blue, alpha);
    }

    public FixedColor(double hue, double saturation, double brightness, double alpha, boolean isHSB) {
        setHSBA(hue, saturation, brightness, alpha);
    }

    public FixedColor(Color color) {
        setColor(color);
    }

    public Color getColor() {
        return new Color((float) red / 255, (float) green / 255, (float) blue / 255, (float) alpha / 255);
    }

    public FixedColor setRGBA(double red, double green, double blue, double alpha) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        double[] HSB = RGBtoHSB(red, green, blue);
        hue = HSB[0];
        saturation = HSB[1];
        brightness = HSB[2];
        this.alpha = alpha;
        return this;
    }

    public FixedColor setHSBA(double hue, double saturation, double brightness, double alpha) {
        this.hue = hue;
        this.saturation = saturation;
        this.brightness = brightness;
        double[] RGB = HSBtoRGB(hue, saturation, brightness);
        red = RGB[0];
        green = RGB[1];
        blue = RGB[2];
        this.alpha = alpha;
        return this;
    }

    public FixedColor setColor(Color color) {
        return setRGBA(
                color.getRed() / 255.0,
                color.getGreen() / 255.0,
                color.getBlue() / 255.0,
                color.getAlpha() / 255.0
        );
    }

    public static double[] RGBtoHSB(double red, double green, double blue) {
        double min = Math.min(red, Math.min(green, blue));
        double max = Math.max(red, Math.max(green, blue));
        double hue = 0;
        double saturation;
        double brightness = max;

        double delta = max - min;

        if (max != 0) {
            saturation = delta / max;
        } else {
            saturation = 0;
        }

        if (delta != 0) {
            if (red == max) {
                hue = (green - blue) / delta;
            } else if (green == max) {
                hue = 2.0 + (blue - red) / delta;
            } else {
                hue = 4.0 + (red - green) / delta;
            }
            hue /= 6.0;
            if (hue < 0) {
                hue += 1.0;
            }
        }

        return new double[]{hue, saturation, brightness};
    }

    public static double[] HSBtoRGB(double hue, double saturation, double brightness) {
        double red = 0, green = 0, blue = 0;

        if (saturation == 0) {
            red = green = blue = brightness;
        } else {
            double h = (hue - Math.floor(hue)) * 6.0;
            double f = h - Math.floor(h);
            double p = brightness * (1.0 - saturation);
            double q = brightness * (1.0 - saturation * f);
            double t = brightness * (1.0 - (saturation * (1.0 - f)));

            switch ((int) h) {
                case 0:
                    red = brightness;
                    green = t;
                    blue = p;
                    break;
                case 1:
                    red = q;
                    green = brightness;
                    blue = p;
                    break;
                case 2:
                    red = p;
                    green = brightness;
                    blue = t;
                    break;
                case 3:
                    red = p;
                    green = q;
                    blue = brightness;
                    break;
                case 4:
                    red = t;
                    green = p;
                    blue = brightness;
                    break;
                case 5:
                    red = brightness;
                    green = p;
                    blue = q;
                    break;
            }
        }

        return new double[]{red, green, blue};
    }
}