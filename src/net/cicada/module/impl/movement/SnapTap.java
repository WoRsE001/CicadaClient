package net.cicada.module.impl.movement;

import net.cicada.event.api.Event;
import net.cicada.event.impl.MovementInputEvent;
import net.cicada.module.api.Category;
import net.cicada.module.api.Module;
import net.cicada.module.api.ModuleInfo;

import java.util.ArrayList;
import java.util.List;

@ModuleInfo(name = "SnapTap", category = Category.Movement, state = true)
public class SnapTap extends Module {
    List<String> lastPressedKeyForward = new ArrayList<>();
    List<String> lastPressedKeyStrafe = new ArrayList<>();

    @Override
    public void listen(Event event) {

    }
}
