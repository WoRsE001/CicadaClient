package net.cicada.module.impl.combat;

import net.cicada.event.impl.AttackEvent;
import net.cicada.module.setting.impl.ListSetting;
import net.minecraft.entity.EntityLivingBase;
import net.cicada.event.api.Event;
import net.cicada.event.impl.SprintEvent;
import net.cicada.event.impl.TickEvent;
import net.cicada.module.api.Category;
import net.cicada.module.api.Module;
import net.cicada.module.api.ModuleInfo;
import net.cicada.module.setting.impl.NumberSetting;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0CPacketInput;

import java.util.List;

@ModuleInfo(name = "MoreKB", category = Category.Combat)
public class MoreKB extends Module {
    ListSetting mode = new ListSetting("Mode", "LegitFast", List.of("LegitFast", "One"), () -> true, this);
    NumberSetting minStopSprintDelay = new NumberSetting("MinStopSprintDelay", 1, 0, 10, 1, () -> mode.is("LegitFast"), this);
    NumberSetting maxStopSprintDelay = new NumberSetting("MaxStopSprintDelay", 2, 0, 10, 1, () -> mode.is("LegitFast"), this);
    NumberSetting minStartSprintDelay = new NumberSetting("MinStartSprintDelay", 1, 0, 10, 1, () -> true, this);
    NumberSetting maxStartSprintDelay = new NumberSetting("MaxStartSprintDelay", 2, 0, 10, 1, () -> true, this);

    int tickStopSprint = 0, tickStartSprint = 0;
    boolean sprint = false;

    @Override
    protected void onDisable() {
        this.tickStopSprint = this.tickStartSprint = 0;
        super.onDisable();
    }

    @Override
    public void listen(Event event) {
        if (event instanceof AttackEvent e && e.getPriority() == Event.Priority.Low) {
            if (e.getTargetEntity() instanceof EntityLivingBase && ((EntityLivingBase) e.getTargetEntity()).hurtTime == 0) {
                if (mode.is("LegitFast")) {
                    this.tickStopSprint = (int) Math.ceil(Math.random() * (this.maxStopSprintDelay.getValue() - this.minStopSprintDelay.getValue()) + this.minStopSprintDelay.getValue());
                    this.tickStartSprint = (int) Math.ceil(Math.random() * (this.maxStartSprintDelay.getValue() - this.minStartSprintDelay.getValue()) + this.minStartSprintDelay.getValue());
                }

                if (mode.is("One")) {
                    this.tickStartSprint = (int) Math.ceil(Math.random() * (this.maxStartSprintDelay.getValue() - this.minStartSprintDelay.getValue()) + this.minStartSprintDelay.getValue());
                    this.sprint = true;
                }
            }
        }

        if (event instanceof TickEvent) {
            if (mode.is("LegitFast")) {
                if (this.tickStopSprint > 0) this.tickStopSprint--;
            }

            if (mode.is("One")) {
                if (this.tickStartSprint > 0) this.tickStartSprint--;
            }
        }

        if (event instanceof SprintEvent) {
            if (mode.is("LegitFast")) {
                if (this.tickStopSprint <= 0 && this.tickStartSprint != 0 && mc.thePlayer.isSprinting()) {
                    mc.thePlayer.setSprinting(false);
                    this.tickStartSprint--;
                }
            }

            if (mode.is("One")) {
                if (this.sprint && this.tickStartSprint <= 0 && mc.thePlayer.isSprinting()) {
                    mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SPRINTING));
                    this.sprint = false;
                }
            }
        }
    }
}
