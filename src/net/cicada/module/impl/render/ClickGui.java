package net.cicada.module.impl.render;

import net.cicada.module.api.Category;
import net.cicada.module.api.Module;
import net.cicada.module.api.ModuleInfo;
import net.cicada.gui.zamorozkaClickGui.ZamorozkaClickGui;
import org.lwjgl.input.Keyboard;

@ModuleInfo(name = "ClickGui", category = Category.Render, key = Keyboard.KEY_RSHIFT)
public class ClickGui extends Module {
    @Override
    protected void onEnable() {
        mc.displayGuiScreen(ZamorozkaClickGui.INSTANCE);
        this.toggle();
    }
}
