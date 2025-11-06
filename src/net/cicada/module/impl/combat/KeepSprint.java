package net.cicada.module.impl.combat;

import net.cicada.module.api.Category;
import net.cicada.module.api.Module;
import net.cicada.module.api.ModuleInfo;
import net.cicada.module.setting.impl.BooleanSetting;
import net.cicada.module.setting.impl.NumberSetting;

@ModuleInfo(name = "KeepSprint", category = Category.Combat)
public class KeepSprint extends Module {
    public NumberSetting slowDownOnGround = new NumberSetting("SlowDownOnGround", 1, 0.6, 1, 0.01, () -> true, this);
    public BooleanSetting sprintOnGround = new BooleanSetting("sprintOnGround", true, () -> true, this);
    public NumberSetting slowDownInAir = new NumberSetting("SlowDownInAir", 1, 0.6, 1, 0.01, () -> true, this);
    public BooleanSetting sprintInAir = new BooleanSetting("SprintInAir", true, () -> true, this);

    @Override
    public boolean listen() {
        return false;
    }
}
