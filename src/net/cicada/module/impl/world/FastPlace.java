package net.cicada.module.impl.world;

import net.cicada.event.impl.LegitClickTimingEvent;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.MovingObjectPosition;
import net.cicada.event.api.Event;
import net.cicada.event.impl.TickEvent;
import net.cicada.module.api.Category;
import net.cicada.module.api.Module;
import net.cicada.module.api.ModuleInfo;
import net.cicada.module.setting.impl.NumberSetting;

@ModuleInfo(name = "FastPlace", category = Category.World, state = true)
public class FastPlace extends Module {
    NumberSetting cps = new NumberSetting("CPS", 20, 0, 20, 1, () -> true, this);

    @Override
    public void listen(Event event) {
        if (event instanceof LegitClickTimingEvent) {
            if (mc.gameSettings.keyBindUseItem.isKeyDown() && Math.random() * 20 < this.cps.getValue()) {
                mc.rightClickMouse();
            }
        }
    }
}
