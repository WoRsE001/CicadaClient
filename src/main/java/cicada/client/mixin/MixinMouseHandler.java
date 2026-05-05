package cicada.client.mixin;

import cicada.client.utils.input.FrameInput;
import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// SCWGxD regrets everything he did. 01.05.2026 5:18.
@Mixin(MouseHandler.class)
public class MixinMouseHandler {
    @Inject(method = "onScroll", at = @At("HEAD"))
    private void onScroll(long handle, double xoffset, double yoffset, CallbackInfo ci) {
        FrameInput.INSTANCE.getScroll().set(xoffset, yoffset);
    }
}
