package net.cicada.module.impl.misc;

import net.cicada.module.api.Category;
import net.cicada.module.api.Module;
import net.cicada.module.api.ModuleInfo;
import net.cicada.module.setting.impl.BooleanSetting;
import net.cicada.module.setting.impl.MultiBooleanSetting;

@ModuleInfo(name = "Targets", category = Category.Misc)
public class Targets extends Module {
    public MultiBooleanSetting typeTargets = new MultiBooleanSetting("TypeTargets", () -> true, this)
            .add("Players", true)
            .add("Animals", false)
            .add("Mobs", false);
    public BooleanSetting teams = new BooleanSetting("Teams",  false, () -> true, this);
    public MultiBooleanSetting antiBot = new MultiBooleanSetting("AntiBot", () -> true, this)
            .add("EntityID", false)
            .add("PlayerInfo", false)
            .add("ResponseTime", false);

    @Override
    protected void onEnable() {
        this.toggle();
        super.onEnable();
    }

    @Override
    public boolean listen() {
        return false;
    }
}
