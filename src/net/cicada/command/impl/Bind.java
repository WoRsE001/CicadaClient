package net.cicada.command.impl;

import net.cicada.command.api.Command;
import net.cicada.command.api.CommandInfo;
import net.cicada.module.api.Module;
import net.cicada.module.api.ModuleManager;
import net.cicada.utility.LoggerUtil;
import org.lwjgl.input.Keyboard;

@CommandInfo(name = "Bind", aliases = {"bind", "bd", "b"}, usage = ".bind <Module> <Key>, .bind list")
public class Bind extends Command {
    @Override
    public void execute(String[] args) {
        if (args.length != 3 && args.length != 2 && args.length != 4) {
            this.sendUsage();
            return;
        }

        if (args.length == 2) {
            if (args[1].equals("list")) {
                LoggerUtil.display("--Bind list--");
                for (Module module : ModuleManager.MODULES) {
                    if (module.getKey() != 0) LoggerUtil.display(module.getName() + ": " + Keyboard.getKeyName(module.getKey()));
                }
            }
        } else if (args.length == 3) {
            Module module = ModuleManager.getModule(args[1]);

            if (module == null) {
                LoggerUtil.display("Sorry, but this Module does not exist!");
                return;
            }

            int key = Keyboard.getKeyIndex(args[2].toUpperCase());
            module.setKey(key);
            LoggerUtil.display("Bound " + module.getName() + " to " + Keyboard.getKeyName(key));
        }
    }
}
