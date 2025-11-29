package net.cicada.module.impl.render;

import net.cicada.Cicada;
import net.cicada.event.api.Event;
import net.cicada.event.impl.Render2DEvent;
import net.cicada.module.api.Category;
import net.cicada.module.api.Module;
import net.cicada.module.api.ModuleInfo;
import net.cicada.module.setting.impl.ListSetting;
import net.cicada.module.setting.impl.NumberSetting;
import net.cicada.utility.LoggerUtil;
import net.cicada.utility.Render.RenderUtil;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.Display;

import java.util.List;

@ModuleInfo(name = "WaterMark", category = Category.Render, state = true)
public class WaterMark extends Module {
    ListSetting mode = new ListSetting("Mode", "Cicada3301", List.of("Text", "Cicada3301", "Mellstroy.gay"), () -> true, this);
    NumberSetting posX = new NumberSetting("PosX", 0, 0, 1, 0.01, () -> true, this);
    NumberSetting posY = new NumberSetting("PosY", 0, 0, 1, 0.01, () -> true, this);
    NumberSetting width = new NumberSetting("Width", 0.11, 0, 1, 0.01, () -> this.mode.is("Cicada3301"), this);
    NumberSetting height = new NumberSetting("Height", 0.11, 0, 1, 0.01, () -> this.mode.is("Cicada3301"), this);

    @Override
    public void listen(Event event) {
        if (event instanceof Render2DEvent) {
            ScaledResolution sr = new ScaledResolution(mc);
            if (this.mode.is("Text")) {
                mc.fontRendererObj.drawString(Cicada.name + " v" + Cicada.version, (float) this.posX.getValue() * sr.getScaledWidth(), (float) this.posY.getValue() * sr.getScaledHeight(), -1);
            } else if (this.mode.is("Cicada3301")) {
                RenderUtil.drawImage(new ResourceLocation("cicada/images/Watermark.png"), (float) this.posX.getValue() * sr.getScaledWidth(), (float) this.posY.getValue() * sr.getScaledHeight(), (int) (this.width.getValue() * sr.getScaledWidth()), (int) (this.height.getValue() * sr.getScaledHeight()));
            } else if (this.mode.is("Mellstroy.gay")) {
                mc.fontRendererObj.drawString("Mellstroy.game", (float) this.posX.getValue() * sr.getScaledWidth(), (float) this.posY.getValue() * sr.getScaledHeight(), -1);
            }
        }
    }
}
