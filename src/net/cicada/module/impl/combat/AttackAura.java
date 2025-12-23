package net.cicada.module.impl.combat;

import de.florianmichael.viamcp.fixes.AttackOrder;
import net.cicada.module.api.ModuleManager;
import net.cicada.module.setting.impl.MultiBooleanSetting;
import net.cicada.utility.MathUtil;
import net.cicada.utility.Player.CombatManager;
import net.cicada.utility.Player.MoveUtil;
import net.cicada.utility.Player.PlayerUtil;
import net.cicada.utility.Player.RotateUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.*;
import net.cicada.event.api.Event;
import net.cicada.event.impl.*;
import net.cicada.module.api.Category;
import net.cicada.module.api.Module;
import net.cicada.module.api.ModuleInfo;
import net.cicada.module.setting.impl.BooleanSetting;
import net.cicada.module.setting.impl.ListSetting;
import net.cicada.module.setting.impl.NumberSetting;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector2f;

import java.util.List;

@ModuleInfo(name = "AttackAura", category = Category.Combat, key = Keyboard.KEY_R)
public class AttackAura extends Module {
    // TARGETS
    ListSetting sortMode = new ListSetting("SortMode", "Distance", List.of("FOV", "Health", "Distance"), () -> true, this);
    // RANGE
    NumberSetting aimRange = new NumberSetting("aimRange", 15, 0, 15, 0.1, () -> true, this);
    NumberSetting attackRange = new NumberSetting("AttackRange", 3, 0, 6, 0.1, () -> true, this);
    // POINT SELECTION
    ListSetting selectionMode = new ListSetting("SelectionMode", "Best", List.of("Center", "Best", "Nearest"), () -> true, this);
    BooleanSetting smartSelection = new BooleanSetting("SmartSelection", true, () -> true, this);
    // ROTATIONS
    BooleanSetting rotation = new BooleanSetting("Rotation", true, () -> true, this);
    NumberSetting yawSpeed = new  NumberSetting("yawSpeed", 180, 0, 180, 1, () -> rotation.isValue(), this);
    NumberSetting pitchSpeed = new  NumberSetting("pitchSpeed", 180, 0, 180, 1, () -> rotation.isValue(), this);
    MultiBooleanSetting randomize = new MultiBooleanSetting("Randomize", () -> rotation.isValue(), this)
            .add("Basic", false)
            .add("Smooth", false)
            .add("MixDelta", false);
    NumberSetting minRandomStrength = new NumberSetting("MinRandomStrength", -2, -15, 15, 0.1, () -> rotation.isValue() && randomize.is("Basic"), this);
    NumberSetting maxRandomStrength = new NumberSetting("MaxRandomStrength", 2, -15, 15, 0.1, () -> rotation.isValue() && randomize.is("Basic"), this);
    NumberSetting smoothFactor = new NumberSetting("SmoothFactor", 1.7, 1, 10, 0.01, () -> rotation.isValue() && randomize.is("Smooth"), this);
    BooleanSetting silentRotation = new BooleanSetting("SilentRotation", true, () -> rotation.isValue(), this);
    // CLICKS
    NumberSetting minCPS = new NumberSetting("MinCPS", 20, 0, 20, 1, () -> true, this);
    NumberSetting maxCPS = new NumberSetting("MaxCPS", 20, 0, 20, 1, () -> true, this);
    NumberSetting minRecalculateDelay = new NumberSetting("MinRecalculateDelay", 0, 0, 1000, 1, () -> true, this);
    NumberSetting maxRecalculateDelay = new NumberSetting("MaxRecalculateDelay", 0, 0, 1000, 1, () -> true, this);
    NumberSetting hitRange = new NumberSetting("HitRange", 8, 0, 15, 0.1, () -> true, this);
    // AUTOBLOCK
    public ListSetting autoBlock = new ListSetting("AutoBlock", "None", List.of("None", "Constant", "PreAttack"), () -> true, this);
    // MOVEMENT
    BooleanSetting jumpFix = new BooleanSetting("JumpFix", true, () -> true, this);
    BooleanSetting moveFix = new BooleanSetting("MoveFix", true, () -> true, this);
    BooleanSetting silentMoveFix = new BooleanSetting("SilentMoveFix", true, () -> moveFix.isValue(), this);

    public EntityLivingBase target;
    Vec3 aimPoint;
    Vector2f lastDelta;
    long timer;
    int CPS;
    public boolean isBlocking;


    @Override
    protected void onEnable() {
        RotateUtil.rotation.set(mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch);
        lastDelta = new Vector2f(0, 0);
        super.onEnable();
    }

    @Override
    protected void onDisable() {
        target = null;
        unblock();
        super.onDisable();
    }

    @Override
    public void listen(Event event) {
        switch (event) {
            case AttackEvent attackEvent -> {
                if (autoBlock.is("PreAttack") && mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword) {
                    if (attackEvent.getPriority() == Event.Priority.Low) unblock();
                    if (attackEvent.getPriority() == Event.Priority.High) block();
                }
            }

            case GameLoopEvent ignored -> {
                if (System.currentTimeMillis() - timer > MathUtil.random(minRecalculateDelay.getValue(), maxRecalculateDelay.getValue())) {
                    CPS = (int) MathUtil.random(minCPS.getValue(), maxCPS.getValue());
                    timer = System.currentTimeMillis();
                }
            }

            case JumpEvent jumpEvent -> {
                if (jumpFix.isValue() && target != null && silentRotation.isValue() && rotation.isValue()) {
                    jumpEvent.setRotationYaw(RotateUtil.rotation.getX());
                }
            }

            case LegitClickTimingEvent ignored -> {
                if (target != null) {
                    if (Math.random() * 20 < CPS) {
                        MovingObjectPosition rayTrace = PlayerUtil.rayTrace(RotateUtil.rotation, (float) attackRange.getValue(), 1);
                        if (rayTrace != null && rayTrace.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY && rayTrace.entityHit == target && mc.thePlayer.getPositionEyes(1F).distanceTo(aimPoint) <= attackRange.getValue()) {
                            AttackOrder.sendFixedAttack(mc.thePlayer, target);
                        } else if (mc.thePlayer.getPositionEyes(1F).distanceTo(aimPoint) <= hitRange.getValue()) {
                            mc.clickMouse();
                        }
                    }
                }
            }

            case LookEvent lookEvent -> {
                if (target != null && silentRotation.isValue() && rotation.isValue()) {
                    lookEvent.setRotationYaw(RotateUtil.rotation.getX());
                    lookEvent.setRotationPitch(RotateUtil.rotation.getY());
                }
            }

            case MotionEvent motionEvent -> {
                if (motionEvent.getPriority() == Event.Priority.Low && target != null && silentRotation.isValue() && rotation.isValue()) {
                    motionEvent.setRotationYaw(RotateUtil.rotation.getX());
                    motionEvent.setRotationPitch(RotateUtil.rotation.getY());
                    mc.thePlayer.rotationYawHead = RotateUtil.rotation.getX();
                    mc.thePlayer.rotationPitchHead = RotateUtil.rotation.getY();
                    mc.thePlayer.renderYawOffset = RotateUtil.rotation.getX();
                }
            }

            case MovementEvent movementEvent -> {
                if (moveFix.isValue() && silentMoveFix.isValue() && target != null && silentRotation.isValue() && rotation.isValue()) {
                    MoveUtil.moveFix(movementEvent, RotateUtil.rotation.getX(), MoveUtil.getDirection(mc.thePlayer.rotationYaw, movementEvent.getMoveForward(), movementEvent.getMoveStrafe()));
                }
            }

            case Render2DEvent ignored -> {
                if (target != null && rotation.isValue() && !silentRotation.isValue()) {
                    mc.thePlayer.rotationYaw = (float) MathUtil.interpolate(RotateUtil.lastRotation.getX(), RotateUtil.rotation.getX(), mc.timer.renderPartialTicks);
                    mc.thePlayer.rotationPitch = (float) MathUtil.interpolate(RotateUtil.lastRotation.getY(), RotateUtil.rotation.getY(), mc.timer.renderPartialTicks);
                }
            }

            case StrafeEvent strafeEvent -> {
                if (moveFix.isValue() && target != null && silentRotation.isValue() && rotation.isValue()) {
                    strafeEvent.setRotationYaw(RotateUtil.rotation.getX());
                }
            }

            case TickEvent ignored -> {
                if (mc.currentScreen != null) return;
                CombatManager.updateTarget(sortMode.getValue());
                target = CombatManager.target != null && mc.thePlayer.getDistanceToEntity(CombatManager.target) <= aimRange.getValue() ? CombatManager.target : null;
                if (target != null) {
                    if (rotation.isValue()) {
                        aimPoint = updateRotation(target);
                    }

                    if (!ModuleManager.ATTACK_AURA.autoBlock.is("None") && mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword) block();
                } else {
                    RotateUtil.rotation.set(mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch);
                    lastDelta.set(0, 0);
                    unblock();
                }
            }

            default -> {
            }
        }
    }

    private Vec3 updateRotation(EntityLivingBase target) {
        AxisAlignedBB targetBox = target.getEntityBoundingBox();
        Vec3 targetEyesPos = target.getPositionEyes(1);

        Vec3 aimPoint = new Vec3(0, 0, 0);

        if (selectionMode.getValue().equals("Center")) aimPoint = targetEyesPos;
        else if (selectionMode.getValue().equals("Best")) aimPoint = RotateUtil.bestHitVec(targetBox);
        else if (selectionMode.is("Nearest")) aimPoint = RotateUtil.nearestHitVec(targetBox);

        if (smartSelection.isValue()) {
            Vec3 candidatePoint = null;
            for (double x = targetBox.minX; x <= targetBox.maxX; x += 0.05) {
                for (double z = targetBox.minZ; z <= targetBox.maxZ; z += 0.05) {
                    for (double y = targetBox.minY; y <= targetBox.maxY; y += 0.05) {
                        if (PlayerUtil.rayTrace(new Vec3(x, y, z), 1) == null || PlayerUtil.rayTrace(new Vec3(x, y, z), 1).typeOfHit != MovingObjectPosition.MovingObjectType.ENTITY) continue;
                        if (candidatePoint == null) {
                            candidatePoint = new Vec3(x, y, z);
                            continue;
                        }

                        if (aimPoint.distanceTo(new Vec3(x, y, z)) < aimPoint.distanceTo(candidatePoint)) candidatePoint = new Vec3(x, y, z);
                    }
                }
            }
            if (candidatePoint != null) aimPoint = candidatePoint;
        }

        Vector2f delta = RotateUtil.calcDeltaRotate(aimPoint, (float) yawSpeed.getValue(), (float) pitchSpeed.getValue());
        if (randomize.is("Basic")) delta.translate((float) MathUtil.random(minRandomStrength.getValue(), maxRandomStrength.getValue()), (float) MathUtil.random(minRandomStrength.getValue(), maxRandomStrength.getValue()));
        if (randomize.is("Smooth")) delta.set((float) (delta.getX() / smoothFactor.getValue()), (float) (delta.getY() / smoothFactor.getValue()));
        if (randomize.is("MixDelta")) delta.translate((lastDelta.getX() - delta.getX()) / 1.4F, (lastDelta.getY() - delta.getY()) / 1.4F);
        RotateUtil.rotateWithGCD(delta);
        lastDelta = delta;
        return aimPoint;
    }

    private void block() {
        if (!isBlocking) {
            mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.getHeldItem()));
            isBlocking = true;
        }
    }

    private void unblock() {
        if (isBlocking) {
            mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
            isBlocking = false;
        }
    }
}
