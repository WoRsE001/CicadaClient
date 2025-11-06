package net.cicada.command.impl;

import net.cicada.Cicada;
import net.cicada.command.api.Command;
import net.cicada.command.api.CommandInfo;
import net.cicada.module.api.Module;
import net.cicada.utility.LoggerUtil;

@CommandInfo(name = "Toggle", aliases = {"toggle", "tog", "t"}, usage = ".t <Module>")
public class Toggle extends Command {
    @Override
    public void execute(String[] args) {
        if (args.length != 2) {
            this.sendUsage();
            return;
        }

        Module module = Cicada.INSTANCE.MODULE_MANAGER.getModule(args[1]);

        if (module == null) {
            LoggerUtil.display("Модуля не существует.");
            return;
        }

        module.toggle();
    }
}
