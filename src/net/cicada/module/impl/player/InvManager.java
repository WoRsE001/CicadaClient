package net.cicada.module.impl.player;

import lombok.Getter;
import net.cicada.event.impl.UpdateEvent;
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
    NumberSetting delay = new NumberSetting("Delay", 1, 0, 20, 1, () -> true, this);

    byte timer;

    @Override
    public void listen(Event event) {
        if (event instanceof UpdateEvent) {
            if (mc.currentScreen instanceof GuiInventory) {
                if (this.timer > 0) this.timer--;
                if (this.timer <= 0) {
                    if (!this.searchAndSort() && !this.autoArmor()) this.drop();
                    this.timer = (byte) this.delay.getValue();
                }
                //if (drop()) return;}
            } else {
                this.timer = (byte) this.delay.getValue();
            }
        }
    }

    private boolean searchAndSort() {
        List<String> items = List.of(this.slot1.getValue(), this.slot2.getValue(), this.slot3.getValue(), this.slot4.getValue(), this.slot5.getValue(), this.slot6.getValue(), this.slot7.getValue(), this.slot8.getValue(), this.slot9.getValue());
        for (int i = 36; i < 45; i++) {
            ItemCategory category = ItemCategory.get(items.get(i - 36));
            ItemStack curItemStack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            for (Slot slot : mc.thePlayer.inventoryContainer.inventorySlots) {
                if (slot.getStack() == null) continue;
                if (category.is(slot.getStack()) && i != slot.slotNumber && (!(slot.getStack().getItem() instanceof ItemBlock) || ((ItemBlock) slot.getStack().getItem()).getBlock().isFullBlock())) {
                    if (curItemStack == null || ItemCategory.get(curItemStack.getItem()) != ItemCategory.get(slot.getStack().getItem()) || (slot.getStack().getItem() instanceof ItemSword && InvUtil.getSwordDamage(curItemStack) < InvUtil.getSwordDamage(slot.getStack())) || (slot.getStack().getItem() instanceof ItemBlock && curItemStack.stackSize < slot.getStack().stackSize)) {
                        InvUtil.moveItem(slot.slotNumber, i - 36);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean autoArmor() {
        for (Slot slot : mc.thePlayer.inventoryContainer.inventorySlots) {
            ItemStack slotItemStack = slot.getStack();
            if (slotItemStack == null) continue;
            if (slotItemStack.getItem() instanceof ItemArmor armor) {
                ItemStack armorItemStack = mc.thePlayer.inventoryContainer.getSlot(armor.armorType + 5).getStack();
                if (armorItemStack == null) {
                    InvUtil.stealItem(slot.slotNumber);
                    return true;
                }
            }
        }
        return false;
    }

    private void drop() {
        for (Slot slot : mc.thePlayer.inventoryContainer.inventorySlots) {
            ItemStack slotItemStack = slot.getStack();
            if (slotItemStack == null) continue;
            if (slotItemStack.getItem() instanceof ItemSword) {
                for (Slot scanSlot : mc.thePlayer.inventoryContainer.inventorySlots) {
                    if (slot.equals(scanSlot)) continue;
                    ItemStack scanItemStack = scanSlot.getStack();
                    if (scanItemStack != null && scanItemStack.getItem() instanceof ItemSword && InvUtil.getSwordDamage(scanItemStack) <= InvUtil.getSwordDamage(slotItemStack)) {
                        InvUtil.dropItem(scanSlot.slotNumber);
                        return;
                    }
                }
            } else if (slotItemStack.getItem() instanceof ItemBlock block) {
                if (!block.getBlock().isFullBlock()) {
                    InvUtil.dropItem(slot.slotNumber);
                    return;
                }
            } else if (slotItemStack.getItem() instanceof ItemFishingRod) {
                for (Slot scanSlot : mc.thePlayer.inventoryContainer.inventorySlots) {
                    if (slot.equals(scanSlot)) continue;
                    ItemStack scanItemStack = scanSlot.getStack();
                    if (scanItemStack != null && scanItemStack.getItem() instanceof ItemFishingRod) {
                        InvUtil.dropItem(scanSlot.slotNumber);
                        return;
                    }
                }
            } else if (slotItemStack.getItem() instanceof ItemBow) {
                for (Slot scanSlot : mc.thePlayer.inventoryContainer.inventorySlots) {
                    if (slot.equals(scanSlot)) continue;
                    ItemStack scanItemStack = scanSlot.getStack();
                    if (scanItemStack != null && scanItemStack.getItem() instanceof ItemBow) {
                        InvUtil.dropItem(scanSlot.slotNumber);
                        return;
                    }
                }
            } else if (slotItemStack.getItem() instanceof ItemArmor armor) {
                for (Slot scanSlot : mc.thePlayer.inventoryContainer.inventorySlots) {
                    if (slot.equals(scanSlot)) continue;
                    ItemStack scanItemStack = scanSlot.getStack();
                    if (scanItemStack != null && scanItemStack.getItem() instanceof ItemArmor scanArmor && armor.armorType == scanArmor.armorType && InvUtil.getDamageReduceAmount(slotItemStack) >= InvUtil.getDamageReduceAmount(scanItemStack)) {
                        InvUtil.dropItem(scanSlot.slotNumber);
                        return;
                    }
                }
            }
        }
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

        public boolean is(ItemStack itemStack) {
            if (this == EMPTY || itemStack == null) {
                return false;
            }
            return matching.test(itemStack.getItem());
        }
    }
}
