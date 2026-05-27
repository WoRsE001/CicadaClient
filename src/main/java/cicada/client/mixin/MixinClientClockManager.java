package cicada.client.mixin;

import cicada.client.feature.module.modules.visual.ModuleAmbient;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.ClientClockManager;
import net.minecraft.core.Holder;
import net.minecraft.world.clock.WorldClock;
import net.minecraft.world.clock.WorldClocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

// SCWGxD regrets everything he did. 22.05.2026 10:38.
@Mixin(ClientClockManager.class)
public class MixinClientClockManager {
    @ModifyReturnValue(method = "getTotalTicks", at = @At("RETURN"))
    private long injectOverrideClockTime(long original, Holder<WorldClock> definition) {
        if (!ModuleAmbient.INSTANCE.getToggled() && !definition.is(WorldClocks.OVERWORLD)) {
            return original;
        }

        return ModuleAmbient.INSTANCE.getTime(original);
    }
}
