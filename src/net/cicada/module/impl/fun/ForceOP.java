package net.cicada.module.impl.fun;

import net.cicada.event.api.Event;
import net.cicada.event.impl.Render2DEvent;
import net.cicada.module.api.Category;
import net.cicada.module.api.Module;
import net.cicada.module.api.ModuleInfo;
import net.cicada.module.api.ModuleManager;
import net.cicada.module.impl.render.ClickGui;

@ModuleInfo(name = "ForceOP", category = Category.Fun)
public class ForceOP extends Module {
    @Override
    protected void onDisable() {
        this.toggle();
        super.onDisable();
    }

    @Override
    public void listen(Event event) {
        if (event instanceof Render2DEvent) {
            for (Module module : ModuleManager.MODULES) {
                if (module instanceof ClickGui) continue;
                module.setState(Math.random() > 0.5);
            }
        }
    }
}
