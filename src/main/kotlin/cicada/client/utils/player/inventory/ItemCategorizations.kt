package cicada.utility.player.inventory

import net.minecraft.core.component.DataComponents
import net.minecraft.tags.ItemTags
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.food.FoodProperties
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ItemUseAnimation
import net.minecraft.world.item.Items
import net.minecraft.world.item.component.Tool

// испорченно SCWGxD в 30.12.2025:20:01
val ItemStack.isConsumable: Boolean
    get() = this.isFood || this.item == Items.POTION || this.item == Items.MILK_BUCKET

val ItemStack.isFood: Boolean
    get() = foodComponent != null && this.useAnimation == ItemUseAnimation.EAT

val ItemStack.foodComponent: FoodProperties?
    get() = this.get(DataComponents.FOOD)

val ItemStack.toolComponent: Tool?
    get() = this.get(DataComponents.TOOL)

val ItemStack.isBundle
    get() = this.`is`(ItemTags.BUNDLES)

val ItemStack.isTotemOfUndying
    get() = this.get(DataComponents.DEATH_PROTECTION) != null

// Tools

val ItemStack.isSword
    get() = this.`is`(ItemTags.SWORDS)

val ItemStack.isSpear
    get() = this.`is`(ItemTags.SPEARS)

val ItemStack.isPickaxe
    get() = this.`is`(ItemTags.PICKAXES)

val ItemStack.isAxe
    get() = this.`is`(ItemTags.AXES)

val ItemStack.isShovel
    get() = this.`is`(ItemTags.SHOVELS)

val ItemStack.isHoe
    get() = this.`is`(ItemTags.HOES)

val ItemStack.isMiningTool
    get() = isAxe || isPickaxe || isShovel || isHoe

// Armors

val ItemStack.isFootArmor
    get() = this.`is`(ItemTags.FOOT_ARMOR)

val ItemStack.isLegArmor
    get() = this.`is`(ItemTags.LEG_ARMOR)

val ItemStack.isChestArmor
    get() = this.`is`(ItemTags.CHEST_ARMOR)

val ItemStack.isHeadArmor
    get() = this.`is`(ItemTags.HEAD_ARMOR)

val ItemStack.isPlayerArmor
    get() = isFootArmor || isLegArmor || isChestArmor || isHeadArmor

val ItemStack.equippableComponent
    get() = this.get(DataComponents.EQUIPPABLE)

val ItemStack.equipmentSlot
    get() = this.equippableComponent?.slot

val ItemStack.armorToughness
    get() = this.getAttributeValue(Attributes.ARMOR_TOUGHNESS)

val ItemStack.armorValue
    get() = this.getAttributeValue(Attributes.ARMOR)

val ItemStack.armorKnockbackResistance
    get() = this.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE)

// Shield

val ItemStack.blocksAttacksComponent
    get() = this.get(DataComponents.BLOCKS_ATTACKS)