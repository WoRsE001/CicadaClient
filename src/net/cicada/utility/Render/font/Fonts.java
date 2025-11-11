package net.cicada.utility.Render.font;

import com.google.common.base.Preconditions;
import net.cicada.Cicada;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public enum Fonts {
    ComicSansMS("ComicSansMS"),
    Minecraft("Minecraft"),
    Papyrus("Papyrus");

    private final String file;
    private final Map<Float, FontRenderer> fontMap = new HashMap<>();

    Fonts(String file) {
        this.file = file;
    }

    public FontRenderer get(float size) {
        return this.fontMap.computeIfAbsent(size, font -> {
            try {
                return create(this.file, size, true);
            } catch (Exception var5) {
                throw new RuntimeException("Unable to load font: " + this, var5);
            }
        });
    }

    public FontRenderer get(float size, boolean antiAlias) {
        return this.fontMap.computeIfAbsent(size, font -> {
            try {
                return create(this.file, size, antiAlias);
            } catch (Exception var5) {
                throw new RuntimeException("Unable to load font: " + this, var5);
            }
        });
    }

    public FontRenderer create(String file, float size, boolean antiAlias) {
        try (
                var in = Preconditions.checkNotNull(
                        Cicada.class.getResourceAsStream("/assets/minecraft/" + Cicada.name.toLowerCase() + "/fonts/" + file + ".ttf"), "Font resource is null"
                )
        ) {
            var font = Font.createFont(0, in).deriveFont(Font.PLAIN, size);

            if (font != null) {
                return new FontRenderer(font, antiAlias);
            } else {
                throw new RuntimeException("Failed to create font");
            }
        } catch (Exception ex) {
            throw new RuntimeException("Failed to create font", ex);
        }
    }
}
