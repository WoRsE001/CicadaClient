package cicada.client.mixin.accessors;

import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

// SCWGxD regrets everything he did. 01.05.2026 9:46.
@Mixin(SoundEvents.class)
public interface AccessorSoundEvents {
    @Invoker("registerForHolder")
    static Holder.Reference<SoundEvent> invokeRegisterForHolder(Identifier id) {
        throw new AssertionError();
    }
}
