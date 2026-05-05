package cicada.client.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import cicada.client.CicadaClient;
import cicada.client.event.impl.GameLoopEvent;
import cicada.client.event.impl.LegitClickTimingEvent;
import cicada.client.event.impl.TickEvent;
import cicada.client.mixin.accessors.AccessorKeyMapping;
import cicada.client.module.impl.player.ModuleMultiAction;
import cicada.client.utils.input.FrameInput;
import cicada.client.utils.player.RaycastUtilsKt;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
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

	@Inject(at = @At("HEAD"), method = "tick", cancellable = true)
	private void callTickEvent$PRE(CallbackInfo ci) {
		TickEvent.Pre event = TickEvent.Pre.INSTANCE;
		event.call();

		if (event.getCanceled())
			ci.cancel();
	}

	@Inject(at = @At("RETURN"), method = "tick")
	private void callTickEvent$POST(CallbackInfo ci) {
		TickEvent.Post.INSTANCE.call();
	}

	@Inject(at = @At("HEAD"), method = "runTick")
	private void callGameLoopEvent$PRE(boolean advanceGameTime, CallbackInfo ci) {
		FrameInput.INSTANCE.getScroll().set(0, 0);
		GameLoopEvent.Pre.INSTANCE.call();
	}

	@Inject(at = @At("RETURN"), method = "runTick")
	private void callGameLoopEvent$POST(boolean advanceGameTime, CallbackInfo ci) {
		GameLoopEvent.Post.INSTANCE.call();
	}

	@Inject(at = @At("HEAD"), method = "runTick")
	private void updateFrameInput(boolean advanceGameTime, CallbackInfo ci) {
		FrameInput.INSTANCE.update();
	}

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;continueAttack(Z)V", shift = At.Shift.BEFORE), method = "handleKeybinds")
	private void callLegitClickTimingEvent(CallbackInfo ci) {
		LegitClickTimingEvent.INSTANCE.call();
	}

	@Inject(method = "startAttack", at = @At("HEAD"), cancellable = true)
	private void customStartAttack(CallbackInfoReturnable<Boolean> cir) {
		cir.setReturnValue(RaycastUtilsKt.startAttack(player.entityInteractionRange(), false));
	}

	@ModifyExpressionValue(method = "handleKeybinds", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isUsingItem()Z", ordinal = 0))
	private boolean injectMultiActionsAttackingWhileUsingAndEnforcedBlockingState(boolean isUsingItem) {
		if (isUsingItem) {
			if (!this.options.keyUse.isDown()) {
				this.gameMode.releaseUsingItem(this.player);
			}

			if (!ModuleMultiAction.mayAttackWhileUsing()) {
				((AccessorKeyMapping) this.options.keyAttack).setClickCount(0);
			}

			((AccessorKeyMapping) this.options.keyPickItem).setClickCount(0);
			((AccessorKeyMapping) this.options.keyUse).setClickCount(0);
		}

		return false;
	}
}
