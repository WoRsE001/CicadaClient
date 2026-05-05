package cicada.client.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import cicada.client.event.impl.PlayerStateUpdateEvent;
import cicada.client.event.impl.SendPosEvent;
import cicada.client.event.impl.SlowDownEvent;
import cicada.client.module.impl.movement.ModuleSprint;
import cicada.client.rotation.CameraRotation;
import cicada.client.utils.MinecraftExtensionsKt;
import cicada.client.utils.player.PlayerStateUtilsKt;
import net.minecraft.client.ClientRecipeBook;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.chat.ChatAbilities;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.stats.StatsCounter;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LocalPlayer.class)
public class MixinLocalPlayer {
    // test
    @Inject(method = "<init>", at = @At("TAIL"))
    private void resetCameraRotation(Minecraft minecraft, ClientLevel level, ClientPacketListener connection, StatsCounter stats, ClientRecipeBook recipeBook, Input lastSentInput, boolean wasSprinting, ChatAbilities chatAbilities, CallbackInfo ci) {
        CameraRotation.INSTANCE.setX(0f);
        CameraRotation.INSTANCE.setY(0f);
    }

    @ModifyExpressionValue(method = "applyInput", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getXRot()F"))
    private float modifyRenderPitch(float original) {
        return CameraRotation.INSTANCE.getX();
    }

    @ModifyExpressionValue(method = "applyInput", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getYRot()F"))
    private float modifyRenderYaw(float original) {
        return CameraRotation.INSTANCE.getY();
    }

    @ModifyExpressionValue(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Input;sprint()Z"), method = "aiStep")
    private boolean modifySprint(boolean original) {
        return original || (ModuleSprint.INSTANCE.getToggled() && ModuleSprint.INSTANCE.getAuto());
    }

    @Inject(method = "aiStep", at = @At("HEAD"))
    private void callUpdateEventPre(CallbackInfo ci) {
        LocalPlayer player = (LocalPlayer) (Object) this;

        if (player.onGround()) {
            PlayerStateUtilsKt.setUtilAirTicks(0);
            PlayerStateUtilsKt.setUtilGroundTicks(PlayerStateUtilsKt.getUtilGroundTicks() + 1);
        } else {
            PlayerStateUtilsKt.setUtilAirTicks(PlayerStateUtilsKt.getUtilAirTicks() + 1);
            PlayerStateUtilsKt.setUtilGroundTicks(0);
        }

        PlayerStateUpdateEvent.Pre.INSTANCE.call();
    }

    @Inject(method = "aiStep", at = @At("TAIL"))
    private void callUpdateEventPost(CallbackInfo ci) {
        PlayerStateUpdateEvent.Post.INSTANCE.call();
    }

    @Inject(method = "sendPosition", at = @At("HEAD"), cancellable = true)
    private void hookMovementPre(CallbackInfo ci) {
        LocalPlayer player = (LocalPlayer) (Object) this;
        SendPosEvent.Pre.INSTANCE.setPos(player.position());
        SendPosEvent.Pre.INSTANCE.setGround(player.onGround());
        SendPosEvent.Pre.INSTANCE.call();

        if (SendPosEvent.Pre.INSTANCE.getCanceled())
            ci.cancel();
    }

    @ModifyExpressionValue(method = "sendPosition", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getX()D"))
    private double modifyXPosition(double original) {
        return SendPosEvent.Pre.INSTANCE.getX();
    }

    @ModifyExpressionValue(method = "sendPosition", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getY()D"))
    private double modifyYPosition(double original) {
        return SendPosEvent.Pre.INSTANCE.getY();
    }

    @ModifyExpressionValue(method = "sendPosition", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getZ()D"))
    private double modifyZPosition(double original) {
        return SendPosEvent.Pre.INSTANCE.getZ();
    }

    @ModifyExpressionValue(method = "sendPosition", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;onGround()Z"))
    private boolean modifyOnGround(boolean original) {
        return SendPosEvent.Pre.INSTANCE.getGround();
    }

    @Inject(method = "sendPosition", at = @At("RETURN"))
    private void hookMovementPost(CallbackInfo callbackInfo) {
        SendPosEvent.Post.INSTANCE.call();
    }

    @ModifyExpressionValue(method = "isSprintingPossible", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isMobilityRestricted()Z"))
    private boolean sprintOnBlindness(boolean original) {
        return original && (!ModuleSprint.INSTANCE.getToggled() || !ModuleSprint.INSTANCE.getWhileBlindness());
    }

    @Inject(method = "isSlowDueToUsingItem", at = @At("RETURN"), cancellable = true)
    private void callNoSlowDownEvent(CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue())
            return;

        LocalPlayer player = MinecraftExtensionsKt.getPlayer();
        ItemStack usedItemStack = player.getUseItem();

        SlowDownEvent.Type type = SlowDownEvent.INSTANCE.getTypeByItem(usedItemStack);
        if (type == null) return;
        SlowDownEvent.INSTANCE.setType(type);
        SlowDownEvent.INSTANCE.setSprint(!cir.getReturnValue());
        SlowDownEvent.INSTANCE.call();
        cir.setReturnValue(!SlowDownEvent.INSTANCE.getSprint());
    }

    @ModifyExpressionValue(method = "modifyInput", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;itemUseSpeedMultiplier()F"))
    private float callNoSlowDownEvent1(float original) {
        LocalPlayer player = MinecraftExtensionsKt.getPlayer();
        ItemStack usedItemStack = player.getUseItem();

        SlowDownEvent.Type type = SlowDownEvent.INSTANCE.getTypeByItem(usedItemStack);
        if (type == null) return original;
        SlowDownEvent.INSTANCE.setType(type);
        SlowDownEvent.INSTANCE.setSlowDown(original);
        SlowDownEvent.INSTANCE.call();
        return SlowDownEvent.INSTANCE.getSlowDown();
    }

    @ModifyExpressionValue(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isShiftKeyDown()Z"))
    private boolean callNoSlowDownEvent2(boolean original) {
        SlowDownEvent.INSTANCE.setType(SlowDownEvent.Type.Sneak);
        SlowDownEvent.INSTANCE.setSprint(!original);
        SlowDownEvent.INSTANCE.call();
        return original; // TODO: FIX SNEAK SPRINT
    }

    @ModifyExpressionValue(method = "modifyInput", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getAttributeValue(Lnet/minecraft/core/Holder;)D"))
    private double callNoSlowDownEvent3(double original) {
        LocalPlayer player = MinecraftExtensionsKt.getPlayer();

        SlowDownEvent.INSTANCE.setType(SlowDownEvent.Type.Sneak);
        SlowDownEvent.INSTANCE.setSlowDown((float) player.getAttributeValue(Attributes.SNEAKING_SPEED));
        SlowDownEvent.INSTANCE.call();
        return SlowDownEvent.INSTANCE.getSlowDown();
    }
}
