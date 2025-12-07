package net.cicada;

import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import de.florianmichael.vialoadingbase.ViaLoadingBase;
import de.florianmichael.viamcp.ViaMCP;
import lombok.experimental.FieldDefaults;
import lombok.experimental.UtilityClass;
import net.cicada.gui.altmanager.AltManager;
import net.cicada.gui.zamorozkaClickGui.ZamorozkaClickGui;
import net.minecraft.client.Minecraft;
import net.cicada.command.api.CommandManager;
import net.cicada.config.api.ConfigManager;
import net.cicada.event.api.EventCaller;
import net.cicada.module.api.ModuleManager;
import org.lwjgl.opengl.Display;

import java.io.File;

@UtilityClass @FieldDefaults(makeFinal = true)
public class Cicada {
    public String name = "Cicada";
    public String version = "1.1.6";

    public File MAIN_DIR = new File(Minecraft.getMinecraft().mcDataDir, name);
    public File CONFIG_DIR = new File(MAIN_DIR, "configs");
    public ModuleManager MODULE_MANAGER = new ModuleManager();
    public CommandManager COMMAND_MANAGER = new CommandManager();
    public ConfigManager CONFIG_MANAGER = new ConfigManager();
    public AltManager ALT_MANAGER = new AltManager();

    public void init() {
        Display.setTitle(name + " " + version);

        MAIN_DIR.mkdirs();
        CONFIG_DIR.mkdirs();

        ViaMCP.create();
        ViaMCP.INSTANCE.initAsyncSlider();
        ViaLoadingBase.getInstance().reload(ViaLoadingBase.PROTOCOLS.getFirst());

        EventCaller.register(MODULE_MANAGER);
        EventCaller.register(COMMAND_MANAGER);
        ZamorozkaClickGui.init();
    }
}
