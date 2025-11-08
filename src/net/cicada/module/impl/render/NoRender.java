package net.cicada.module.impl.render;

import net.cicada.module.api.Category;
import net.cicada.module.api.Module;
import net.cicada.module.api.ModuleInfo;
import net.cicada.module.setting.impl.BooleanSetting;

@ModuleInfo(name = "AntiBlind", category = Category.Render)
public class NoRender extends Module {
    public BooleanSetting achievements = new BooleanSetting("Achievements", true, () -> true, this);
    public BooleanSetting blockOverlay = new BooleanSetting("BlockOverlay", true, () -> true, this);
    public BooleanSetting blindness = new BooleanSetting("Blindness", true, () -> true, this);

    @Override
    public boolean listen() {
        return false;
    }
}
