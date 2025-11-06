package net.cicada.module.impl.render;

import net.cicada.event.api.Event;
import net.cicada.event.impl.Render3DEvent;
import net.cicada.module.api.Category;
import net.cicada.module.api.Module;
import net.cicada.module.api.ModuleInfo;
import net.cicada.utility.RenderUtil;
import net.cicada.utility.SimulatedPlayer;

import java.awt.*;

@ModuleInfo(name = "MovementPredict", category = Category.Render)
public class MovementPredict extends Module {
    SimulatedPlayer simulatedPlayer;

    @Override
    public void listen(Event event) {
        if (event instanceof Render3DEvent) {
            simulatedPlayer = SimulatedPlayer.fromClientPlayer(mc.thePlayer.movementInput, mc.thePlayer.rotationYaw);
            simulatedPlayer.tick(20);
            RenderUtil.start3D();
            if (mc.gameSettings.thirdPersonView > 0) RenderUtil.render3DEntityBox(simulatedPlayer.player, true, false, new Color(1F, 1F, 1F, 0.3F));
            RenderUtil.stop3D();
        }
    }
}
