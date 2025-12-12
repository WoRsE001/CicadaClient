package net.cicada.module.impl.world;

import net.cicada.event.impl.LegitClickTimingEvent;
import net.cicada.module.setting.impl.BooleanSetting;
import net.minecraft.item.ItemBlock;
import net.cicada.event.api.Event;
import net.cicada.module.api.Category;
import net.cicada.module.api.Module;
import net.cicada.module.api.ModuleInfo;
import net.cicada.module.setting.impl.NumberSetting;

@ModuleInfo(name = "FastPlace", category = Category.World)
public class FastPlace extends Module {
    NumberSetting cps = new NumberSetting("CPS", 20, 0, 20, 1, () -> true, this);
    BooleanSetting onlyBlock = new BooleanSetting("OnlyBlock", true, () -> true, this);

    @Override
    public void listen(Event event) {
        if (event instanceof LegitClickTimingEvent) {
            if (mc.gameSettings.keyBindUseItem.isKeyDown() && Math.random() * 20 < this.cps.getValue() && (!onlyBlock.isValue() || mc.thePlayer.inventory.getCurrentItem() != null && mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemBlock)) {
                mc.rightClickMouse();
            }
        }
    }
}
