package net.cicada.module.impl.render;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.cicada.event.api.Event;
import net.cicada.event.impl.Render3DEvent;
import net.cicada.module.api.Category;
import net.cicada.module.api.Module;
import net.cicada.module.api.ModuleInfo;
import net.cicada.module.setting.impl.BooleanSetting;
import net.cicada.utility.RenderUtil;

import java.awt.*;

@ModuleInfo(name = "Esp", category = Category.Render)
public class ESP extends Module {
    @Override
    public void listen(Event event) {
        if (event instanceof Render3DEvent e && e.getPriority() == Event.Priority.High) {
            for (Entity entity : mc.theWorld.getLoadedEntityList()) {
                if (entity == mc.thePlayer || !(entity instanceof EntityPlayer)) continue;
                RenderUtil.start3D();
                RenderUtil.setGlColor(new Color(0, 0, 0, 128));
                RenderUtil.render3DEntityBox(entity);
                RenderUtil.stop3D();
            }
        }
    }
}
