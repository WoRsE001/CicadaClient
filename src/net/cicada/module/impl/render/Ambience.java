package net.cicada.module.impl.render;

import net.cicada.event.api.Event;
import net.cicada.event.impl.TickEvent;
import net.cicada.module.api.Category;
import net.cicada.module.api.Module;
import net.cicada.module.api.ModuleInfo;
import net.cicada.module.setting.impl.ListSetting;

import java.util.List;

@ModuleInfo(name = "Ambience", category = Category.Render)
public class Ambience extends Module {
    public ListSetting weather = new ListSetting("Weather", "Snow", List.of("Sunny", "Rain", "Snow"), () -> true, this);

    @Override
    public void listen(Event event) {
        if (event instanceof TickEvent) {
            if (weather.getValue().equals("Sunny")) {
                mc.theWorld.setRainStrength(0);
                mc.theWorld.setThunderStrength(0);
            } else if (weather.getValue().equals("Rain") || weather.getValue().equals("Snow")) {
                mc.theWorld.setRainStrength(1);
                mc.theWorld.setThunderStrength(0);
            }
        }
    }
}
