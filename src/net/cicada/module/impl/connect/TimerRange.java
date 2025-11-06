package net.cicada.module.impl.connect;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.Vec3;
import net.cicada.event.api.Event;
import net.cicada.event.impl.TickEvent;
import net.cicada.module.api.Category;
import net.cicada.module.api.Module;
import net.cicada.module.api.ModuleInfo;
import net.cicada.module.api.ModuleManager;
import net.cicada.module.setting.impl.NumberSetting;
import net.cicada.utility.SimulatedPlayer;

import java.io.IOException;

@ModuleInfo(name = "TimerRange", category = Category.Connect)
public class TimerRange extends Module {
    NumberSetting maxTPTicks = new NumberSetting("MaxTPTicks", 3, 1, 10, 1, () -> true, this);

    EntityLivingBase target;
    int balance;
    boolean teleport;

    @Override
    public void listen(Event event) {
        if (teleport) return;
        if (event instanceof TickEvent e && ModuleManager.ATTACK_AURA.isState()) {
            target = ModuleManager.ATTACK_AURA.target;
            if (balance > 0) {
                e.cancel();
                balance--;
                return;
            }
            if (target == null || target.hurtTime > 0) return;
            if (mc.thePlayer.getPositionVector().distanceTo(target.getPositionVector()) < 3) return;
            SimulatedPlayer simulatedPlayer = SimulatedPlayer.fromClientPlayer(mc.thePlayer.movementInput, mc.thePlayer.rotationYaw);
            int teleportTicks = 0;
            for (int i = 0; i < maxTPTicks.getValue(); i++) {
                Vec3 pos = target.getPositionVector();
                boolean flag = simulatedPlayer.getPos().distanceTo(pos) > 3;
                if (flag) {
                    simulatedPlayer.tick(1);
                } else {
                    teleportTicks = i;
                }
            }
            teleport = true;
            for (int i = 0; i < teleportTicks; i++) {
                try {
                    mc.runTick();
                    balance++;
                } catch (IOException ignored) {
                }
            }
            teleport = false;
        }
    }
}
