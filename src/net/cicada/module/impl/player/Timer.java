package net.cicada.module.impl.player;

import net.cicada.event.api.Event;
import net.cicada.event.impl.TickEvent;
import net.cicada.module.api.Category;
import net.cicada.module.api.Module;
import net.cicada.module.api.ModuleInfo;
import net.cicada.module.setting.impl.NumberSetting;

@ModuleInfo(name = "Timer", category = Category.Player)
public class Timer extends Module {
    NumberSetting speed = new NumberSetting("Speed", 1, 0, 10, 0.01, () -> true, this);

    @Override
    protected void onDisable() {
        mc.timer.setTimer(1);
        super.onDisable();
    }

    @Override
    public void listen(Event event) {
        if (event instanceof TickEvent) {
            mc.timer.setTimer((float) this.speed.getValue());
        }
    }
}
