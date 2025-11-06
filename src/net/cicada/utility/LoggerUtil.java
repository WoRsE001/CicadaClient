package net.cicada.utility;

import net.cicada.Cicada;
import net.minecraft.util.ChatComponentText;

public class LoggerUtil implements Access {
    public static void display(String message) {
        mc.ingameGUI.getChatGUI().printChatMessage(new ChatComponentText("§5[" + Cicada.name + "]  §f" + message));
    }
}
