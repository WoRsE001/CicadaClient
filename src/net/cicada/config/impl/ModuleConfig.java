package net.cicada.config.impl;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.cicada.config.api.Config;
import net.cicada.module.api.Module;
import net.cicada.module.api.ModuleManager;
import net.cicada.module.setting.Setting;
import net.cicada.module.setting.impl.BooleanSetting;
import net.cicada.module.setting.impl.ListSetting;
import net.cicada.module.setting.impl.NumberSetting;

public class ModuleConfig extends Config {
    public ModuleConfig(String name) {
        super(name);
    }

    @Override
    public void loadConfig(JsonObject object) {
        for (net.cicada.module.api.Module module : ModuleManager.MODULES) {
            if (object.has(module.getName())) {

                JsonObject moduleObject = object.get(module.getName()).getAsJsonObject();

                if (moduleObject.has("State")) {
                    module.setState(moduleObject.get("State").getAsBoolean());
                }

                if (moduleObject.has("Key")) {
                    module.setKey(moduleObject.get("Key").getAsInt());
                }

                if (moduleObject.has("Values")) {
                    JsonObject valuesObject = moduleObject.get("Values").getAsJsonObject();

                    for (Setting setting : module.getSettings()) {
                        if (valuesObject.has(setting.getName())) {
                            JsonElement theValue = valuesObject.get(setting.getName());
                            if (setting instanceof NumberSetting numberSetting) {
                                numberSetting.setValue(theValue.getAsNumber().doubleValue());
                            }
                            if (setting instanceof BooleanSetting booleanSetting) {
                                booleanSetting.setValue(theValue.getAsBoolean());
                            }
                            if (setting instanceof ListSetting listSetting) {
                                listSetting.setValue(theValue.getAsString());
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public JsonObject saveConfig() {
        JsonObject object = new JsonObject();
        for (Module module : ModuleManager.MODULES) {
            JsonObject moduleObject = new JsonObject();

            moduleObject.addProperty("State", module.isState());
            moduleObject.addProperty("Key", module.getKey());

            JsonObject valuesObject = new JsonObject();

            for (Setting setting : module.getSettings()) {
                if (setting instanceof NumberSetting numberSetting) {
                    valuesObject.addProperty(setting.getName(), numberSetting.getValue());
                }
                if (setting instanceof BooleanSetting booleanSetting) {
                    valuesObject.addProperty(setting.getName(), booleanSetting.isValue());
                }
                if (setting instanceof ListSetting listSetting) {
                    valuesObject.addProperty(setting.getName(), listSetting.getValue());
                }
            }

            moduleObject.add("Values", valuesObject);
            object.add(module.getName(), moduleObject);
        }
        return object;
    }
}
