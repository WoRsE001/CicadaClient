package net.cicada.module.impl.render;

import net.cicada.event.api.Event;
import net.cicada.event.impl.Render3DEvent;
import net.cicada.module.api.Category;
import net.cicada.module.api.Module;
import net.cicada.module.api.ModuleInfo;
import net.cicada.module.api.ModuleManager;
import net.cicada.utility.Render.RenderUtil;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;

import java.awt.*;

@ModuleInfo(name = "TargetESP", category = Category.Render)
public class TargetESP extends Module {
    float counter = 0;

    @Override
    public void listen(Event event) {
        if (event instanceof Render3DEvent e && e.getPriority() == Event.Priority.High) {
            if (ModuleManager.ATTACK_AURA.target != null) {
                Vec3 targetPos = ModuleManager.ATTACK_AURA.target.getPositionVector();
                float targetHeight = ModuleManager.ATTACK_AURA.target.height;
                Vec3 targetESPPos = new Vec3(targetPos.xCoord, targetPos.yCoord + (Math.sin(Math.toRadians(counter)) + 1) * (targetHeight / 2), targetPos.zCoord);
                RenderUtil.setGlColor(new Color(255, 255, 255, 255));
                RenderUtil.render3DBox(new AxisAlignedBB(-0.5, -0.01, -0.5, 0.5, 0.01, 0.5).offset(targetESPPos.xCoord, targetESPPos.yCoord, targetESPPos.zCoord));
                counter += 2;
            }
        }
    }
}
