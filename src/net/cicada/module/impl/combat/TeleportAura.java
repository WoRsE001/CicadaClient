package net.cicada.module.impl.combat;

import de.florianmichael.viamcp.fixes.AttackOrder;
import net.cicada.event.api.Event;
import net.cicada.event.impl.MotionEvent;
import net.cicada.event.impl.Render3DEvent;
import net.cicada.module.api.Category;
import net.cicada.module.api.Module;
import net.cicada.module.api.ModuleInfo;
import net.cicada.module.setting.impl.ListSetting;
import net.cicada.module.setting.impl.NumberSetting;
import net.cicada.utility.CombatManager;
import net.cicada.utility.RenderUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@ModuleInfo(name = "TeleportAura", category = Category.Combat)
public class TeleportAura extends Module {
    ListSetting targetSortMode = new ListSetting("TargetSortMode", "Distance", List.of("FOV", "Health", "Distance"), () -> true, this);
    NumberSetting maxTargetHurtTime = new NumberSetting("MaxTargetHurtTime", 0, 0, 10, 1, () -> true, this);

    EntityLivingBase target;
    List<Vec3> path;

    @Override
    public void listen(Event event) {
        if (event instanceof MotionEvent e && e.getPriority() == Event.Priority.Low) {
            CombatManager.updateTarget(this.targetSortMode.getValue());
            this.target = CombatManager.target;

            if (this.target != null && this.target.hurtTime <= this.maxTargetHurtTime.getValue()) {
                this.path = this.getPath(mc.thePlayer, this.target, 4);
                for (Vec3 vec3 : this.path) {
                    mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(vec3.xCoord, vec3.yCoord, vec3.zCoord, true));
                }
                AttackOrder.sendFixedAttack(mc.thePlayer, this.target);
                this.path = this.path.reversed();
                for (Vec3 vec3 : this.path) {
                    mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(vec3.xCoord, vec3.yCoord, vec3.zCoord, true));
                }
            }
        }
    }

    private List<Vec3> getPath(EntityLivingBase from, EntityLivingBase to, float stepLength) {
        List<Vec3> path = new ArrayList<>();
        int steps = (int) Math.ceil(mc.thePlayer.getPositionVector().distanceTo(this.target.getPositionVector()) / stepLength);
        Vec3 diff = to.getPositionVector().subtract(from.getPositionVector());
        Vec3 step = new Vec3(diff.xCoord / steps, diff.yCoord / steps, diff.zCoord / steps);
        for (int i = 1; i <= steps; i++) {
            path.add(new Vec3(from.posX + step.xCoord * i, from.posY + step.yCoord * i, from.posZ + step.zCoord * i));
        }
        return path;
    }
}
