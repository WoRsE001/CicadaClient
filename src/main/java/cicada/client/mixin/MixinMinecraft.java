package cicada.client.mixin;

import cicada.client.CicadaClient;
import cicada.client.event.events.EventGameLoop;
import cicada.client.event.events.EventClickTiming;
import cicada.client.event.events.EventTick;
import cicada.client.feature.module.modules.player.ModuleMultiAction;
import cicada.client.feature.module.modules.world.ModuleFastBreak;
import cicada.client.packethandle.PacketHandler;
import cicada.client.utils.input.FrameInput;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.main.GameConfig;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft {
	@Shadow
	@Nullable
	public LocalPlayer player;
	@Shadow
	@Final
	public Options options;
	@Shadow
	@Nullable
	public MultiPlayerGameMode gameMode;

	@Inject(method = "<init>", at = @At("TAIL"))
	private void callInitializeClient(GameConfig gameConfig, CallbackInfo ci) {
		CicadaClient.INSTANCE.initialize();
	}

    @Inject(method = "stop", at = @At("TAIL"))
    private void callUninitializeClient(CallbackInfo ci) {
        CicadaClient.INSTANCE.uninitialize();
    }

	@Inject(at = @At("HEAD"), method = "tick", cancellable = true)
	private void callTickEvent$PRE(CallbackInfo ci) {
		EventTick.Pre event = EventTick.Pre.INSTANCE;
		event.call();

		if (event.getCanceled())
			ci.cancel();
	}

	@Inject(at = @At("RETURN"), method = "tick")
	private void callTickEvent$POST(CallbackInfo ci) {
		EventTick.Post.INSTANCE.call();
	}

	@Inject(at = @At("HEAD"), method = "runTick")
	private void callGameLoopEvent$PRE(boolean advanceGameTime, CallbackInfo ci) {
		PacketHandler.INSTANCE.handle();
		FrameInput.INSTANCE.getScroll().set(0, 0);
		EventGameLoop.Pre.INSTANCE.call();
	}

	@Inject(at = @At("RETURN"), method = "runTick")
	private void callGameLoopEvent$POST(boolean advanceGameTime, CallbackInfo ci) {
		EventGameLoop.Post.INSTANCE.call();
	}

	@Inject(at = @At("HEAD"), method = "runTick")
	private void updateFrameInput(boolean advanceGameTime, CallbackInfo ci) {
		FrameInput.INSTANCE.update();
	}

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isUsingItem()Z", ordinal = 0, shift = At.Shift.BEFORE), method = "handleKeybinds")
	private void callLegitClickTimingEvent(CallbackInfo ci) {
		EventClickTiming.INSTANCE.call();
	}

	@ModifyExpressionValue(method = "continueAttack", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isUsingItem()Z"))
	private boolean injectMultiActionsBreakingWhileUsing(boolean original) {
		return original && !ModuleMultiAction.mayBreakWhileUsing();
	}

	@ModifyExpressionValue(method = "handleKeybinds", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isUsingItem()Z", ordinal = 0))
	private boolean injectMultiActionsAttackingWhileUsingAndEnforcedBlockingState(boolean isUsingItem) {
		if (isUsingItem) {
			if (!this.options.keyUse.isDown()) {
				this.gameMode.releaseUsingItem(this.player);
			}

			if (!ModuleMultiAction.mayAttackWhileUsing()) {
				this.options.keyAttack.clickCount = 0;
			}

			this.options.keyPickItem.clickCount = 0;
			this.options.keyUse.clickCount = 0;
		}

		return false;
	}
}
