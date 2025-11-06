package net.cicada.module.impl.render;

import net.cicada.module.api.Category;
import net.cicada.module.api.Module;
import net.cicada.module.api.ModuleInfo;

@ModuleInfo(name = "NoHurtCam", category = Category.Render, state = true)
public class NoHurtCam extends Module
{
    @Override
    public boolean listen() {
        return false;
    }
}
