package net.cicada.module.impl.render;

import net.cicada.gui.panelclickgui.PanelClickGui;
import net.cicada.module.api.Category;
import net.cicada.module.api.Module;
import net.cicada.module.api.ModuleInfo;
import net.cicada.module.setting.impl.ColorSetting;
import net.cicada.utility.FixedColor;
import org.lwjgl.input.Keyboard;

@ModuleInfo(name = "ClickGui", category = Category.Render, key = Keyboard.KEY_RSHIFT)
public class ClickGui extends Module {
    public ColorSetting BGColor = new ColorSetting("BackgroundColor", new FixedColor(0, 0, 0, 0), () -> true, this);
    public ColorSetting panelBGColor = new ColorSetting("PanelBackgroundColor", new FixedColor(0, 0, 0, 0.5), () -> true, this);
    public ColorSetting turnedOffModuleColor = new ColorSetting("TurnedOffModuleColor", new FixedColor(0, 0, 0, 1), () -> true, this);
    public ColorSetting turnedOnModuleColor = new ColorSetting("TurnedOnModuleColor", new FixedColor(0.5, 0.5, 0.5, 1), () -> true, this);
    public ColorSetting panelColor = new ColorSetting("PanelColor", new FixedColor(0, 0, 0, 1), () -> true, this);

    @Override
    protected void onEnable() {
        mc.displayGuiScreen(new PanelClickGui());
        this.toggle();
    }
}
