package net.cicada.module.impl.render;

import net.cicada.event.api.Event;
import net.cicada.event.impl.TickEvent;
import net.cicada.module.api.Category;
import net.cicada.module.api.Module;
import net.cicada.module.api.ModuleInfo;

@ModuleInfo(name = "FullBright", category = Category.Render, state = true)
public class FullBright extends Module {
    float lastBright = -1;

    @Override
    protected void onEnable() {
        lastBright = mc.gameSettings.saturation;
        super.onEnable();
    }

    @Override
    protected void onDisable() {
        mc.gameSettings.saturation = lastBright;
        super.onDisable();
    }

    @Override
    public void listen(Event event) {
        if (event instanceof TickEvent) {
            mc.gameSettings.saturation = 100;
        }
    }
}
