package cicada.client.mixin.accessors;

import net.minecraft.network.protocol.game.ClientboundMoveEntityPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

// SCWGxD regrets everything he did. 23.06.2026 16:53.
@Mixin(ClientboundMoveEntityPacket.class)
public interface AccessorClientboundMoveEntityPacket {
    @Accessor("entityId")
    int getEntityId();
}
