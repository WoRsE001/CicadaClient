package cicada.client.mixin.accessors;

import net.minecraft.client.KeyMapping;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

// SCWGxD regrets everything he did. 19.04.2026 12:07.
@Mixin(KeyMapping.class)
public interface AccessorKeyMapping {
    @Accessor("clickCount")
    void setClickCount(int clickCount);
}
