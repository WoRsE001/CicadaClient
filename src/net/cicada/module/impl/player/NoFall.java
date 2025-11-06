package net.cicada.module.impl.player;

import net.cicada.event.api.Event;
import net.cicada.event.impl.MotionEvent;
import net.cicada.module.api.Category;
import net.cicada.module.api.Module;
import net.cicada.module.api.ModuleInfo;
import net.cicada.module.setting.impl.BooleanSetting;

@ModuleInfo(name = "NoFall", category = Category.Player)
public class NoFall extends Module {
    BooleanSetting onGround = new BooleanSetting("onGround", true, () -> true, this);

    @Override
    public void listen(Event event) {
        if (event instanceof MotionEvent e) {
            e.setOnGround(onGround.isValue());
        }
    }
}
