package net.cicada.module.impl.render;

import lombok.Getter;
import lombok.Setter;
import net.cicada.event.api.Event;
import net.cicada.event.impl.Render2DEvent;
import net.cicada.module.api.Category;
import net.cicada.module.api.Module;
import net.cicada.module.api.ModuleInfo;
import net.cicada.module.setting.impl.NumberSetting;
import net.cicada.utility.FixedColor;
import net.cicada.utility.Render.RenderUtil;

import java.util.ArrayList;
import java.util.List;

// испорченно SCWGxD в 20.12.2025:15:32
@ModuleInfo(name = "Notifications", category = Category.Render, state = true)
public class Notifications extends Module {
    private final NumberSetting timeOfDisappearance = new NumberSetting("Time Of Disappearance", 1000, 0, 10000, 1, () -> true, this);

    public List<Notification> notificationsList = new ArrayList<>();

    @Override
    protected void onEnable() {
        notificationsList.clear();
        super.onEnable();
    }

    @Override
    public void listen(Event event) {
        if (event instanceof Render2DEvent) {
            float offsetX = mc.displayWidth / 2F - 200;
            float offsetY = mc.displayHeight / 2F - 18;

            for (Notification notification : notificationsList) {
                RenderUtil.setGlColor(new FixedColor(0, 0, 0, 1));
                RenderUtil.drawRect(offsetX, offsetY, 200, 18);

                mc.fontRendererObj.drawString(notification.module.getName(), offsetX, offsetY, 0xFFFFFFFF);
                mc.fontRendererObj.drawString(notification.message, offsetX, offsetY + mc.fontRendererObj.FONT_HEIGHT, 0xFFFFFFFF);

                offsetY -= 20;
            }

            notificationsList.removeIf(notification -> {
                return System.currentTimeMillis() - notification.startTime > timeOfDisappearance.getValue();
            });
        }
    }

    @Getter @Setter
    public static class Notification {
        private Module module;
        private String message;
        private long startTime = System.currentTimeMillis();

        public Notification(Module module, String message) {
            this.module = module;
            this.message = message;
        }
    }
}
