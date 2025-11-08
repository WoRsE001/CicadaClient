package net.cicada.command.impl;

import net.cicada.command.api.Command;
import net.cicada.command.api.CommandInfo;
import net.cicada.command.api.CommandManager;
import net.cicada.utility.LoggerUtil;

@CommandInfo(name = "Help", aliases = {"help"}, usage = ".help, .help <Command>")
public class Help extends Command {
    @Override
    public void execute(String[] args) {
        if (args.length != 1 && args.length != 2) {
            return;
        }

        if (args.length == 1) {
            LoggerUtil.display("--Help info--");
            for (Command command : CommandManager.COMMANDS) {
                LoggerUtil.display(command.getName() + ": " + command.getUsage());
            }
        } else if (args.length == 2) {
            Command command = CommandManager.getCommand(args[1]);
            LoggerUtil.display("--" + command.getName() + " info--");
            StringBuilder aliases = new StringBuilder();
            for (String alias : command.getAliases()) {
                aliases.append(alias).append(", ");
            }
            aliases.setLength(aliases.length() - 2);
            LoggerUtil.display("Aliases: " + aliases);
            LoggerUtil.display("Usage: " + command.getUsage());
        }
    }
}
