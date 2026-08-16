package cicada.client.mixin;

import cicada.client.event.events.EventChatMessage;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.client.multiplayer.chat.GuiMessageSource;
import net.minecraft.client.multiplayer.chat.GuiMessageTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MessageSignature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatComponent.class)
public class MixinChatComponent {
    @Inject(method = "addMessage", at = @At(value = "HEAD"), cancellable = true)
    private void callReceiveChatEvent(
            Component contents,
            MessageSignature signature,
            GuiMessageSource source,
            GuiMessageTag tag,
            CallbackInfo ci
    ) {
        EventChatMessage.Receive event = EventChatMessage.Receive.INSTANCE;
        event.setContent(contents.getString());
        event.call();
        if (event.getCanceled()) ci.cancel();
    }
}
