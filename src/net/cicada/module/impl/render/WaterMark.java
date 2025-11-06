package net.cicada.module.impl.render;

import net.cicada.Cicada;
import net.cicada.event.api.Event;
import net.cicada.event.impl.Render2DEvent;
import net.cicada.module.api.Category;
import net.cicada.module.api.Module;
import net.cicada.module.api.ModuleInfo;

@ModuleInfo(name = "WaterMark", category = Category.Render, state = true)
public class WaterMark extends Module {
    @Override
    public void listen(Event event) {
        if (event instanceof Render2DEvent) {
            mc.fontRendererObj.drawString(Cicada.name + " v" + Cicada.version, 3, 3, -1);
        }
    }
}
