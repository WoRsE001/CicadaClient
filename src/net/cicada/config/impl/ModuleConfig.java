package net.cicada.config.impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.Setter;
import net.cicada.Cicada;
import net.cicada.module.api.Module;
import net.cicada.module.api.ModuleManager;
import net.cicada.module.setting.Setting;
import net.cicada.module.setting.impl.BooleanSetting;
import net.cicada.module.setting.impl.ListSetting;
import net.cicada.module.setting.impl.MultiBooleanSetting;
import net.cicada.module.setting.impl.NumberSetting;
import net.cicada.utility.Doubles;
import org.lwjgl.input.Keyboard;

import java.io.File;

@Getter @Setter
public class ModuleConfig {
    private final File file;
    private final String name;

    public ModuleConfig(String name) {
        this.name = name;
        this.file = new File(Cicada.CONFIG_DIR, name + ".json");
    }

    public void loadConfig(JsonObject object) {
        for (Module module : ModuleManager.MODULES) {
            if (object.has(module.getName())) {

                JsonObject moduleObject = object.get(module.getName()).getAsJsonObject();

                if (moduleObject.has("State")) {
                    module.setState(moduleObject.get("State").getAsBoolean());
                }

                if (moduleObject.has("Key")) {
                    module.setKey(Keyboard.getKeyIndex(moduleObject.get("Key").getAsString()));
                }

                if (moduleObject.has("Settings")) {
                    JsonObject settingsObject = moduleObject.get("Settings").getAsJsonObject();

                    for (Setting setting : module.getSettings()) {
                        if (settingsObject.has(setting.getName())) {
                            JsonElement configSetting = settingsObject.get(setting.getName());
                            if (setting instanceof NumberSetting numberSetting) {
                                numberSetting.setValue(configSetting.getAsNumber().doubleValue());
                            }
                            if (setting instanceof BooleanSetting booleanSetting) {
                                booleanSetting.setValue(configSetting.getAsBoolean());
                            }
                            if (setting instanceof ListSetting listSetting) {
                                listSetting.setValue(configSetting.getAsString());
                            }
                            if (setting instanceof MultiBooleanSetting multiBooleanSetting) {
                                for (Doubles<String, Boolean> value : multiBooleanSetting.getValues()) {
                                    if (configSetting.getAsJsonObject().get(value.getT()) != null) value.setE(configSetting.getAsJsonObject().get(value.getT()).getAsBoolean());
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public JsonObject saveConfig() {
        JsonObject object = new JsonObject();
        for (Module module : ModuleManager.MODULES) {
            JsonObject moduleObject = new JsonObject();

            moduleObject.addProperty("State", module.isState());
            moduleObject.addProperty("Key", Keyboard.getKeyName(module.getKey()));

            JsonObject settingsObject = new JsonObject();

            for (Setting setting : module.getSettings()) {
                if (setting instanceof NumberSetting numberSetting) {
                    settingsObject.addProperty(setting.getName(), numberSetting.getValue());
                }
                if (setting instanceof BooleanSetting booleanSetting) {
                    settingsObject.addProperty(setting.getName(), booleanSetting.isValue());
                }
                if (setting instanceof ListSetting listSetting) {
                    settingsObject.addProperty(setting.getName(), listSetting.getValue());
                }
                if (setting instanceof MultiBooleanSetting multiBooleanSetting) {
                    JsonObject obj = new JsonObject();
                    for (Doubles<String, Boolean> value : multiBooleanSetting.getValues()) {
                        obj.addProperty(value.getT(), value.getE());
                    }
                    settingsObject.add(multiBooleanSetting.getName(), obj);
                }
            }

            moduleObject.add("Settings", settingsObject);
            object.add(module.getName(), moduleObject);
        }
        return object;
    }
}
