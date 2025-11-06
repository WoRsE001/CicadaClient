package net.cicada.module.api;

import lombok.Getter;
import lombok.Setter;
import net.cicada.event.api.Event;
import net.cicada.event.api.EventListener;
import net.cicada.module.setting.Setting;
import net.cicada.utility.Access;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class Module implements EventListener, Access {
    private final ModuleInfo info = getClass().getAnnotation(ModuleInfo.class);
    private final String name = info.name();
    private final Category category = info.category();
    private int key = info.key();
    private boolean state = info.state();
    private boolean showInArrayList = true;
    private List<Setting> settings = new ArrayList<>();

    protected void onEnable() {
    }

    protected void onDisable() {
    }

    @Override
    public void listen(Event event) {
    }

    @Override
    public boolean listen() {
        return state && mc.thePlayer != null && mc.theWorld != null;
    }

    public void toggle() {
        setState(!state);
    }

    public void setState(boolean state) {
        if (this.state != state) {
            this.state = state;
            if (state) {
                onEnable();
            } else {
                onDisable();
            }
        }
    }
}
