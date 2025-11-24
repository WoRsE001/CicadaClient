package net.cicada.module.impl.combat;

import de.florianmichael.viamcp.fixes.AttackOrder;
import net.cicada.module.api.ModuleManager;
import net.cicada.utility.Player.CombatManager;
import net.cicada.utility.Player.MovementUtil;
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
    BooleanSetting rotation = new BooleanSetting("Rotation", true, () -> true, this);
    NumberSetting yawSpeed = new  NumberSetting("yawSpeed", 180, 0, 180, 1, () -> this.rotation.isValue(), this);
    NumberSetting pitchSpeed = new  NumberSetting("pitchSpeed", 180, 0, 180, 1, () -> this.rotation.isValue(), this);
    ListSetting randomize = new ListSetting("Randomize", "None", List.of("None", "Basic"), () -> this.rotation.isValue(), this);
    NumberSetting randomStrength = new NumberSetting("RandomStrength", 0.4, 0, 2, 0.1, () -> this.rotation.isValue() && randomize.is("Basic"), this);
    BooleanSetting silentRotation = new BooleanSetting("SilentRotation", true, () -> this.rotation.isValue(), this);
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
            if (this.target != null) {
                if (this.rotation.isValue()) {
                    this.aimPoint = this.updateRotation(this.target);
                    if (!silentRotation.isValue()) {
                        mc.thePlayer.rotationYaw = RotateUtil.rotation.getX();
                        mc.thePlayer.rotationPitch = RotateUtil.rotation.getY();
                    }
                }
                if (!ModuleManager.ATTACK_AURA.autoBlock.is("None") && mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword) this.block();
            } else {
                this.unblock();
            }
        }

        if (event instanceof LegitClickTimingEvent) {
            if (this.target != null) {
                if (Math.random() < this.cps.getValue() / 20) {
                    if ((mc.thePlayer.canEntityBeSeen(this.target) || this.rotation.isValue()) && mc.thePlayer.getPositionEyes(1F).distanceTo(this.aimPoint) <= this.attackRange.getValue()) {
                        AttackOrder.sendFixedAttack(mc.thePlayer, this.target);
                    }
                }
            }
        }

        if (this.target != null && silentRotation.isValue() && this.rotation.isValue()) {
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

                if (event instanceof MovementEvent e && this.silentMoveFix.isValue()) {
                    MovementUtil.moveFix(e, MovementUtil.getDirection(mc.thePlayer.rotationYaw, e.getMoveForward(), e.getMoveStrafe()));
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
                        if (PlayerUtil.rayTrace(new Vec3(x, y, z), 1) == null || PlayerUtil.rayTrace(new Vec3(x, y, z), 1).typeOfHit != MovingObjectPosition.MovingObjectType.ENTITY) continue;
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

        if (this.randomize.is("Basic")) aimPoint = aimPoint.add(new Vec3((Math.random() * 2 - 1) * this.randomStrength.getValue(), (Math.random() * 2 - 1) * this.randomStrength.getValue(), (Math.random() * 2 - 1) * this.randomStrength.getValue()));

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
