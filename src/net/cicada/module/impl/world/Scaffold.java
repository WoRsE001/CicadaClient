package net.cicada.module.impl.world;

import net.cicada.module.setting.impl.BooleanSetting;
import net.cicada.module.setting.impl.ListSetting;
import net.cicada.module.setting.impl.NumberSetting;
import net.cicada.utility.Player.MovementUtil;
import net.cicada.utility.Player.PlayerUtil;
import net.cicada.utility.Player.RotateUtil;
import net.cicada.utility.Render.RenderUtil;
import net.minecraft.block.BlockAir;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.*;
import net.cicada.event.api.Event;
import net.cicada.event.impl.*;
import net.cicada.module.api.Category;
import net.cicada.module.api.Module;
import net.cicada.module.api.ModuleInfo;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector2f;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@ModuleInfo(name = "Scaffold", category = Category.World, key = Keyboard.KEY_V)
public class Scaffold extends Module {
    // ROTATION
    NumberSetting yawSpeed = new  NumberSetting("yawSpeed", 180, 0, 180, 1, () -> true, this);
    NumberSetting pitchSpeed = new  NumberSetting("pitchSpeed", 180, 0, 180, 1, () -> true, this);
    BooleanSetting snapYaw = new BooleanSetting("SnapYaw", true, () -> true, this);
    NumberSetting offsetYaw = new  NumberSetting("OffsetYaw", 45, 0, 180, 1, () -> true, this);
    BooleanSetting telly = new BooleanSetting("Telly", false, () -> true, this);
    NumberSetting jumpTicksToRotate = new  NumberSetting("JumpTicksToRotate", 7, 0, 10, 1, () -> telly.isValue(), this);
    NumberSetting groundTicksToJump = new  NumberSetting("GroundTicksToJump", 0, 0, 10, 1, () -> telly.isValue(), this);
    BooleanSetting staticPitch = new BooleanSetting("StaticPitch", false, () -> true, this);
    NumberSetting pitch = new  NumberSetting("Pitch", 75, -90, 90, 1, () -> staticPitch.isValue(), this);
    BooleanSetting intave = new BooleanSetting("Intave", true, () -> true, this);
    ListSetting sortPitches = new ListSetting("SortPitches", "Nearest", List.of("Nearest", "Highest", "Lowest", "Random"), () -> true, this);
    // Movement
    BooleanSetting moveFix = new BooleanSetting("moveFix", true, () -> true, this);
    BooleanSetting jumpFix = new BooleanSetting("JumpFix", true, () -> true, this);
    BooleanSetting autoJump = new BooleanSetting("AutoJump", true, () -> true, this);
    BooleanSetting sameY = new BooleanSetting("SameY", false, () -> true, this);
    // MISC
    ListSetting pickBlock = new ListSetting("PickBlock", "Legit", List.of("None", "Legit"), () -> true, this);
    // VISUAL
    BooleanSetting autoF5 = new BooleanSetting("AutoF5", true, () -> true, this);
    BooleanSetting showTargetBlock = new BooleanSetting("ShowTargetBlock", true, () -> true, this);

    BlockPos targetBlock;
    int groundTick, lastSlot;
    boolean onGround;

    @Override
    protected void onEnable() {
        RotateUtil.setRotation(mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch);
        if (this.autoF5.isValue()) mc.gameSettings.thirdPersonView = 1;
        super.onEnable();
    }

    @Override
    protected void onDisable() {
        this.targetBlock = null;
        if (this.autoF5.isValue()) mc.gameSettings.thirdPersonView = 0;
        if (!this.pickBlock.is("None") && lastSlot != -1) {
            mc.thePlayer.inventory.currentItem = lastSlot;
            lastSlot = -1;
        }
        super.onDisable();
    }

    @Override
    public void listen(Event event) {
        if (event instanceof TickEvent) {
            if (mc.thePlayer.onGround) {
                this.groundTick++;
                if (!this.onGround) {
                    this.onGround = true;
                    this.groundTick = 0;
                }
            }
            if (!mc.thePlayer.onGround && this.onGround) this.onGround = false;
            this.targetBlock = getBlockPos();
            if (this.targetBlock != null) {
                if (!this.pickBlock.is("None")) {
                    int bestSlot = this.findBlock();
                    if (bestSlot != -1) {
                        if (lastSlot == -1) lastSlot = mc.thePlayer.inventory.currentItem;
                        mc.thePlayer.inventory.currentItem = bestSlot;
                    } else if (lastSlot != -1) {
                        mc.thePlayer.inventory.currentItem = lastSlot;
                        lastSlot = -1;
                    }
                }
                Vector2f rot = this.getBestRotation();
                RotateUtil.rotateTo(this.getVectorForRotation(rot.getX(), rot.getY()), (float) this.yawSpeed.getValue(), (float) this.pitchSpeed.getValue());
            } else if (!this.pickBlock.is("None") && lastSlot != -1) {
                mc.thePlayer.inventory.currentItem = lastSlot;
                lastSlot = -1;
            }
        }

        if (event instanceof LegitClickTimingEvent) {
            MovingObjectPosition rayTrace = PlayerUtil.rayTrace(RotateUtil.rotation, 4.5F, 1);
            if (rayTrace.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && ((rayTrace.sideHit != EnumFacing.UP && rayTrace.sideHit != EnumFacing.DOWN) || !this.sameY.isValue() || mc.gameSettings.keyBindUseItem.isKeyDown()) && rayTrace.getBlockPos().equals(this.targetBlock)) {
                mc.rightClickMouse();
            }
        }

        if (event instanceof MovementInputEvent e) {
            if (this.telly.isValue() && MovementUtil.isMoving() && this.groundTick >= this.groundTicksToJump.getValue()) e.setJump(true);
            if (this.autoJump.isValue()) e.setJump(true);
        }

        if (this.showTargetBlock.isValue() && event instanceof Render3DEvent e && e.getPriority() == Event.Priority.High) {
            if (this.targetBlock != null) RenderUtil.render3DBox(new AxisAlignedBB(targetBlock.getX(), targetBlock.getY(), targetBlock.getZ(), targetBlock.getX() + 1, targetBlock.getY() + 1, targetBlock.getZ() + 1));
        }

        if (event instanceof LookEvent e) {
            e.setRotationYaw(RotateUtil.rotation.getX());
            e.setRotationPitch(RotateUtil.rotation.getY());
        }

        if (event instanceof MotionEvent e && e.getPriority() == Event.Priority.Low) {
            e.setRotationYaw(RotateUtil.rotation.getX());
            e.setRotationPitch(RotateUtil.rotation.getY());
            mc.thePlayer.rotationYawHead = RotateUtil.rotation.getX();
            mc.thePlayer.rotationPitchHead = RotateUtil.rotation.getY();
            mc.thePlayer.renderYawOffset = RotateUtil.rotation.getX();
        }

        if (event instanceof JumpEvent e && this.jumpFix.isValue()) {
            e.setRotationYaw(RotateUtil.rotation.getX());
        }

        if (this.moveFix.isValue()) {
            if (event instanceof StrafeEvent e) {
                e.setRotationYaw(RotateUtil.rotation.getX());
            }

            if (event instanceof MovementEvent e) {
                MovementUtil.moveFix(e, RotateUtil.rotation.getX(), MovementUtil.getDirection(mc.thePlayer.rotationYaw, e.getMoveForward(), e.getMoveStrafe()));
            }
        }
    }

    private BlockPos getBlockPos() {
        BlockPos playerBlockPos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY + mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ);
        Vec3 playerPos = mc.thePlayer.getPositionVector().add(new Vec3(0, -1, 0));
        BlockPos blockPos = null;
        for (int x = playerBlockPos.getX() - 4; x <= playerBlockPos.getX() + 4; x++) {
            for (int y = playerBlockPos.getY() - 4; y <= playerBlockPos.getY() + 4; y++) {
                for (int z = playerBlockPos.getZ() - 4; z <= playerBlockPos.getZ() + 4; z++) {
                    if (mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock() instanceof BlockAir) continue;
                    if (blockPos == null) blockPos = new BlockPos(x, y, z);
                    if (playerPos.distanceTo(new Vec3(x + 0.5, y, z + 0.5)) < playerPos.distanceTo(new Vec3(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5))) blockPos = new BlockPos(x, y, z);
                }
            }
        }
        return blockPos;
    }

    private float getYaw() {
        float yaw = MovementUtil.getDirs() - 180;
        float roundYaw = MathHelper.round(yaw, 45);
        if (this.snapYaw.isValue()) yaw = roundYaw;
        if (this.telly.isValue()) {
            if ((mc.thePlayer.onGround && this.groundTick >= this.groundTicksToJump.getValue()) || mc.thePlayer.jumpTicks > this.jumpTicksToRotate.getValue()) {
                yaw += 180;
            }
        } else if (roundYaw % 90 == 0) {
            if (Math.floor(mc.thePlayer.posX + Math.cos(Math.toRadians(yaw)) * 0.5) != Math.floor(mc.thePlayer.posX) || Math.floor(mc.thePlayer.posZ + Math.sin(Math.toRadians(yaw)) * 0.5) != Math.floor(mc.thePlayer.posZ)) yaw += (float) this.offsetYaw.getValue();
            else yaw -= (float) this.offsetYaw.getValue();
        }
        return yaw;
    }

    private float getPitch(float yaw) {
        List<Float> pitches = new ArrayList<>();
        for (int i = -90; i < 91; i++) {
            MovingObjectPosition rayTrace = PlayerUtil.rayTrace(new Vector2f(yaw, i), 4.5F, 1);
            if (rayTrace.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK || rayTrace.sideHit == EnumFacing.UP || !rayTrace.getBlockPos().equals(this.targetBlock))
                continue;
            pitches.add((float) i);
        }
        if (this.staticPitch.isValue()) return (float) this.pitch.getValue();
        if (pitches.isEmpty()) return intave.isValue() ? 75 : RotateUtil.rotation.getY();
        if (this.sortPitches.is("Nearest")) {
            pitches.sort(Comparator.comparingDouble(pitch -> Math.abs(RotateUtil.rotation.getY() - pitch)));
            return pitches.getFirst();
        } else {
            pitches.sort(Comparator.comparingDouble(pitch -> 90 - pitch));
            if (this.sortPitches.is("Highest")) return pitches.getLast();
            if (this.sortPitches.is("Lowest")) return pitches.getFirst();
            if (this.sortPitches.is("Random")) return pitches.get((int) Math.ceil(Math.random() * (pitches.size() - 1)));
        }
        return 0;
    }

    private Vector2f getBestRotation() {
        float yaw = this.getYaw();
        float pitch = this.getPitch(yaw);

        if (this.telly.isValue()) {
            if (!((mc.thePlayer.onGround && this.groundTick >= this.groundTicksToJump.getValue()) || mc.thePlayer.jumpTicks > this.jumpTicksToRotate.getValue()) && this.intave.isValue()) {
                List<Float> yaws = new ArrayList<>();
                for (int i = -180; i < 181; i++) {
                    MovingObjectPosition rayTrace = PlayerUtil.rayTrace(new Vector2f(i, pitch), 4.5F, 1);
                    if (rayTrace.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK || rayTrace.sideHit == EnumFacing.UP || !rayTrace.getBlockPos().equals(this.targetBlock))
                        continue;
                    yaws.add((float) i);
                }
                if (!yaws.isEmpty()) {
                    boolean isRight = Math.floor(mc.thePlayer.posX + Math.cos(Math.toRadians(yaw)) * 0.5) != Math.floor(mc.thePlayer.posX) || Math.floor(mc.thePlayer.posZ + Math.sin(Math.toRadians(yaw)) * 0.5) != Math.floor(mc.thePlayer.posZ);
                    yaws.sort(Comparator.comparingDouble(yaw1 -> Math.abs(MovementUtil.getDirs() - 180 + (isRight ? this.offsetYaw.getValue() : -this.offsetYaw.getValue()) - yaw1)));
                    yaw =  yaws.getFirst();
                } else yaw = RotateUtil.rotation.getX();
            }
        }

        return new Vector2f(yaw, pitch);
    }

    private Vec3 getVectorForRotation(float yaw, float pitch) {
        float f = MathHelper.cos(-yaw * 0.017453292F - (float) Math.PI);
        float f1 = MathHelper.sin(-yaw * 0.017453292F - (float) Math.PI);
        float f2 = -MathHelper.cos(-pitch * 0.017453292F);
        float f3 = MathHelper.sin(-pitch * 0.017453292F);
        return mc.thePlayer.getPositionEyes(1).add(new Vec3(f1 * f2, f3, f * f2));
    }

    private int findBlock() {
        for (int i = 0; i < 9; i++) {
            if (mc.thePlayer.inventory.getStackInSlot(i) != null && mc.thePlayer.inventory.getStackInSlot(i).getItem() instanceof ItemBlock) return i;
        }

        return -1;
    }
}
