package net.cicada.module.impl.movement;

import net.cicada.event.api.Event;
import net.cicada.event.impl.BlockShapeEvent;
import net.cicada.event.impl.MotionEvent;
import net.cicada.event.impl.UpdateEvent;
import net.cicada.module.api.Category;
import net.cicada.module.api.Module;
import net.cicada.module.api.ModuleInfo;
import net.cicada.module.setting.impl.BooleanSetting;
import net.cicada.module.setting.impl.NumberSetting;
import org.lwjgl.input.Keyboard;

@ModuleInfo(name = "Spider", category = Category.Movement)
public class Spider extends Module {
    BooleanSetting fast = new BooleanSetting("Fast", false, () -> true, this);
    NumberSetting motionY = new NumberSetting("motionY", 0.6, 0, 5, 0.01, () -> fast.isValue(), this);

    @Override
    public void listen(Event event) {
        if (event instanceof BlockShapeEvent e && e.getBoundingBox() != null) {
            if (e.getBoundingBox().minY >= mc.thePlayer.posY - 0.99 || (mc.thePlayer.isSneaking() && mc.thePlayer.onGround))
                e.setBoundingBox(e.getBoundingBox().expand(-0.01, 0.0, -0.01));
        }

        if (fast.isValue() && mc.thePlayer.isCollidedHorizontally && mc.gameSettings.keyBindSneak.isKeyDown()) {
            if (event instanceof MotionEvent e && e.getPriority() == Event.Priority.Low && mc.thePlayer.isCollidedHorizontally && mc.gameSettings.keyBindSneak.isKeyDown()) {
                e.setOnGround(true);
            }

            if (event instanceof UpdateEvent && mc.thePlayer.isCollidedHorizontally && mc.gameSettings.keyBindSneak.isKeyDown()) {
                mc.thePlayer.motionY = motionY.getValue();
            }
        }
    }
}
