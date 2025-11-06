package net.cicada.command.impl;

import net.cicada.command.api.Command;
import net.cicada.command.api.CommandInfo;
import net.cicada.module.api.Module;
import net.cicada.module.api.ModuleManager;
import net.cicada.utility.LoggerUtil;
import org.lwjgl.input.Keyboard;

@CommandInfo(name = "Bind", aliases = {"bind"}, usage = ".bind <Module> <Key>")
public class Bind extends Command {
    @Override
    public void execute(String[] args) {
        if (args.length != 3) {
            this.sendUsage();
            return;
        }

        Module module = ModuleManager.getModule(args[1]);

        if(module == null)  {
            LoggerUtil.display("Sorry, but this Module does not exist!");
            return;
        }

        int key = Keyboard.getKeyIndex(args[2].toUpperCase());
        module.setKey(key);
    }
}
