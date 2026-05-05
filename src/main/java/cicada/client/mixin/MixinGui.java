package cicada.client.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import cicada.client.event.impl.RenderEvent;
import cicada.client.hud.HUDs;
import cicada.client.module.impl.visual.ModuleNoRender;
import cicada.client.module.impl.visual.ModuleOverlay;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class MixinGui {
    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;extractCameraOverlays(Lnet/minecraft/client/gui/GuiGraphicsExtractor;Lnet/minecraft/client/DeltaTracker;)V", shift = At.Shift.BEFORE), method = "extractRenderState")
    private void callRenderEvent$Gui$PRE(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        RenderEvent.Gui.Pre event = RenderEvent.Gui.Pre.INSTANCE;
        event.setGraphics(graphics);
        event.setDeltaTracker(deltaTracker);
        event.call();
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;extractSubtitleOverlay(Lnet/minecraft/client/gui/GuiGraphicsExtractor;Z)V", shift = At.Shift.AFTER, ordinal = 0), method = "extractRenderState")
    private void callRenderEvent$Gui$POST(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        RenderEvent.Gui.Post event = RenderEvent.Gui.Post.INSTANCE;
        event.setGraphics(graphics);
        event.setDeltaTracker(deltaTracker);
        event.call();
        HUDs.INSTANCE.render(graphics);
    }

    @ModifyExpressionValue(method = "extractCameraOverlays", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getTicksFrozen()I"))
    private int noRenderPowderSnowOverlay(int original) {
        if (ModuleNoRender.INSTANCE.getToggled() && ModuleNoRender.INSTANCE.getPowderSnowOverlay())
            return 0;

        return original;
    }

    @Inject(method = "extractCameraOverlays", at = @At("HEAD"))
    private void renderOverlay(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        if (ModuleOverlay.INSTANCE.getToggled())
            ModuleOverlay.INSTANCE.renderOverlay(graphics);
    }
}
