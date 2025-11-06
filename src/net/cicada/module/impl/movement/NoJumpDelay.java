package net.cicada.module.impl.movement;

import net.cicada.module.api.Category;
import net.cicada.module.api.Module;
import net.cicada.module.api.ModuleInfo;

@ModuleInfo(name = "NoJumpDelay", category = Category.Movement, state = true)
public class NoJumpDelay extends Module {
    @Override
    public boolean listen() {
        return false;
    }
}
