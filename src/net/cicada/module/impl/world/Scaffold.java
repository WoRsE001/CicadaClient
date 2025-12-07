package net.cicada.module.impl.world;

import net.cicada.module.setting.impl.BooleanSetting;
import net.cicada.module.setting.impl.ListSetting;
import net.cicada.module.setting.impl.NumberSetting;
import net.cicada.utility.Player.MoveUtil;
import net.cicada.utility.Player.PlayerUtil;
import net.cicada.utility.Player.RotateUtil;
import net.cicada.utility.Render.RenderUtil;
import net.minecraft.util.*;
import net.cicada.event.api.Event;
import net.cicada.event.impl.*;
import net.cicada.module.api.Category;
import net.cicada.module.api.Module;
import net.cicada.module.api.ModuleInfo;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector2f;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@ModuleInfo(name = "Scaffold", category = Category.World, key = Keyboard.KEY_V)
public class Scaffold extends Module {
    NumberSetting yawSpeed = new NumberSetting("YawSpeed", 180, 0, 180, 1, () -> true, this);
    NumberSetting pitchSpeed = new NumberSetting("PitchSpeed", 180, 0, 180, 1, () -> true, this);
    NumberSetting yawOffset = new NumberSetting("OffsetYaw", 0, 0, 50, 1, () -> true, this);
    BooleanSetting yawCorrect = new BooleanSetting("YawCorrect", false, () -> true, this);
    ListSetting sortPitches = new ListSetting("SortPitches", "Nearest", List.of("Nearest", "Highest", "Lowest"), () -> true, this);
    BooleanSetting moveFix = new BooleanSetting("MoveFix", true, () -> true, this);
    BooleanSetting jumpFix = new BooleanSetting("JumpFix", true, () -> true, this);
    BooleanSetting telly = new BooleanSetting("Telly", false, () -> true, this);
    NumberSetting yawForwardSpeed = new NumberSetting("YawForwardSpeed", 180, 0, 180, 1, () -> telly.isValue(), this);
    NumberSetting pitchForwardSpeed = new NumberSetting("PitchForwardSpeed", 180, 0, 180, 1, () -> telly.isValue(), this);
    NumberSetting rotateAfterJumpTicks = new NumberSetting("RotateAfterJumpTicks", 0, 0, 10, 1, () -> telly.isValue(), this);
    NumberSetting jumpAfterGroundTicks = new NumberSetting("JumpAfterGroundTicks", 0, 0, 10, 1, () -> telly.isValue(), this);
    BooleanSetting safeY = new BooleanSetting("SafeY", false, () -> true, this);
    BooleanSetting autoJump = new BooleanSetting("AutoJump", false, () -> true, this);

    BlockPos targetBlock;
    int groundTicks;
    boolean onGround;

    @Override
    protected void onEnable() {
        targetBlock = null;
        super.onEnable();
    }

    @Override
    public void listen(Event event) {
        switch (event) {
            case JumpEvent jumpEvent -> {
                if (targetBlock != null && jumpFix.isValue()) {
                    jumpEvent.setRotationYaw(RotateUtil.rotation.getX());
                }
            }

            case LegitClickTimingEvent ignored -> {
                MovingObjectPosition rayTrace = PlayerUtil.rayTrace(new Vector2f(RotateUtil.rotation.getX(), RotateUtil.rotation.getY()), 4.5F, 1);
                if (rayTrace.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && rayTrace.getBlockPos().equals(targetBlock) && (rayTrace.sideHit != EnumFacing.UP || !(safeY.isValue() && !mc.gameSettings.keyBindUseItem.isKeyDown()))) mc.rightClickMouse();
            }

            case LookEvent lookEvent -> {
                if (targetBlock != null) {
                    lookEvent.setRotationYaw(RotateUtil.rotation.getX());
                    lookEvent.setRotationPitch(RotateUtil.rotation.getY());
                }
            }

            case MotionEvent motionEvent -> {
                if (targetBlock != null && motionEvent.getPriority() == Event.Priority.Low) {
                    motionEvent.setRotationYaw(RotateUtil.rotation.getX());
                    motionEvent.setRotationPitch(RotateUtil.rotation.getY());
                    mc.thePlayer.rotationYawHead = RotateUtil.rotation.getX();
                    mc.thePlayer.rotationPitchHead = RotateUtil.rotation.getY();
                    mc.thePlayer.renderYawOffset = RotateUtil.rotation.getX();
                }
            }

            case MovementEvent movementEvent -> {
                if (targetBlock != null && moveFix.isValue()) {
                    MoveUtil.moveFix(movementEvent, RotateUtil.rotation.getX(), MoveUtil.getDirection(mc.thePlayer.rotationYaw, movementEvent.getMoveForward(), movementEvent.getMoveStrafe()));
                }
            }

            case MovementInputEvent movementInputEvent -> {
                if (targetBlock != null) {
                    movementInputEvent.setJump((autoJump.isValue() || mc.gameSettings.keyBindJump.isKeyDown()) && (!telly.isValue() || groundTicks >= jumpAfterGroundTicks.getValue()));
                }
            }

            case Render3DEvent render3DEvent -> {
                if (targetBlock != null && render3DEvent.getPriority() == Event.Priority.High) {
                    RenderUtil.setGlColor(new Color(0, 0, 0));
                    RenderUtil.render3DBox(new AxisAlignedBB(targetBlock.getX(), targetBlock.getY(), targetBlock.getZ(), targetBlock.getX() + 1, targetBlock.getY() + 1, targetBlock.getZ() + 1));
                }
            }

            case StrafeEvent strafeEvent -> {
                if (targetBlock != null && moveFix.isValue()) {
                    strafeEvent.setRotationYaw(RotateUtil.rotation.getX());
                }
            }

            case TickEvent ignored -> {
                if (mc.thePlayer.onGround) {
                    groundTicks++;
                    if (!onGround) {
                        onGround = true;
                        groundTicks = 0;
                    }
                }
                if (!mc.thePlayer.onGround && onGround) onGround = false;
                targetBlock = getNearestBlock();
                if (targetBlock != null) {
                    if (!(telly.isValue() && isTelly())) {
                        RotateUtil.setRotateWithGCD(getBestRotation(), (float) yawSpeed.getValue(), (float) pitchSpeed.getValue());
                    } else {
                        RotateUtil.setRotateWithGCD(getBestRotation(), (float) yawForwardSpeed.getValue(), (float) pitchForwardSpeed.getValue());
                    }
                }
            }

            default -> {
            }
        }
    }

    private BlockPos getNearestBlock() {
        BlockPos eyePos = mc.thePlayer.getPosition().add(0, 1, 0);
        BlockPos targetBlock = null;
        for (int x = eyePos.getX() - 4; x <= eyePos.getX() + 4; x++) {
            for (int y = eyePos.getY() - 4; y <= eyePos.getY(); y++) {
                for (int z = eyePos.getZ() - 4; z <= eyePos.getZ() + 4; z++) {
                    if (!mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock().isFullBlock()) continue;
                    if (targetBlock == null) targetBlock = new BlockPos(x, y, z);
                    else if (mc.thePlayer.getDistance(x + 0.5, y + 0.5, z + 0.5) < mc.thePlayer.getDistance(targetBlock.getX() + 0.5, targetBlock.getY() + 0.5, targetBlock.getZ() + 0.5)) targetBlock = new BlockPos(x, y, z);
                }
            }
        }
        return targetBlock;
    }

    private float getYaw() {
        float yaw = MoveUtil.getDirs() - 180;
        boolean isOnRightSide = Math.floor(mc.thePlayer.posX + Math.cos(Math.toRadians(yaw)) * 0.5) != Math.floor(mc.thePlayer.posX) ||
                Math.floor(mc.thePlayer.posZ + Math.sin(Math.toRadians(yaw)) * 0.5) != Math.floor(mc.thePlayer.posZ);
        if (telly.isValue() && isTelly()) yaw += 180;
        else yaw += (float) yawOffset.getValue() * (isOnRightSide ? 1 : -1);
        return yaw;
    }

    private float getPitch(float yaw) {
        List<Integer> pitches = new ArrayList<>();
        for (int i = 0; i <= 90; i++) {
            MovingObjectPosition rayTrace = PlayerUtil.rayTrace(new Vector2f(yaw, i), 4.5F, 1);
            if (rayTrace.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK || !rayTrace.getBlockPos().equals(targetBlock) || rayTrace.sideHit == EnumFacing.UP) continue;
            pitches.add(i);
        }
        if (sortPitches.is("Nearest")) pitches.sort(Comparator.comparingInt(pitch -> (int) Math.abs(pitch - RotateUtil.rotation.getY())));
        else if (sortPitches.is("Highest")) pitches.sort(Comparator.reverseOrder());
        else if (sortPitches.is("Lowest")) pitches.sort(Comparator.naturalOrder());
        return pitches.isEmpty() ? 75 : pitches.getFirst();
    }

    private Vector2f getBestRotation() {
        List<Vector2f> validRotations = new ArrayList<>();
        float bestYaw = getYaw();

        for (int i = 0; i < 360; i++) {
            float yaw = MathHelper.wrapAngleTo180_float(i);
            float pitch = getPitch(yaw);
            MovingObjectPosition rayTrace = PlayerUtil.rayTrace(new Vector2f(yaw, pitch), 4.5F, 1);
            if (rayTrace.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && rayTrace.getBlockPos().equals(targetBlock) && rayTrace.sideHit != EnumFacing.DOWN && rayTrace.hitVec.yCoord < mc.thePlayer.posY) validRotations.add(new Vector2f(yaw, pitch));
        }

        if (!(telly.isValue() && isTelly())) {
            validRotations.sort(Comparator.comparingDouble(data -> {
                float sortYaw = yawCorrect.isValue() ? bestYaw : RotateUtil.rotation.getX();
                float yaw = MathHelper.wrapAngleTo180_float(sortYaw - data.getX());
                float pitch = RotateUtil.rotation.getY() - data.getY();
                return Math.hypot(yaw, pitch);
            }));
        } else return new Vector2f(bestYaw, RotateUtil.rotation.getY());

        return validRotations.isEmpty() ? RotateUtil.rotation : validRotations.getFirst();
    }

    private boolean isTelly() {
        return (mc.thePlayer.onGround && groundTicks >= jumpAfterGroundTicks.getValue()) || rotateAfterJumpTicks.getValue() > 10 - mc.thePlayer.jumpTicks;
    }
}
