package net.cicada.module.impl.combat;

import net.cicada.event.impl.GameLoopEvent;
import net.cicada.module.setting.impl.BooleanSetting;
import net.cicada.utility.MathUtil;
import net.minecraft.util.MovingObjectPosition;
import net.cicada.event.api.Event;
import net.cicada.event.impl.LegitClickTimingEvent;
import net.cicada.module.api.Category;
import net.cicada.module.api.Module;
import net.cicada.module.api.ModuleInfo;
import net.cicada.module.setting.impl.NumberSetting;

@ModuleInfo(name = "AutoClicker", category = Category.Combat)
public class AutoClicker extends Module {
    NumberSetting minCPS = new NumberSetting("MinCPS", 20, 0, 20, 1, () -> true, this);
    NumberSetting maxCPS = new NumberSetting("MaxCPS", 20, 0, 20, 1, () -> true, this);
    NumberSetting minRecalculateDelay = new NumberSetting("MinRecalculateDelay", 0, 0, 1000, 1, () -> true, this);
    NumberSetting maxRecalculateDelay = new NumberSetting("MaxRecalculateDelay", 0, 0, 1000, 1, () -> true, this);

    long timer;
    int CPS;

    @Override
    public void listen(Event event) {
        if (event instanceof GameLoopEvent) {
            if (System.currentTimeMillis() - timer > MathUtil.random(minRecalculateDelay.getValue(), maxRecalculateDelay.getValue())) {
                CPS = (int) MathUtil.random(minCPS.getValue(), maxCPS.getValue());
                timer = System.currentTimeMillis();
            }
        }

        if (event instanceof LegitClickTimingEvent) {
            if (mc.gameSettings.keyBindAttack.isKeyDown() && Math.random() * 20 < CPS && !mc.thePlayer.isUsingItem() && mc.objectMouseOver.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK) {
                mc.clickMouse();
            }
        }
    }
}
