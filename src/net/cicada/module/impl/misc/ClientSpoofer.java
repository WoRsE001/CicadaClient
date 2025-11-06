package net.cicada.module.impl.misc;

import net.cicada.module.api.Category;
import net.cicada.module.api.Module;
import net.cicada.module.api.ModuleInfo;
import net.cicada.module.setting.impl.ListSetting;

import java.util.List;

@ModuleInfo(name = "ClientSpoofer", category = Category.Misc, state = true)
public class ClientSpoofer extends Module {
    ListSetting brand = new ListSetting("Brand", "Lunar", List.of("Default", "Forge", "Optifine", "Lunar", "FML", "LabyMod"), () -> true, this);

    public String getBrand() {
        if (brand.getValue().equals("Forge")) {
            return "forge";
        } else if (brand.getValue().equals("Optifine")) {
            return "optifine";
        } else if (brand.getValue().equals("Lunar")) {
            return "LunarClient";
        } else if (brand.getValue().equals("FML")) {
            return "fml,forge";
        } else if (brand.getValue().equals("LabyMod")) {
            return "labymod";
        } else {
            return "Vanila";
        }
    }
}
