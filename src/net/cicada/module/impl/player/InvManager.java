package net.cicada.module.impl.player;

import lombok.Getter;
import net.cicada.event.impl.UpdateEvent;
import net.cicada.module.setting.impl.ListSetting;
import net.cicada.utility.LoggerUtil;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.cicada.event.api.Event;
import net.cicada.event.impl.TickEvent;
import net.cicada.module.api.Category;
import net.cicada.module.api.Module;
import net.cicada.module.api.ModuleInfo;
import net.cicada.utility.InvUtil;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

@ModuleInfo(name = "InvManager", category = Category.Player)
public class InvManager extends Module {
    ListSetting slot1 = new ListSetting("Slot1", "Sword", List.of("None", "Sword", "FishingRod", "Bow", "WaterBucket", "Food", "Gapple", "EnderPearl", "Egg", "Block", "Pickaxe", "Axe", "Shovel"), () -> true, this);
    ListSetting slot2 = new ListSetting("Slot2", "FishingRod", List.of("None", "Sword", "FishingRod", "Bow", "WaterBucket", "Food", "Gapple", "EnderPearl", "Egg", "Block", "Pickaxe", "Axe", "Shovel"), () -> true, this);
    ListSetting slot3 = new ListSetting("Slot3", "Bow", List.of("None", "Sword", "FishingRod", "Bow", "WaterBucket", "Food", "Gapple", "EnderPearl", "Egg", "Block", "Pickaxe", "Axe", "Shovel"), () -> true, this);
    ListSetting slot4 = new ListSetting("Slot4", "WaterBucket", List.of("None", "Sword", "FishingRod", "Bow", "WaterBucket", "Food", "Gapple", "EnderPearl", "Egg", "Block", "Pickaxe", "Axe", "Shovel"), () -> true, this);
    ListSetting slot5 = new ListSetting("Slot5", "Food", List.of("None", "Sword", "FishingRod", "Bow", "WaterBucket", "Food", "Gapple", "EnderPearl", "Egg", "Block", "Pickaxe", "Axe", "Shovel"), () -> true, this);
    ListSetting slot6 = new ListSetting("Slot6", "Gapple", List.of("None", "Sword", "FishingRod", "Bow", "WaterBucket", "Food", "Gapple", "EnderPearl", "Egg", "Block", "Pickaxe", "Axe", "Shovel"), () -> true, this);
    ListSetting slot7 = new ListSetting("Slot7", "EnderPearl", List.of("None", "Sword", "FishingRod", "Bow", "WaterBucket", "Food", "Gapple", "EnderPearl", "Egg", "Block", "Pickaxe", "Axe", "Shovel"), () -> true, this);
    ListSetting slot8 = new ListSetting("Slot8", "Egg", List.of("None", "Sword", "FishingRod", "Bow", "WaterBucket", "Food", "Gapple", "EnderPearl", "Egg", "Block", "Pickaxe", "Axe", "Shovel"), () -> true, this);
    ListSetting slot9 = new ListSetting("Slot9", "Block", List.of("None", "Sword", "FishingRod", "Bow", "WaterBucket", "Food", "Gapple", "EnderPearl", "Egg", "Block", "Pickaxe", "Axe", "Shovel"), () -> true, this);

    @Override
    public void listen(Event event) {
        if (event instanceof UpdateEvent && mc.currentScreen instanceof GuiInventory) {
            if (this.searchAndSort()) return;
            //if (drop()) return;
        }
    }

    private boolean searchAndSort() {
        List<String> items = List.of(this.slot1.getValue(), this.slot2.getValue(), this.slot3.getValue(), this.slot4.getValue(), this.slot5.getValue(), this.slot6.getValue(), this.slot7.getValue(), this.slot8.getValue(), this.slot9.getValue());
        for (int i = 0; i < items.size(); i++) {
            ItemCategory category = ItemCategory.get(items.get(i));
            for (int j = 0; j < mc.thePlayer.inventory.getSizeInventory(); j++) {
                if (mc.thePlayer.inventory.getStackInSlot(j) == null) continue;
                ItemStack currentItemStack = mc.thePlayer.inventory.getStackInSlot(j);
                if (category.is(currentItemStack) && i != j) {
                    InvUtil.moveItem(j, i - 36);
                    return true;
                }
            }
        }
        return false;
    }

    private boolean drop() {
        for (int i = 9; i < 36; i++) {
            ItemStack itemStack = getItemStack(i);
            if (itemStack == null) continue;
            Item item = itemStack.getItem();

            if (item instanceof ItemSword || item instanceof ItemFishingRod || item instanceof ItemBow || item == Items.water_bucket) {
                InvUtil.dropItem(i);
                return true;
            }
        }
        return false;
    }

    private ItemStack getItemStack(int slot) {
        return mc.thePlayer.inventory.getStackInSlot(slot);
    }

    @Getter
    private enum ItemCategory {
        SWORD("Sword", item -> item instanceof ItemSword),
        FISHING_ROD("FishingRod", item -> item instanceof ItemFishingRod),
        BOW("Bow", item -> item instanceof ItemBow),
        WATER_BUCKET("WaterBucket", item -> item == Items.water_bucket),
        FOOD("Food", item -> item instanceof ItemFood),
        GOLDEN_APPLE("GoldenApple", item -> item instanceof ItemAppleGold),
        ENDER_PEARL("EnderPearl", item -> item instanceof ItemEnderPearl),
        EGG("Egg", item -> item instanceof ItemEgg),
        BLOCK("Block", item -> item instanceof ItemBlock),
        PICKAXE("Pickaxe", item -> item instanceof ItemPickaxe),
        AXE("Axe", item -> item instanceof ItemAxe),
        SHOVEL("Shovel", item -> item instanceof ItemSpade),
        EMPTY("None", item -> false);

        private final String displayName;
        private final Predicate<Item> matching;

        ItemCategory(String displayName, Predicate<Item> matching) {
            this.displayName = displayName;
            this.matching = matching;
        }

        public static ItemCategory get(String displayName) {
            for (ItemCategory category : values()) {
                if (category.getDisplayName().equalsIgnoreCase(displayName)) {
                    return category;
                }
            }
            return EMPTY;
        }

        public boolean is(ItemStack itemStack) {
            if (itemStack == null) return this == EMPTY;

            if (this == EMPTY) {
                return false;
            }
            return matching.test(itemStack.getItem());
        }
    }
}
