package cicada.client.mixin;

import cicada.client.event.impl.ChatMessageEvent;
import net.minecraft.client.multiplayer.ClientPacketListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// SCWGxD regrets everything he did. 10.04.2026 7:38.
@Mixin(ClientPacketListener.class)
public class MixinClientPacketListener {
    @Inject(method = "sendChat", at = @At("HEAD"), cancellable = true)
    private void callChatSendEvent(String string, CallbackInfo ci) {
        ChatMessageEvent.Send.INSTANCE.setContent(string);
        ChatMessageEvent.Send.INSTANCE.call();
        if (ChatMessageEvent.Send.INSTANCE.getCanceled())
            ci.cancel();
    }
}
