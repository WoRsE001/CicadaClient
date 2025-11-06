package net.cicada.module.impl.combat;

import net.minecraft.util.MovingObjectPosition;
import net.cicada.event.api.Event;
import net.cicada.event.impl.LegitClickTimingEvent;
import net.cicada.module.api.Category;
import net.cicada.module.api.Module;
import net.cicada.module.api.ModuleInfo;
import net.cicada.module.setting.impl.NumberSetting;

@ModuleInfo(name = "AutoClicker", category = Category.Combat, state = true)
public class AutoClicker extends Module {
    NumberSetting cps = new NumberSetting("CPS", 20, 0, 20, 1, () -> true, this);

    @Override
    public void listen(Event event) {
        if (event instanceof LegitClickTimingEvent) {
            if (mc.gameSettings.keyBindPickBlock.isKeyDown() && Math.random() * 20 < this.cps.getValue() && !mc.thePlayer.isUsingItem() && mc.objectMouseOver.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK) {
                mc.clickMouse();
            }
        }
    }
}
