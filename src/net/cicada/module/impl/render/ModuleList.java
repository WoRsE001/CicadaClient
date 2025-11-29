package net.cicada.module.impl.render;

import net.cicada.event.api.Event;
import net.cicada.event.impl.Render2DEvent;
import net.cicada.event.impl.TickEvent;
import net.cicada.module.api.Category;
import net.cicada.module.api.Module;
import net.cicada.module.api.ModuleInfo;
import net.cicada.module.api.ModuleManager;
import net.cicada.module.setting.impl.ListSetting;
import net.cicada.module.setting.impl.NumberSetting;
import net.cicada.utility.Render.RenderUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@ModuleInfo(name = "ModuleList", category = Category.Render)
public class ModuleList extends Module {
    NumberSetting posX = new NumberSetting("PosX", 1, 0, 1, 0.01, () -> true, this);
    NumberSetting posY = new NumberSetting("PosY", 0, 0, 1, 0.01, () -> true, this);
    ListSetting xOrient = new ListSetting("XOrient", "Right", List.of("Left", "Right"), () -> true, this);
    ListSetting yOrient = new ListSetting("YOrient", "Top", List.of("Top", "Bottom"), () -> true, this);
    NumberSetting width = new NumberSetting("Width", 2, 0, 10, 0.1, () -> true, this);
    NumberSetting gaps = new NumberSetting("Gaps", 2, 0, 10, 0.1, () -> true, this);

    List<Module> modules = new ArrayList<>();

    @Override
    public void listen(Event event) {
        if (event instanceof TickEvent) {
            for (Module module : ModuleManager.MODULES) {
                if (!this.modules.contains(module)) this.modules.add(module);
                if (!module.isState()) this.modules.remove(module);
            }
            this.modules.sort(Comparator.comparingInt(m -> (this.yOrient.is("Top") ? -1 : 1) * mc.fontRendererObj.getStringWidth(m.getName())));
        }

        if (event instanceof Render2DEvent) {
            float offsetY = 0;
            for (Module module : this.modules) {
                RenderUtil.setGlColor(new Color(0, 0, 0));
                float posX = (float) (this.xOrient.is("Left") ? mc.displayWidth / 2F * this.posX.getValue() : mc.displayWidth / 2F * this.posX.getValue() - mc.fontRendererObj.getStringWidth(module.getName()) - this.width.getValue() * 2);
                float posY = (float) (this.yOrient.is("Top") ? mc.displayHeight / 2F * this.posY.getValue() + offsetY : mc.displayHeight / 2F * this.posY.getValue() + offsetY - (mc.fontRendererObj.FONT_HEIGHT + (float) this.gaps.getValue()) * this.modules.size());
                RenderUtil.render2DRect(posX, posY, mc.fontRendererObj.getStringWidth(module.getName()) + this.width.getValue() * 2, mc.fontRendererObj.FONT_HEIGHT + (float) this.gaps.getValue());
                mc.fontRendererObj.drawString(module.getName(), posX + (float) this.width.getValue(), posY + (float) this.gaps.getValue() / 2, -1);
                offsetY += mc.fontRendererObj.FONT_HEIGHT + (float) this.gaps.getValue();
            }
        }
    }
}
