package net.minecraft.util;

import net.cicada.event.impl.MovementEvent;
import net.cicada.event.impl.MovementInputEvent;
import net.minecraft.client.settings.GameSettings;

public class MovementInputFromOptions extends MovementInput
{
    private final GameSettings gameSettings;

    public MovementInputFromOptions(GameSettings gameSettingsIn)
    {
        this.gameSettings = gameSettingsIn;
    }

    public void updatePlayerMoveState()
    {
        MovementInputEvent movementInputEvent = new MovementInputEvent(
                this.gameSettings.keyBindForward.isKeyDown(),
                this.gameSettings.keyBindBack.isKeyDown(),
                this.gameSettings.keyBindRight.isKeyDown(),
                this.gameSettings.keyBindLeft.isKeyDown(),
                this.gameSettings.keyBindJump.isKeyDown(),
                this.gameSettings.keyBindSneak.isKeyDown()
        ).call();

        this.moveStrafe = 0.0F;
        this.moveForward = 0.0F;

        if (movementInputEvent.isForward())
        {
            ++this.moveForward;
        }

        if (movementInputEvent.isBackward())
        {
            --this.moveForward;
        }

        if (movementInputEvent.isLeft())
        {
            ++this.moveStrafe;
        }

        if (movementInputEvent.isRight())
        {
            --this.moveStrafe;
        }

        this.jump = movementInputEvent.isJump();
        this.sneak = movementInputEvent.isSneak();

        MovementEvent movementEvent = new MovementEvent(this.moveForward, this.moveStrafe).call();
        this.moveForward = movementEvent.getMoveForward();
        this.moveStrafe = movementEvent.getMoveStrafe();

        if (this.sneak)
        {
            this.moveStrafe = (float)((double)this.moveStrafe * 0.3D);
            this.moveForward = (float)((double)this.moveForward * 0.3D);
        }
    }
}
