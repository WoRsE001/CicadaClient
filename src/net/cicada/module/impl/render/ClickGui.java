package net.cicada.module.impl.render;

import net.cicada.module.api.Category;
import net.cicada.module.api.Module;
import net.cicada.module.api.ModuleInfo;
import net.cicada.module.setting.impl.ListSetting;
import net.cicada.module.setting.impl.NumberSetting;
import net.cicada.ui.windowClickGui.WindowClickGui;
import net.cicada.ui.zamorozkaClickGui.ZamorozkaClickGui;
import net.cicada.utility.Render.font.Fonts;
import org.lwjgl.input.Keyboard;

import java.util.Arrays;
import java.util.List;

@ModuleInfo(name = "ClickGui", category = Category.Render, key = Keyboard.KEY_RSHIFT)
public class ClickGui extends Module {
    ListSetting type = new ListSetting("Type", "Zamorozka", List.of("Augustus", "Zamorozka"), () -> true, this);
    ListSetting font = new ListSetting("Font", "Minecraft", Arrays.stream(Fonts.values()).map(Enum::name).toList(), () -> true, this);
    NumberSetting fontSize = new NumberSetting("FontSize", 20, 1, 100, 1, () -> true, this);

    @Override
    protected void onEnable() {
        for (Fonts value : Fonts.values()) {
            if (value.name().equals(this.font.getValue())) {
                WindowClickGui.font = value.get((float) this.fontSize.getValue());
                ZamorozkaClickGui.font = value.get((float) this.fontSize.getValue());
            }
        }

        switch(this.type.getValue()) {
            case "Augustus":
                mc.displayGuiScreen(WindowClickGui.INSTANCE);
                break;
            case "Zamorozka":
                mc.displayGuiScreen(ZamorozkaClickGui.INSTANCE);
                break;
        }

        this.toggle();
    }
}
