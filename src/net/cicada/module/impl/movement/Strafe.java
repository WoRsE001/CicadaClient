package net.cicada.module.impl.movement;

import net.cicada.event.api.Event;
import net.cicada.event.impl.UpdateEvent;
import net.cicada.module.api.Category;
import net.cicada.module.api.Module;
import net.cicada.module.api.ModuleInfo;
import net.cicada.utility.MovementUtil;

@ModuleInfo(name = "Strafe", category = Category.Movement)
public class Strafe extends Module {
    @Override
    public void listen(Event event) {
        if (event instanceof UpdateEvent) {
            MovementUtil.strafe(Math.hypot(mc.thePlayer.motionX, mc.thePlayer.motionZ), 1);
        }
    }
}
