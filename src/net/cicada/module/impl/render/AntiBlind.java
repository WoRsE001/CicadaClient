package net.cicada.module.impl.render;

import net.cicada.module.api.Category;
import net.cicada.module.api.Module;
import net.cicada.module.api.ModuleInfo;
import net.cicada.module.setting.impl.BooleanSetting;

@ModuleInfo(name = "AntiBlind", category = Category.Render, state = true)
public class AntiBlind extends Module {
    public BooleanSetting achievements = new BooleanSetting("Achievements", false, () -> true, this);
    public BooleanSetting blockOverlay = new BooleanSetting("BlockOverlay", false, () -> true, this);
    public BooleanSetting blindness = new BooleanSetting("Blindness", false, () -> true, this);

    @Override
    public boolean listen() {
        return false;
    }
}
