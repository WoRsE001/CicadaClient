package net.cicada.command.api;

import lombok.Getter;
import net.cicada.utility.Access;
import net.cicada.utility.LoggerUtil;

@Getter
public class Command implements Access {
    private String name = this.getClass().getAnnotation(CommandInfo.class).name();
    private String[] aliases = this.getClass().getAnnotation(CommandInfo.class).aliases();
    private String usage = this.getClass().getAnnotation(CommandInfo.class).usage();

    public void execute(String[] args) {
    }

    public void sendUsage() {
        LoggerUtil.display("Usage: " + usage);
    }
}
