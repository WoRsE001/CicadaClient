package net.cicada.module.impl.combat;

import de.florianmichael.viamcp.fixes.AttackOrder;
import net.cicada.module.api.ModuleManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
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
import net.cicada.utility.*;
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
    NumberSetting attackThroughWallsRange = new NumberSetting("AttackThroughWallsRange", 0, 0, 6, 0.1, () -> true, this);
    // POINT SELECTION
    ListSetting selectionMode = new ListSetting("SelectionMode", "Best", List.of("Center", "Best", "Nearest"), () -> true, this);
    BooleanSetting smartSelection = new BooleanSetting("SmartSelection", true, () -> true, this);
    // ROTATIONS
    NumberSetting yawSpeed = new  NumberSetting("yawSpeed", 180, 0, 180, 1, () -> true, this);
    NumberSetting pitchSpeed = new  NumberSetting("pitchSpeed", 180, 0, 180, 1, () -> true, this);
    BooleanSetting silentRotation = new BooleanSetting("SilentRotation", true, () -> true, this);
    // CLICKS
    NumberSetting cps = new NumberSetting("CPS", 20, 0, 20, 1, () -> true, this);
    NumberSetting hitRange = new NumberSetting("HitRange", 8, 0, 15, 0.1, () -> true, this);
    NumberSetting hitFovYaw = new NumberSetting("HitFovYaw", 180, 0,180, 1, () -> true, this);
    NumberSetting hitFovPitch = new NumberSetting("HitFovPitch", 180, 0, 180, 1, () -> true, this);
    // AUTOBLOCK
    public ListSetting autoBlock = new ListSetting("AutoBlock", "None", List.of("None", "Constant", "PreAttack"), () -> true, this);
    // MOVEMENT
    BooleanSetting jumpFix = new BooleanSetting("JumpFix", true, () -> true, this);
    BooleanSetting moveFix = new BooleanSetting("MoveFix", true, () -> true, this);
    BooleanSetting silentMoveFix = new BooleanSetting("SilentMoveFix", true, () -> moveFix.isValue(), this);

    public EntityLivingBase target;
    Vec3 aimPoint;
    public boolean isBlocking;

    @Override
    protected void onEnable() {
        RotateUtil.setRotation(mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch);
        super.onEnable();
    }

    @Override
    protected void onDisable() {
        this.target = null;
        this.unblock();
        super.onDisable();
    }

    @Override
    public void listen(Event event) {
        if (this.autoBlock.is("PreAttack") && event instanceof AttackEvent e && mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword) {
            if (e.getPriority() == Event.Priority.Low) this.unblock();
            if (e.getPriority() == Event.Priority.High) this.block();
        }

        if (event instanceof TickEvent) {
            if (mc.currentScreen != null) return;
            CombatManager.updateTarget(sortMode.getValue());
            this.target = CombatManager.target != null && mc.thePlayer.getDistanceToEntity(CombatManager.target) <= this.aimRange.getValue() ? CombatManager.target : null;
            if (this.target != null) this.aimPoint = this.updateRotation(this.target);

            if (this.target != null) {
                if (!silentRotation.isValue()) {
                    mc.thePlayer.rotationYaw = RotateUtil.rotation.getX();
                    mc.thePlayer.rotationPitch = RotateUtil.rotation.getY();
                }

                if (!ModuleManager.ATTACK_AURA.autoBlock.is("None") && mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword) this.block();
            } else {
                this.unblock();
            }
        }

        if (event instanceof LegitClickTimingEvent) {
            if (this.target != null) {
                if (Math.random() < this.cps.getValue() / 20) {
                    double range = mc.thePlayer.rayTrace(6, 1, RotateUtil.rotation.getX(), RotateUtil.rotation.getY()).typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY ? this.attackRange.getValue() : this.attackThroughWallsRange.getValue();
                    Vector2f FOVToTarget = CombatManager.FOVToTarget(this.target, RotateUtil.rotation.getX(), RotateUtil.rotation.getY());
                    if (RotateUtil.isLookingAtEntity(this.target, RotateUtil.rotation, range)) {
                        AttackOrder.sendFixedAttack(mc.thePlayer, this.target);
                    } else if (FOVToTarget.getX() <= hitFovYaw.getValue() && FOVToTarget.getY() <= hitFovPitch.getValue() && mc.thePlayer.getPositionEyes(1).distanceTo(this.aimPoint) <= this.hitRange.getValue()) {
                        mc.clickMouse();
                    }
                }
            }


        }

        if (this.target != null && silentRotation.isValue()) {
            if (event instanceof LookEvent e) {
                e.setRotationYaw(RotateUtil.rotation.getX());
                e.setRotationPitch(RotateUtil.rotation.getY());
            }

            if (event instanceof MotionEvent e) {
                if (e.getPriority() == Event.Priority.Low) {
                    e.setRotationYaw(RotateUtil.rotation.getX());
                    e.setRotationPitch(RotateUtil.rotation.getY());
                } else if (e.getPriority() == Event.Priority.High) {
                    mc.thePlayer.rotationYawHead = RotateUtil.rotation.getX();
                    mc.thePlayer.rotationPitchHead = RotateUtil.rotation.getY();
                    mc.thePlayer.renderYawOffset = RotateUtil.rotation.getX();
                }
            }

            if (event instanceof JumpEvent e && this.jumpFix.isValue()) {
                e.setRotationYaw(RotateUtil.rotation.getX());
            }

            if (this.moveFix.isValue()) {
                if (event instanceof StrafeEvent e) {
                    e.setRotationYaw(RotateUtil.rotation.getX());
                }

                if (event instanceof MovementEvent e && this.silentMoveFix.isValue()) {
                    MovementUtil.fixMovement(e, RotateUtil.rotation.getX());
                }
            }
        }
    }

    private Vec3 updateRotation(EntityLivingBase target) {
        AxisAlignedBB targetBox = target.getEntityBoundingBox();
        Vec3 targetEyesPos = target.getPositionEyes(1);

        Vec3 aimPoint = new Vec3(0, 0, 0);

        if (selectionMode.getValue().equals("Center")) aimPoint = targetEyesPos;
        else if (selectionMode.getValue().equals("Best")) aimPoint = RotateUtil.bestHitVec(targetBox);

        if (this.smartSelection.isValue()) {
            Vec3 candidatePoint = null;
            for (double x = targetBox.minX; x <= targetBox.maxX; x += 0.05) {
                for (double z = targetBox.minZ; z <= targetBox.maxZ; z += 0.05) {
                    for (double y = targetBox.minY; y <= targetBox.maxY; y += 0.05) {
                        if (mc.thePlayer.rayTrace(1, new Vec3(x, y, z)) == null || mc.thePlayer.rayTrace(1, new Vec3(x, y, z)).typeOfHit != MovingObjectPosition.MovingObjectType.ENTITY) continue;
                        if (candidatePoint == null) {
                            candidatePoint = new Vec3(x, y, z);
                            continue;
                        }
                        if (selectionMode.getValue().equals("Center")) {
                            if (targetEyesPos.distanceTo(new Vec3(x, y, z)) < targetEyesPos.distanceTo(candidatePoint))
                                candidatePoint = new Vec3(x, y, z);
                        } else if (selectionMode.getValue().equals("Best")) {
                            if (RotateUtil.bestHitVec(targetBox).distanceTo(new Vec3(x, y, z)) < RotateUtil.bestHitVec(targetBox).distanceTo(candidatePoint))
                                candidatePoint = new Vec3(x, y, z);
                        }
                    }
                }
            }
            if (candidatePoint != null) aimPoint = candidatePoint;
        }

        RotateUtil.rotateTo(aimPoint, (float) this.yawSpeed.getValue(), (float) this.pitchSpeed.getValue());
        return aimPoint;
    }

    private void block() {
        if (!this.isBlocking) {
            mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.getHeldItem()));
            this.isBlocking = true;
        }
    }

    private void unblock() {
        if (this.isBlocking) {
            mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
            this.isBlocking = false;
        }
    }
}
