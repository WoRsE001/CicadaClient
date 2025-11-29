package net.cicada.module.impl.render;

import net.cicada.module.setting.impl.MultiBooleanSetting;
import net.cicada.module.setting.impl.NumberSetting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.cicada.event.api.Event;
import net.cicada.event.impl.Render3DEvent;
import net.cicada.module.api.Category;
import net.cicada.module.api.Module;
import net.cicada.module.api.ModuleInfo;
import net.cicada.utility.Render.RenderUtil;

import java.awt.*;

@ModuleInfo(name = "Esp", category = Category.Render)
public class ESP extends Module {
    public MultiBooleanSetting mode = new MultiBooleanSetting("Mode", () -> true, this)
            .add("Box", false)
            .add("Chams", false)
            .add("Glow", false)
            .add("HitBox", true);
    NumberSetting expandBox = new NumberSetting("expandBox", 0.1, 0, 1, 0.01, () -> mode.is("Box"), this);
    NumberSetting expandHitBox = new NumberSetting("expandHitBox", 0.1, 0, 1, 0.01, () -> mode.is("HitBox"), this);
    NumberSetting lineWidth = new NumberSetting("LineWidth", 3, 0, 10, 1, () -> mode.is("HitBox"), this);

    @Override
    public void listen(Event event) {
        if (event instanceof Render3DEvent e && e.getPriority() == Event.Priority.High) {
            for (Entity entity : mc.theWorld.getLoadedEntityList()) {
                if (entity == mc.thePlayer || !(entity instanceof EntityPlayer)) continue;
                RenderUtil.setGlColor(new Color(0, 0, 0, 128));
                if (this.mode.is("Box")) RenderUtil.render3DEntityBox(entity, (float) expandBox.getValue());
                if (this.mode.is("Glow")) {}
                RenderUtil.setGlColor(new Color(0, 0, 0, 128));
                if (this.mode.is("HitBox")) RenderUtil.render3DEntityHitBox(entity, (float) expandHitBox.getValue(), (float) lineWidth.getValue());
            }
        }
    }
}
