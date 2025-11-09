package net.cicada.module.impl.render;

import net.cicada.module.api.Category;
import net.cicada.module.api.Module;
import net.cicada.module.api.ModuleInfo;
import net.cicada.module.setting.impl.BooleanSetting;
import net.cicada.module.setting.impl.ListSetting;
import net.cicada.module.setting.impl.NumberSetting;

import java.util.List;

@ModuleInfo(name = "Animation", category = Category.Render)
public class Animation extends Module {
    public NumberSetting x = new NumberSetting("X", 0, -3, 3, 0.01, () -> true, this);
    public NumberSetting y = new NumberSetting("Y", 0, -3, 3, 0.01, () -> true, this);
    public NumberSetting z = new NumberSetting("Z", 0, -3, 3, 0.01, () -> true, this);
    public NumberSetting x1 = new NumberSetting("XOnBlock", 0, -3, 3, 0.01, () -> true, this);
    public NumberSetting y1 = new NumberSetting("YOnBlock", 0, -3, 3, 0.01, () -> true, this);
    public NumberSetting z1 = new NumberSetting("ZOnBlock", 0, -3, 3, 0.01, () -> true, this);
    public ListSetting typeAnimation = new ListSetting("TypeAnimation", "None", List.of("None", "1.7", "Smooth", "Punch", "OldExhibition"), () -> true, this);
}
