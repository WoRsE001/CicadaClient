package net.cicada.module.impl.movement;

import net.cicada.event.api.Event;
import net.cicada.event.impl.BlockShapeEvent;
import net.cicada.module.api.Category;
import net.cicada.module.api.Module;
import net.cicada.module.api.ModuleInfo;
import net.cicada.module.setting.impl.ListSetting;
import net.minecraft.block.BlockLiquid;
import net.minecraft.util.AxisAlignedBB;

import java.util.List;

@ModuleInfo(name = "Jesus", category = Category.Movement)
public class LiquidWalk extends Module {
  ListSetting mode = new ListSetting("Mode", "Polar", List.of("FullBlock", "Polar"), () -> true, this);

  @Override
  public void listen(Event event) {
      if (event instanceof BlockShapeEvent e) {
          if (this.mode.is("FullBlock")) {
              if (e.getState().getBlock() instanceof BlockLiquid)
                  e.setBoundingBox(new AxisAlignedBB(0, 0, 0, 1, 1, 1).offset(e.getPos().getX(), e.getPos().getY(), e.getPos().getZ()));
          }
      }
  }
}
