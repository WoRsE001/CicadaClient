package net.cicada.config.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Getter;
import net.cicada.config.impl.ModuleConfig;

import java.io.*;

@Getter
public class ConfigManager {

    public final ModuleConfig setting = new ModuleConfig("default");
    public final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public ConfigManager() {
        loadConfigs();
    }

    public boolean loadConfig(Config config) {
        if (config == null) {
            return false;
        }
        try {
            JsonObject object = new JsonParser().parse(new FileReader(config.getFile())).getAsJsonObject();
            config.loadConfig(object);
            return true;
        } catch (FileNotFoundException e) {
            return false;
        }
    }

    public boolean saveConfig(Config config) {
        if (config == null) {
            return false;
        }
        String contentPrettyPrint = new GsonBuilder().setPrettyPrinting().create().toJson(config.saveConfig());
        config.saveConfig();
        try {
            FileWriter writer = new FileWriter(config.getFile());
            writer.write(contentPrettyPrint);
            writer.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public void saveConfigs() {
        saveConfig(setting);
    }

    public void loadConfigs() {
        loadConfig(setting);
    }
}
