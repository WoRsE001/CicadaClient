package net.cicada.module.impl.misc;

import net.cicada.module.api.Category;
import net.cicada.module.api.Module;
import net.cicada.module.api.ModuleInfo;
import net.cicada.module.setting.impl.ListSetting;

import java.util.List;

@ModuleInfo(name = "ClientSpoofer", category = Category.Misc, state = true)
public class ClientSpoofer extends Module {
    ListSetting brand = new ListSetting("Brand", "Optifine", List.of("Default", "Forge", "Optifine", "Lunar", "FML", "LabyMod"), () -> true, this);

    public String getBrand() {
        return switch (brand.getValue()) {
            case "Forge" -> "forge";
            case "Optifine" -> "optifine";
            case "Lunar" -> "LunarClient";
            case "FML" -> "fml,forge";
            case "LabyMod" -> "labymod";
            default -> "Vanila";
        };
    }
}
