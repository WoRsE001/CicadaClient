package net.cicada.module.impl.render;

import net.cicada.event.api.Event;
import net.cicada.event.impl.Render2DEvent;
import net.cicada.module.api.Category;
import net.cicada.module.api.Module;
import net.cicada.module.api.ModuleInfo;

@ModuleInfo(name = "BPS", category = Category.Render, state = true)
public class BPS extends Module {
    @Override
    public void listen(Event event) {
        if (event instanceof Render2DEvent) {
            mc.fontRendererObj.drawString(String.valueOf(Math.hypot(mc.thePlayer.motionX, mc.thePlayer.motionZ) * 20), 3, 13, -1);
        }
    }
}
