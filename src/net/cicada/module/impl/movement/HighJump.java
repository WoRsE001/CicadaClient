package net.cicada.module.impl.movement;

import net.cicada.event.impl.MotionEvent;
import net.cicada.module.setting.impl.ListSetting;
import net.cicada.module.setting.impl.NumberSetting;
import net.cicada.utility.Player.MovementUtil;
import net.minecraft.block.BlockAir;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.cicada.event.api.Event;
import net.cicada.event.impl.JumpEvent;
import net.cicada.event.impl.UpdateEvent;
import net.cicada.module.api.Category;
import net.cicada.module.api.Module;
import net.cicada.module.api.ModuleInfo;
import org.lwjgl.input.Keyboard;

import java.util.List;

@ModuleInfo(name = "HighJump", category = Category.Movement)
public class HighJump extends Module {
    ListSetting mode = new ListSetting("Mode", "Motion", List.of("Motion", "Matrix"), () -> true, this);
    NumberSetting motion = new NumberSetting("Motion", 0.42, 0, 5, 0.01, () -> this.mode.is("Motion"), this);

    int ticksSinceJump;
    boolean active, falling, moving;

    @Override
    public void onEnable() {
        this.ticksSinceJump = 0;
        this.active = this.falling = false;
        this.moving = MovementUtil.isMoving();
        super.onEnable();
    }

    @Override
    public void listen(Event event) {
        if (event instanceof MotionEvent e) {
            if (this.mode.is("Matrix")) {
                if (e.getPriority() == Event.Priority.Low && this.ticksSinceJump == 1) {
                    e.setOnGround(false);
                }
            }
        }

        if (event instanceof UpdateEvent) {
            if (this.mode.is("Matrix")) {
                if (!this.moving) {
                    MovementUtil.strafe(0.16, 1);
                    this.moving = true;
                }

                if (mc.thePlayer.isCollidedVertically) {
                    this.active = true;
                }

                if (this.ticksSinceJump == 1) {
                    mc.thePlayer.onGround = false;
                    mc.thePlayer.motionY = 0.998;
                }

                if (mc.thePlayer.isCollidedVertically && this.ticksSinceJump > 4) {
                    this.toggle();
                }

                if (!mc.thePlayer.onGround && this.ticksSinceJump >= 2) {
                    mc.thePlayer.motionY += 0.0034999;
                    if (!this.falling && mc.thePlayer.motionY < (double) 0.0F && mc.thePlayer.motionY > -0.05) {
                        mc.thePlayer.motionY = 0.0029999;
                        this.falling = true;
                        this.toggle();
                    }
                }

                if (this.active) {
                    ++this.ticksSinceJump;
                }
            }
        }

        if (event instanceof JumpEvent e) {
            if (this.mode.is("Motion")) {
                e.setMotion((float) motion.getValue());
            }
        }
    }
}
