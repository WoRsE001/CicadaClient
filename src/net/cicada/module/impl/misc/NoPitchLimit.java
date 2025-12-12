package net.cicada.module.impl.misc;

import net.cicada.module.api.Category;
import net.cicada.module.api.Module;
import net.cicada.module.api.ModuleInfo;

@ModuleInfo(name = "NoPitchLimit", category = Category.Misc)
public class NoPitchLimit extends Module {
    @Override
    public boolean listen() {
        return false;
    }
}
