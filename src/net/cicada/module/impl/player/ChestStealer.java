package net.cicada.module.impl.player;

import net.cicada.module.setting.impl.BooleanSetting;
import net.cicada.module.setting.impl.ListSetting;
import net.cicada.utility.MathUtil;
import net.minecraft.inventory.ContainerChest;
import net.cicada.event.api.Event;
import net.cicada.event.impl.TickEvent;
import net.cicada.module.api.Category;
import net.cicada.module.api.Module;
import net.cicada.module.api.ModuleInfo;
import net.cicada.module.setting.impl.NumberSetting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ModuleInfo(name = "ChestStealer", category = Category.Player)
public class ChestStealer extends Module {
    NumberSetting minStartDelay = new NumberSetting("MinStartDelay", 4, 0, 20, 1, () -> true, this);
    NumberSetting maxStartDelay = new NumberSetting("MaxStartDelay", 6, 0, 20, 1, () -> true, this);
    NumberSetting minDelay = new NumberSetting("MinDelay", 2, 0, 20, 1, () -> true, this);
    NumberSetting maxDelay = new NumberSetting("MaxDelay", 3, 0, 20, 1, () -> true, this);
    ListSetting randomize = new ListSetting("Randomize", "None", List.of("None", "Basic"), () -> true, this);

    byte tick;

    @Override
    protected void onDisable() {
        this.tick = 0;
        super.onDisable();
    }

    @Override
    public void listen(Event event) {
        if (event instanceof TickEvent) {
            if (mc.thePlayer.openContainer instanceof ContainerChest chest && chest.getLowerChestInventory().getName().equals("Chest")) {
                if (this.tick > 0) this.tick--;
                else {
                    List<Integer> itemsPos = new ArrayList<>();
                    for (int i = 0; i < chest.inventorySlots.size() - 36; i++) {
                        if (chest.getSlot(i).getStack() != null) itemsPos.add(i);
                    }
                    if (this.randomize.is("Basic")) Collections.shuffle(itemsPos);
                    for (Integer itemPos : itemsPos) {
                        mc.playerController.windowClick(chest.windowId, itemPos, 0, 1, mc.thePlayer);
                        this.tick = (byte) MathUtil.random(this.minDelay.getValue(), this.maxDelay.getValue());
                        if (this.tick != 0) return;
                    }
                }
            } else {
                this.tick = (byte) MathUtil.random(this.minStartDelay.getValue(), this.maxStartDelay.getValue());
            }
        }
    }
}
