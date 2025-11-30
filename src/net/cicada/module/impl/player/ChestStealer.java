package net.cicada.module.impl.player;

import net.minecraft.inventory.ContainerChest;
import net.cicada.event.api.Event;
import net.cicada.event.impl.TickEvent;
import net.cicada.module.api.Category;
import net.cicada.module.api.Module;
import net.cicada.module.api.ModuleInfo;
import net.cicada.module.setting.impl.NumberSetting;
import net.cicada.utility.Player.InvUtil;

@ModuleInfo(name = "ChestStealer", category = Category.Player)
public class ChestStealer extends Module {
    NumberSetting delay = new NumberSetting("Delay", 1, 0, 20, 1, () -> true, this);

    int tick;

    @Override
    protected void onDisable() {
        this.tick = 0;
        super.onDisable();
    }

    @Override
    public void listen(Event event) {
        if (event instanceof TickEvent) {
            if (mc.thePlayer.openContainer instanceof ContainerChest container && container.getLowerChestInventory().getName().equals("Chest")) {
                if (this.tick == 0) {
                    int containerSize = container.getLowerChestInventory().getSizeInventory();
                    for (int i = 0; i < containerSize; i++) {
                        if (container.getLowerChestInventory().getStackInSlot(i) == null) continue;
                        InvUtil.stealItem(i);
                        this.tick = (int) delay.getValue();
                        if (this.tick != 0) break;
                    }
                }
                if (this.tick > 0) this.tick--;
            }
        }
    }
}
