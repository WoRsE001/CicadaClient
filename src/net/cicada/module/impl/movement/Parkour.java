package net.cicada.module.impl.movement;

import net.cicada.event.api.Event;
import net.cicada.event.impl.MovementEvent;
import net.cicada.event.impl.MovementInputEvent;
import net.cicada.event.impl.UpdateEvent;
import net.cicada.module.api.Category;
import net.cicada.module.api.Module;
import net.cicada.module.api.ModuleInfo;
import net.cicada.utility.SimulatedPlayer;

@ModuleInfo(name = "Parkour", category = Category.Movement)
public class Parkour extends Module {
    boolean jump;

    @Override
    public void listen(Event event) {
        if (event instanceof UpdateEvent) {
            SimulatedPlayer simulatedPlayer = SimulatedPlayer.fromClientPlayer(mc.thePlayer.movementInput, mc.thePlayer.rotationYaw);
            simulatedPlayer.tick(1);
            if (!simulatedPlayer.onGround) {
                jump = true;
            }
        }
        if (event instanceof MovementInputEvent e && jump) {
            e.setJump(true);
            jump = false;
        }
    }
}
