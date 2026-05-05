package cicada.client.mixin;

import cicada.client.event.impl.KeyEvent;
import net.minecraft.client.KeyboardHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// SCWGxD regrets everything he did. 30.03.2026 16:01.
@Mixin(KeyboardHandler.class)
public class MixinKeyboardHandler {
    @Inject(method = "keyPress", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/platform/FramerateLimitTracker;onInputReceived()V", shift = At.Shift.AFTER))
    private void callKeyEvent(long handle, int action, net.minecraft.client.input.KeyEvent event, CallbackInfo ci) {
        KeyEvent keyEvent = KeyEvent.INSTANCE;
        keyEvent.setAction(action);
        keyEvent.setInput(event);
        keyEvent.call();
    }
}
