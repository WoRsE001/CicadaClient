package cicada.client.mixin;

import cicada.client.feature.module.modules.visual.ModuleAmbient;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.renderer.WeatherEffectRenderer;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

// SCWGxD regrets everything he did. 22.05.2026 10:54.
@Mixin(WeatherEffectRenderer.class)
public class MixinWeatherEffectRenderer {
    @ModifyExpressionValue(method = "tickRainParticles", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;getRainLevel(F)F"))
    private float ambientPrecipitation2(float original) {
        if (!ModuleAmbient.INSTANCE.getToggled() || !ModuleAmbient.INSTANCE.getWeather().getToggled()) return original;
        if (ModuleAmbient.INSTANCE.getSnow()) return 0;

        return original;
    }

    @ModifyReturnValue(method = "getPrecipitationAt", at = @At(value = "RETURN", ordinal = 1))
    private Biome.Precipitation modifyBiomePrecipitation(Biome.Precipitation original) {
        if (!ModuleAmbient.INSTANCE.getToggled() || !ModuleAmbient.INSTANCE.getWeather().getToggled()) return original;
        if (ModuleAmbient.INSTANCE.getSnow()) return Biome.Precipitation.SNOW;

        return original;
    }
}
