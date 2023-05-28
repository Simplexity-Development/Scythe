package simplexity.scythe.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;

public abstract class SubCommand {
    private final Permission permission;

    public SubCommand(Permission permission) {
        this.permission = permission;
    }

    public abstract void execute(CommandSender sender, String[] args);

    public Permission getPermission() {
        return permission;
    }
}
