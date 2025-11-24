package net.cicada.module.impl.fun;

import net.cicada.event.api.Event;
import net.cicada.event.impl.MotionEvent;
import net.cicada.module.api.Category;
import net.cicada.module.api.Module;
import net.cicada.module.api.ModuleInfo;

@ModuleInfo(name = "Spin", category = Category.Fun)
public class Spin extends Module {
    int count = 0;

    @Override
    public void listen(Event event) {
        if (event instanceof MotionEvent e && e.getPriority() == Event.Priority.High) {
            mc.thePlayer.rotationYawHead = count;
            mc.thePlayer.rotationPitchHead = 90;
            mc.thePlayer.renderYawOffset = count;
            count += 80;
        }
    }
}
