package net.cicada.module.impl.render;

import lombok.Getter;
import net.cicada.module.api.Category;
import net.cicada.module.api.Module;
import net.cicada.module.api.ModuleInfo;
import net.cicada.gui.zamorozkaClickGui.ZamorozkaClickGui;
import net.cicada.module.setting.impl.ListSetting;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;

import java.util.List;

@ModuleInfo(name = "ClickGui", category = Category.Render, key = Keyboard.KEY_RSHIFT)
public class ClickGui extends Module {
    public ListSetting image = new ListSetting("Image", "Martin", List.of("None", "Martin", "Hitori", "Nijika"), () -> true, this);

    @Override
    protected void onEnable() {
        mc.displayGuiScreen(ZamorozkaClickGui.INSTANCE);
        this.toggle();
    }

    @Getter
    public enum ImagesLoc {
        Martin("Martin", "ClickGUI"),
        Hitori("Hitori", "ClickGUI1"),
        Nijika("Nijika", "ClickGUI2");

        private final String name;
        private final String file;

        ImagesLoc(String name, String file) {
            this.name = name;
            this.file = file;
        }

        public static ResourceLocation get(String name) {
            for (ImagesLoc value : ImagesLoc.values()) {
                if (value.getName().equalsIgnoreCase(name)) return new ResourceLocation("cicada/images/" + value.getFile() + ".png");
            }
            return null;
        }
    }
}
