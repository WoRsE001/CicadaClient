package net.cicada.module.impl.player;

import lombok.Getter;
import net.cicada.event.impl.UpdateEvent;
import net.cicada.module.setting.impl.BooleanSetting;
import net.cicada.module.setting.impl.ListSetting;
import net.cicada.module.setting.impl.NumberSetting;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.init.Items;
import net.minecraft.inventory.Slot;
import net.minecraft.item.*;
import net.cicada.event.api.Event;
import net.cicada.module.api.Category;
import net.cicada.module.api.Module;
import net.cicada.module.api.ModuleInfo;
import net.cicada.utility.Player.InvUtil;

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
    NumberSetting startDelay = new NumberSetting("StartDelay", 0, 0, 20, 1, () -> true, this);
    NumberSetting sortDelay = new NumberSetting("SortDelay", 0, 0, 20, 1, () -> true, this);
    NumberSetting autoArmorDelay = new NumberSetting("AutoArmorDelay", 0, 0, 20, 1, () -> true, this);
    NumberSetting dropDelay = new NumberSetting("DropDelay", 0, 0, 20, 1, () -> true, this);
    BooleanSetting autoClose = new BooleanSetting("AutoClose", true, () -> true, this);

    byte timer;

    @Override
    public void listen(Event event) {
        if (event instanceof UpdateEvent) {
            if (mc.currentScreen instanceof GuiInventory) {
                if (this.timer > 0) this.timer--;
                if (this.timer <= 0) {
                    if (searchAndSort()) timer = (byte) sortDelay.getValue();
                    if (autoArmor()) timer = (byte) autoArmorDelay.getValue();
                    if (drop()) timer = (byte) dropDelay.getValue();
                    if (autoClose.isValue() && timer <= 0) mc.thePlayer.closeScreen();
                }
            } else {
                this.timer = (byte) this.startDelay.getValue();
            }
        }
    }

    private boolean searchAndSort() {
        String[] items = {this.slot1.getValue(), this.slot2.getValue(), this.slot3.getValue(), this.slot4.getValue(), this.slot5.getValue(), this.slot6.getValue(), this.slot7.getValue(), this.slot8.getValue(), this.slot9.getValue()};
        for (int iterHotBar = 0; iterHotBar < 9; iterHotBar++) {
            ItemCategory hotBarCategory = ItemCategory.get(items[iterHotBar]);
            if (hotBarCategory == ItemCategory.EMPTY) continue;
            Slot bestSlot = null;
            if (mc.thePlayer.inventory.getStackInSlot(iterHotBar) != null && ItemCategory.get(mc.thePlayer.inventory.getStackInSlot(iterHotBar).getItem()) == hotBarCategory) bestSlot = mc.thePlayer.inventoryContainer.getSlot(iterHotBar + 36);
            for (Slot iterSlot : mc.thePlayer.inventoryContainer.inventorySlots) {
                ItemStack inventoryItemStack = iterSlot.getStack();
                if (inventoryItemStack == null) continue;
                ItemCategory inventoryCategory = ItemCategory.get(inventoryItemStack.getItem());
                if (inventoryCategory != hotBarCategory) continue;
                if (bestSlot == null) bestSlot = iterSlot;
                else if (inventoryItemStack.getItem() instanceof ItemSword) {
                    if (InvUtil.getSwordDamage(inventoryItemStack) > InvUtil.getSwordDamage(bestSlot.getStack())) bestSlot = iterSlot;
                } else if (inventoryItemStack.getItem() instanceof ItemFood food) {
                    if (food.getSaturationModifier(inventoryItemStack) > ((ItemFood) bestSlot.getStack().getItem()).getSaturationModifier(bestSlot.getStack())) bestSlot = iterSlot;
                } else if (inventoryItemStack.getItem() instanceof ItemBlock itemBlock && itemBlock.getBlock().isFullBlock()) {
                    if (inventoryItemStack.stackSize > bestSlot.getStack().stackSize) bestSlot = iterSlot;
                } else if (inventoryItemStack.getItem() instanceof ItemPickaxe || inventoryItemStack.getItem() instanceof ItemAxe || inventoryItemStack.getItem() instanceof ItemSpade) {
                    if (InvUtil.getMineSpeed(inventoryItemStack) > InvUtil.getMineSpeed(bestSlot.getStack())) bestSlot = iterSlot;
                }
            }
            if (bestSlot != null && bestSlot.slotNumber != iterHotBar + 36) {
                mc.playerController.windowClick(mc.thePlayer.openContainer.windowId, bestSlot.slotNumber, iterHotBar, 2, mc.thePlayer);
                if (this.sortDelay.getValue() != 0) return true;
            }
        }
        return false;
    }

    private boolean autoArmor() {
        for (int iterArmorBar = 5; iterArmorBar < 9; iterArmorBar++) {
            Slot bestSlot = null;
            if (mc.thePlayer.inventoryContainer.getSlot(iterArmorBar).getStack() != null) bestSlot = mc.thePlayer.inventoryContainer.getSlot(iterArmorBar);
            for (Slot iterSlot : mc.thePlayer.inventoryContainer.inventorySlots) {
                ItemStack inventoryItemStack = iterSlot.getStack();
                if (inventoryItemStack == null || !(inventoryItemStack.getItem() instanceof ItemArmor armor) || armor.armorType != iterArmorBar - 5) continue;
                if (bestSlot == null || bestSlot.getStack() == null) bestSlot = iterSlot;
                else if (InvUtil.getDamageReduceAmount(inventoryItemStack) > InvUtil.getDamageReduceAmount(bestSlot.getStack())) bestSlot = iterSlot;
            }
            if (bestSlot != null && bestSlot.slotNumber != iterArmorBar) {
                if (mc.thePlayer.inventoryContainer.getSlot(iterArmorBar).getStack() == null) mc.playerController.windowClick(mc.thePlayer.openContainer.windowId, bestSlot.slotNumber, 0, 1, mc.thePlayer);
                else {
                    if (InvUtil.isFull()) return !this.drop();
                    mc.playerController.windowClick(mc.thePlayer.openContainer.windowId, iterArmorBar, 0, 1, mc.thePlayer);
                }
                if (this.autoArmorDelay.getValue() != 0)  return true;
            }
        }
        return false;
    }

    private boolean drop() {
        String[] items = {this.slot1.getValue(), this.slot2.getValue(), this.slot3.getValue(), this.slot4.getValue(), this.slot5.getValue(), this.slot6.getValue(), this.slot7.getValue(), this.slot8.getValue(), this.slot9.getValue()};
        for (Slot iterSlot : mc.thePlayer.inventoryContainer.inventorySlots) {
            ItemStack iterItemStack = iterSlot.getStack();
            if (iterItemStack == null) continue;
            for (int iterHotBar = 0; iterHotBar < 9; iterHotBar++) {
                ItemCategory hotBarCategory = ItemCategory.get(items[iterHotBar]);
                if (hotBarCategory == ItemCategory.EMPTY || hotBarCategory != ItemCategory.get(iterItemStack.getItem()) || iterSlot.slotNumber == iterHotBar + 36) continue;
                if (hotBarCategory == ItemCategory.SWORD || hotBarCategory == ItemCategory.FISHING_ROD || hotBarCategory == ItemCategory.BOW || hotBarCategory == ItemCategory.PICKAXE || hotBarCategory == ItemCategory.AXE || hotBarCategory == ItemCategory.SHOVEL) {
                    mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, iterSlot.slotNumber, 1, 4, mc.thePlayer);
                    if (this.dropDelay.getValue() != 0) return true;
                }
            }
            if (iterItemStack.getItem() instanceof ItemArmor && iterSlot.slotNumber > 8) {
                mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, iterSlot.slotNumber, 1, 4, mc.thePlayer);
                if (this.dropDelay.getValue() != 0) return true;
            }
        }
        return false;
    }

    @Getter
    private enum ItemCategory {
        SWORD("Sword", item -> item instanceof ItemSword),
        FISHING_ROD("FishingRod", item -> item instanceof ItemFishingRod),
        BOW("Bow", item -> item instanceof ItemBow),
        WATER_BUCKET("WaterBucket", item -> item == Items.water_bucket),
        FOOD("Food", item -> item instanceof ItemFood),
        GOLDEN_APPLE("GoldenApple", item -> item == Items.golden_apple),
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

        public static ItemCategory get(Item matching) {
            for (ItemCategory category : values()) {
                if (category.getMatching().test(matching)) {
                    return category;
                }
            }
            return EMPTY;
        }
    }
}
