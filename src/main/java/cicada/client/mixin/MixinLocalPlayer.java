package cicada.client.mixin;

import cicada.client.event.events.*;
import cicada.client.feature.module.modules.movement.ModuleSprint;
import cicada.client.rotation.CameraRotation;
import cicada.client.utils.client.MinecraftExtensionsKt;
import cicada.client.utils.player.PlayerStateUtilsKt;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
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
import net.minecraft.world.phys.Vec3;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LocalPlayer.class)
public class MixinLocalPlayer {
    @Shadow
    private boolean wasSprinting;

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
            PlayerStateUtilsKt.setUtilAirTick(0);
            PlayerStateUtilsKt.setUtilGroundTick(PlayerStateUtilsKt.getUtilGroundTick() + 1);
        } else {
            PlayerStateUtilsKt.setUtilAirTick(PlayerStateUtilsKt.getUtilAirTick() + 1);
            PlayerStateUtilsKt.setUtilGroundTick(0);
        }

        EventPlayerStateUpdate.Pre.INSTANCE.call();
    }

    @Inject(method = "aiStep", at = @At("TAIL"))
    private void callUpdateEventPost(CallbackInfo ci) {
        EventPlayerStateUpdate.Post.INSTANCE.call();
    }

    @Inject(method = "sendPosition", at = @At("HEAD"), cancellable = true)
    private void hookMovementPre(CallbackInfo ci) {
        LocalPlayer player = (LocalPlayer) (Object) this;
        EventSendPos.Pre.INSTANCE.setPos(player.position());
        EventSendPos.Pre.INSTANCE.setGround(player.onGround());
        EventSendPos.Pre.INSTANCE.call();

        if (EventSendPos.Pre.INSTANCE.getCanceled())
            ci.cancel();
    }

    @ModifyExpressionValue(method = "sendPosition", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getX()D"))
    private double modifyXPosition(double original) {
        return EventSendPos.Pre.INSTANCE.getX();
    }

    @ModifyExpressionValue(method = "sendPosition", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getY()D"))
    private double modifyYPosition(double original) {
        return EventSendPos.Pre.INSTANCE.getY();
    }

    @ModifyExpressionValue(method = "sendPosition", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getZ()D"))
    private double modifyZPosition(double original) {
        return EventSendPos.Pre.INSTANCE.getZ();
    }

    @ModifyExpressionValue(method = "sendPosition", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;position()Lnet/minecraft/world/phys/Vec3;"))
    private Vec3 modifyPosition(Vec3 original) {
        return EventSendPos.Pre.INSTANCE.getPos();
    }

    @ModifyExpressionValue(method = "sendPosition", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;onGround()Z"))
    private boolean modifyOnGround(boolean original) {
        return EventSendPos.Pre.INSTANCE.getGround();
    }

    @Inject(method = "sendPosition", at = @At("RETURN"))
    private void hookMovementPost(CallbackInfo callbackInfo) {
        EventSendPos.Post.INSTANCE.call();
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

        EventSlowDown.Type type = EventSlowDown.INSTANCE.getTypeByItem(usedItemStack);
        if (type == null) return;
        EventSlowDown.INSTANCE.setType(type);
        EventSlowDown.INSTANCE.setSprint(!cir.getReturnValue());
        EventSlowDown.INSTANCE.call();
        cir.setReturnValue(!EventSlowDown.INSTANCE.getSprint());
    }

    @ModifyExpressionValue(method = "modifyInput", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;itemUseSpeedMultiplier()F"))
    private float callNoSlowDownEvent1(float original) {
        LocalPlayer player = MinecraftExtensionsKt.getPlayer();
        ItemStack usedItemStack = player.getUseItem();

        EventSlowDown.Type type = EventSlowDown.INSTANCE.getTypeByItem(usedItemStack);
        if (type == null) return original;
        EventSlowDown.INSTANCE.setType(type);
        EventSlowDown.INSTANCE.setSlowDown(original);
        EventSlowDown.INSTANCE.call();
        return EventSlowDown.INSTANCE.getSlowDown();
    }

    @ModifyExpressionValue(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isShiftKeyDown()Z"))
    private boolean callNoSlowDownEvent2(boolean original) {
        EventSlowDown.INSTANCE.setType(EventSlowDown.Type.Sneak);
        EventSlowDown.INSTANCE.setSprint(!original);
        EventSlowDown.INSTANCE.call();
        return original; // TODO: FIX SNEAK SPRINT
    }

    @ModifyExpressionValue(method = "modifyInput", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getAttributeValue(Lnet/minecraft/core/Holder;)D"))
    private double callNoSlowDownEvent3(double original) {
        LocalPlayer player = MinecraftExtensionsKt.getPlayer();

        EventSlowDown.INSTANCE.setType(EventSlowDown.Type.Sneak);
        EventSlowDown.INSTANCE.setSlowDown((float) player.getAttributeValue(Attributes.SNEAKING_SPEED));
        EventSlowDown.INSTANCE.call();
        return EventSlowDown.INSTANCE.getSlowDown();
    }

    @ModifyExpressionValue(method = "tick", at = @At(value = "FIELD", target = "Lnet/minecraft/client/player/ClientInput;keyPresses:Lnet/minecraft/world/entity/player/Input;"))
    private Input callEventSendInput(Input original) {
        EventSendInput.INSTANCE.setInput(original);
        EventSendInput.INSTANCE.call();
        return EventSendInput.INSTANCE.getInput();
    }

    @Inject(method = "sendIsSprintingIfNeeded", at = @At("HEAD"), cancellable = true)
    private void callSendSprintPreEvent(CallbackInfo ci) {
        EventSendSprint.Pre.INSTANCE.setWasSprinting(wasSprinting);
        EventSendSprint.Pre.INSTANCE.call();
        if (EventSendSprint.Pre.INSTANCE.getCanceled()) {
            ci.cancel();
        }
    }

    @ModifyExpressionValue(method = "sendIsSprintingIfNeeded", at = @At(value = "FIELD", target = "Lnet/minecraft/client/player/LocalPlayer;wasSprinting:Z", ordinal = 0, opcode = Opcodes.GETFIELD))
    private boolean injectSendSprintPreEvent(boolean original) {
        return EventSendSprint.Pre.INSTANCE.getWasSprinting();
    }

    @Inject(method = "sendIsSprintingIfNeeded", at = @At("TAIL"))
    private void callSendSprintPostEvent(CallbackInfo ci) {
        EventSendSprint.Post.INSTANCE.call();
    }
}
