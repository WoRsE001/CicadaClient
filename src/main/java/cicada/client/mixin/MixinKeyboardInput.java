package cicada.client.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import cicada.client.event.impl.MovementInputEvent;
import net.minecraft.client.player.KeyboardInput;
import net.minecraft.world.entity.player.Input;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

// SCWGxD regrets everything he did. 16.04.2026 8:39.
@Mixin(KeyboardInput.class)
public class MixinKeyboardInput {
    @ModifyExpressionValue(method = "tick", at = @At(value = "NEW", target = "(ZZZZZZZ)Lnet/minecraft/world/entity/player/Input;"))
    private Input callMovementInputEvent(Input original) {
        MovementInputEvent MIE = MovementInputEvent.INSTANCE;
        
        MIE.setForward(original.forward());
        MIE.setBack(original.backward());
        MIE.setLeft(original.left());
        MIE.setRight(original.right());
        MIE.setJump(original.jump());
        MIE.setSneak(original.shift());
        MIE.setSprint(original.sprint());
        MIE.call();

        return new Input(
                MIE.getForward(),
                MIE.getBack(),
                MIE.getLeft(),
                MIE.getRight(),
                MIE.getJump(),
                MIE.getSneak(),
                MIE.getSprint()
        );
    }
}
