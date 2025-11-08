package net.cicada.module.impl.world;

import net.cicada.event.api.Event;
import net.cicada.event.impl.UpdateEvent;
import net.cicada.module.api.Category;
import net.cicada.module.api.Module;
import net.cicada.module.api.ModuleInfo;
import net.cicada.module.setting.impl.BooleanSetting;
import net.cicada.module.setting.impl.NumberSetting;
import org.lwjgl.input.Keyboard;

@ModuleInfo(name = "FastBreak", category = Category.World)
public class FastBreak extends Module {
    NumberSetting breakDamage = new NumberSetting("BreakDamage", 0, 0, 1, 0.01, () -> true, this);
    BooleanSetting noBreakDelay = new BooleanSetting("NoBreakDelay", false, () -> true, this);

    @Override
    public void listen(Event event) {
        if (event instanceof UpdateEvent) {
            if (noBreakDelay.isValue()) mc.playerController.blockHitDelay = -99999999;
            if (mc.playerController.curBlockDamageMP >= breakDamage.getValue()) mc.playerController.curBlockDamageMP = 1;
        }
    }
}
