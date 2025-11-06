package net.cicada.config.api;

import com.google.gson.JsonObject;
import lombok.Getter;
import net.cicada.Cicada;

import java.io.File;

@Getter
public class Config {
    private final File file;
    private final String name;

    public Config(String name) {
        this.name = name;
        this.file = new File(Cicada.CONFIG_DIR, name + ".json");
    }

    public void loadConfig(JsonObject object){

    }

    public JsonObject saveConfig(){
        return null;
    }
}

