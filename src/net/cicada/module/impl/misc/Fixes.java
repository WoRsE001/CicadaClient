package net.cicada.module.impl.misc;

import net.cicada.module.api.Category;
import net.cicada.module.api.Module;
import net.cicada.module.api.ModuleInfo;
import net.cicada.module.setting.impl.BooleanSetting;

@ModuleInfo(name = "Fixes", category = Category.Misc, state = true)
public class Fixes extends Module {
    public BooleanSetting noHitDelay = new BooleanSetting("noHitDelay", true, () -> true, this);

    @Override
    public boolean listen() {
        return false;
    }
}
