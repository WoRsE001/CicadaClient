package cicada.client.mixin;

import cicada.client.event.events.EventJump;
import cicada.client.feature.module.modules.movement.ModuleMovementHelper;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

// SCWGxD regrets everything he did. 20.04.2026 6:27.
@Mixin(LivingEntity.class)
public class MixinLivingEntity {
    @ModifyExpressionValue(method = "jumpFromGround", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getJumpPower()F"))
    private float jumpHeight(float original) {
        EventJump.INSTANCE.setJumpPower(original);
        EventJump.INSTANCE.call();
        return EventJump.INSTANCE.getJumpPower();
    }

    @ModifyExpressionValue(method = "jumpFromGround", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getYRot()F"))
    private float jumpYaw(float original) {
        EventJump.INSTANCE.setMotionYaw(original);
        EventJump.INSTANCE.call();
        return EventJump.INSTANCE.getMotionYaw();
    }

    @ModifyArgs(method = "jumpFromGround", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/Vec3;<init>(DDD)V"))
    private void addMotionAfterJump(Args args, @Local(name="angle") float angle) {
        EventJump.INSTANCE.setMotionAddFactor(0.2f);
        EventJump.INSTANCE.call();
        args.set(0, (-Mth.sin(angle)) * 0.2);
        args.set(2, Mth.cos(angle) * 0.2);
    }

    @ModifyConstant(method = "aiStep", constant = @Constant(intValue = 10))
    private int noJumpDelay(int constant) {
        if (ModuleMovementHelper.INSTANCE.getToggled() && ModuleMovementHelper.INSTANCE.getNoJumpDelay())
            return 0;

        return constant;
    }
}
