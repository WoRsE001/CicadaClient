package net.cicada.module.impl.render;

import net.cicada.module.api.Category;
import net.cicada.module.api.Module;
import net.cicada.module.api.ModuleInfo;
import net.cicada.ui.windowClickGui.WindowClickGui;
import org.lwjgl.input.Keyboard;

@ModuleInfo(name = "ClickGui", category = Category.Render, key = Keyboard.KEY_RSHIFT)
public class ClickGui extends Module {
    @Override
    protected void onEnable() {
        mc.displayGuiScreen(new WindowClickGui());
        this.toggle();
    }
}
