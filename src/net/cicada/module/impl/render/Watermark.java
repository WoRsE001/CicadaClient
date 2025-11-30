package net.cicada.module.impl.render;

import net.cicada.Cicada;
import net.cicada.event.api.Event;
import net.cicada.event.impl.Render2DEvent;
import net.cicada.module.api.Category;
import net.cicada.module.api.Module;
import net.cicada.module.api.ModuleInfo;
import net.cicada.module.setting.impl.ListSetting;
import net.cicada.module.setting.impl.NumberSetting;
import net.cicada.utility.Render.RenderUtil;
import net.minecraft.util.ResourceLocation;

import java.util.List;

@ModuleInfo(name = "Watermark", category = Category.Render, state = true)
public class Watermark extends Module {
    ListSetting mode = new ListSetting("Mode", "Cicada3301", List.of("Text", "Cicada3301", "Callstroy.gay"), () -> true, this);
    NumberSetting posX = new NumberSetting("PosX", 0, 0, 1, 0.01, () -> true, this);
    NumberSetting posY = new NumberSetting("PosY", 0, 0, 1, 0.01, () -> true, this);
    NumberSetting width = new NumberSetting("Width", 0.11, 0, 1, 0.01, () -> this.mode.is("Cicada3301"), this);
    NumberSetting height = new NumberSetting("Height", 0.11, 0, 1, 0.01, () -> this.mode.is("Cicada3301"), this);

    @Override
    public void listen(Event event) {
        if (event instanceof Render2DEvent) {
            if (this.mode.is("Text")) {
                mc.fontRendererObj.drawString(Cicada.name + " v" + Cicada.version, (mc.displayWidth / 2F - mc.fontRendererObj.getStringWidth(Cicada.name + " v" + Cicada.version)) * (float) this.posX.getValue(), (mc.displayHeight / 2F - mc.fontRendererObj.FONT_HEIGHT) * (float) this.posY.getValue(), -1);
            } else if (this.mode.is("Cicada3301")) {
                RenderUtil.drawImage(new ResourceLocation("cicada/images/Watermark.png"), (mc.displayWidth / 2F - mc.displayWidth / 2F * (float) this.width.getValue()) * (float) this.posX.getValue(), (mc.displayHeight / 2F - mc.displayHeight / 2F * (float) this.height.getValue()) * (float) this.posY.getValue(), (int) (mc.displayWidth / 2F * this.width.getValue()), (int) (mc.displayHeight / 2F * this.height.getValue()));
            } else if (this.mode.is("Callstroy.gay")) {
                mc.fontRendererObj.drawString("Mellstroy.game", (mc.displayWidth / 2F - mc.fontRendererObj.getStringWidth("Mellstroy.game")) * (float) this.posX.getValue(), (mc.displayHeight / 2F - mc.fontRendererObj.FONT_HEIGHT) * (float) this.posY.getValue(), -1);
            }
        }
    }
}
