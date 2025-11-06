package net.cicada;

import de.florianmichael.viamcp.ViaMCP;
import net.minecraft.client.Minecraft;
import net.cicada.command.api.CommandManager;
import net.cicada.config.api.ConfigManager;
import net.cicada.event.api.EventCaller;
import net.cicada.module.api.ModuleManager;
import org.lwjgl.opengl.Display;

import java.io.File;

public class Cicada {
    public static final String name = "Cicada";
    public static final String version = "1.1.0";

    public static Cicada INSTANCE = new Cicada();
    public static File MAIN_DIR = new File(Minecraft.getMinecraft().mcDataDir, name);
    public static File CONFIG_DIR = new File(MAIN_DIR, "configs");
    public static final ModuleManager MODULE_MANAGER = new ModuleManager();
    public static final CommandManager COMMAND_MANAGER = new CommandManager();
    public static final ConfigManager CONFIG_MANAGER = new ConfigManager();

    public static void init() {
        Display.setTitle(name + " " + version);

        ViaMCP.create();
        ViaMCP.INSTANCE.initAsyncSlider();

        EventCaller.register(MODULE_MANAGER);
        EventCaller.register(COMMAND_MANAGER);
    }
}
