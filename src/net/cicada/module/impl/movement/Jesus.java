package net.cicada.module.impl.movement;

import net.cicada.module.api.Category;
import net.cicada.module.api.Module;
import net.cicada.module.api.ModuleInfo;
import net.cicada.module.setting.impl.ListSetting;
import net.cicada.event.impl.UpdateEvent;

@ModuleInfo(name = "Jesus", category = Category.Movement)
public class Jesus extends Module {
  ListSetting mode = new ListSetting("Mode", "Polar", List.of("Polar", "Motion"), () -> true, this);

  @Override
  public void listen(Event event) {
    if (event instanceof UpdateEvent) {
      if (mode.getValue().equals("Polar") {
        // завтра сделаю
      }
    }
  }
}
