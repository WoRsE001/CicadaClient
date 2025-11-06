package net.cicada.module.impl.render;

import net.cicada.module.api.Category;
import net.cicada.module.api.Module;
import net.cicada.module.api.ModuleInfo;
import net.cicada.module.setting.impl.BooleanSetting;
import net.cicada.module.setting.impl.NumberSetting;

@ModuleInfo(name = "CustomF5", category = Category.Render, state = true)
public class CustomF5 extends Module {
    public NumberSetting x = new NumberSetting("CustomX", 0, -10, 10, 0.01, () -> true, this);
    public NumberSetting y = new NumberSetting("CustomY", -0.5, -10, 10, 0.01, () -> true, this);
    public NumberSetting z = new NumberSetting("CustomZ", 2, -10, 10, 0.01, () -> true, this);
    public NumberSetting x1 = new NumberSetting("CustomXOnF5", 0, -10, 10, 0.01, () -> true, this);
    public NumberSetting y1 = new NumberSetting("CustomYOnF5", 0, -10, 10, 0.01, () -> true, this);
    public NumberSetting z1 = new NumberSetting("CustomZOnF5", 2, -10, 10, 0.01, () -> true, this);
    public BooleanSetting cameraNoClip = new BooleanSetting("CameraNoClip", true, () -> true, this);

    @Override
    public boolean listen() {
        return false;
    }
}
