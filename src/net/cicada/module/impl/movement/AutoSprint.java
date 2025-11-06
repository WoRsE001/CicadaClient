package net.cicada.module.impl.movement;

import net.cicada.module.api.Category;
import net.cicada.module.api.Module;
import net.cicada.module.api.ModuleInfo;

@ModuleInfo(name = "AutoSprint", category = Category.Movement, state = true)
public class AutoSprint extends Module {
    @Override
    public boolean listen() {
        return false;
    }
}
