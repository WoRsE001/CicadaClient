package net.cicada.module.impl.render;

import net.cicada.module.api.Category;
import net.cicada.module.api.Module;
import net.cicada.module.api.ModuleInfo;
import net.cicada.module.setting.impl.BooleanSetting;
import net.cicada.module.setting.impl.NumberSetting;

@ModuleInfo(name = "Animation", category = Category.Render)
public class Animation extends Module {
    public NumberSetting x = new NumberSetting("X", 0, -3, 3, 0.01, () -> true, this);
    public NumberSetting y = new NumberSetting("Y", 0, -3, 3, 0.01, () -> true, this);
    public NumberSetting z = new NumberSetting("Z", 0, -3, 3, 0.01, () -> true, this);
    public BooleanSetting oldAnimation = new BooleanSetting("oldAnimation", false, () -> true, this);
}
