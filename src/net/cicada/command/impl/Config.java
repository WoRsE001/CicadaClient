package net.cicada.command.impl;

import net.cicada.Cicada;
import net.cicada.command.api.Command;
import net.cicada.command.api.CommandInfo;
import net.cicada.config.impl.ModuleConfig;
import net.cicada.utility.LoggerUtil;

@CommandInfo(name = "Config", aliases = {"config", "cfg", "setting"}, usage = ".cfg <Action> <Name>")
public class Config extends Command {
    @Override
    public void execute(String[] args) {
        if (args.length != 3) {
            this.sendUsage();
            return;
        }

        ModuleConfig config = new ModuleConfig(args[2]);
        if (args[1].equals("load")) {
            if (Cicada.CONFIG_MANAGER.loadConfig(config)) {
                LoggerUtil.display("Loaded config: " + args[2]);
            } else {
                LoggerUtil.display("Invalid config: " + args[2]);
            }
        } else if (args[1].equals("save")) {
            if (Cicada.CONFIG_MANAGER.saveConfig(config)) {
                LoggerUtil.display("Saved config: " + args[2]);
            } else {
                LoggerUtil.display("Invalid config: " + args[2]);
            }
        }
    }
}
