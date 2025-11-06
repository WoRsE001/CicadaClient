package net.cicada.module.impl.combat;

import net.cicada.module.setting.impl.ListSetting;
import net.minecraft.entity.EntityLivingBase;
import net.cicada.event.api.Event;
import net.cicada.event.impl.SprintEvent;
import net.cicada.event.impl.TickEvent;
import net.cicada.module.api.Category;
import net.cicada.module.api.Module;
import net.cicada.module.api.ModuleInfo;
import net.cicada.module.api.ModuleManager;
import net.cicada.module.setting.impl.NumberSetting;

import java.util.List;

@ModuleInfo(name = "MoreKB", category = Category.Combat, state = true)
public class MoreKB extends Module {
    ListSetting mode = new ListSetting("Mode", "legitFast", List.of("LegitFast"), () -> true, this);
    NumberSetting minStopSprintDelay = new NumberSetting("MinStopSprintDelay", 1, 0, 10, 1, () -> mode.is("LegitFast"), this);
    NumberSetting maxStopSprintDelay = new NumberSetting("MaxStopSprintDelay", 2, 0, 10, 1, () -> mode.is("LegitFast"), this);
    NumberSetting minStartSprintDelay = new NumberSetting("MinStartSprintDelay", 1, 0, 10, 1, () -> mode.is("LegitFast"), this);
    NumberSetting maxStartSprintDelay = new NumberSetting("MaxStartSprintDelay", 2, 0, 10, 1, () -> mode.is("LegitFast"), this);

    int tickSprint = 0, reset = 0;

    @Override
    public void listen(Event event) {
        if (event instanceof TickEvent) {
            EntityLivingBase target = ModuleManager.ATTACK_AURA.target;
            if (target != null && target.hurtTime == 10) {
                tickSprint = (int) Math.ceil(Math.random() * (maxStopSprintDelay.getValue() - minStopSprintDelay.getValue()) + minStopSprintDelay.getValue());
                reset = (int) Math.ceil(Math.random() * (maxStartSprintDelay.getValue() - minStartSprintDelay.getValue()) + minStartSprintDelay.getValue());
            }
        }

        if (tickSprint > 0) {
            if (event instanceof TickEvent) tickSprint--;
            return;
        }

        if (reset == 0) return;

        if (event instanceof SprintEvent) {
            if (mc.thePlayer.isSprinting()) {
                mc.thePlayer.setSprinting(false);
                reset--;
            }
        }
    }
}
