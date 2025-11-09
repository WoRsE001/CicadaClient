package net.cicada.module.impl.movement;

import net.cicada.module.api.Category;
import net.cicada.module.api.Module;
import net.cicada.module.api.ModuleInfo;
import net.cicada.module.setting.impl.BooleanSetting;

@ModuleInfo(name = "AutoSprint", category = Category.Movement, state = true)
public class AutoSprint extends Module {
    public BooleanSetting allDirection = new BooleanSetting("AllDirection", false, () -> true, this);

    @Override
    public boolean listen() {
        return false;
    }
}
