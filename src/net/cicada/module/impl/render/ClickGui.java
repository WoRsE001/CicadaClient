package net.cicada.module.impl.render;

import net.cicada.module.api.Category;
import net.cicada.module.api.Module;
import net.cicada.module.api.ModuleInfo;
import net.cicada.module.setting.impl.ListSetting;
import net.cicada.ui.windowClickGui.WindowClickGui;
import net.cicada.ui.zamorozkaClickGui.ZamorozkaClickGui;
import org.lwjgl.input.Keyboard;

import java.util.List;

@ModuleInfo(name = "ClickGui", category = Category.Render, key = Keyboard.KEY_RSHIFT)
public class ClickGui extends Module {
    ListSetting type = new ListSetting("Type", "Zamorozka", List.of("Augustus", "Zamorozka"), () -> true, this);

    @Override
    protected void onEnable() {
        switch(this.type.getValue()) {
            case "Augustus":
                mc.displayGuiScreen(new WindowClickGui());
                break;
            case "Zamorozka":
                mc.displayGuiScreen(new ZamorozkaClickGui());
                break;
        }

        this.toggle();
    }
}
