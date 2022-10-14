package adhdmc.scythe.config;

import org.bukkit.permissions.PermissionDefault;
import org.bukkit.util.permissions.DefaultPermissions;

public enum ScythePermission {
    USE("scythe.use"),
    TOGGLE_COMMAND("scythe.use.toggle"),
    RELOAD_COMMAND("scythe.reload");
    final String permission;
    ScythePermission(String permission) {
        this.permission = permission;
    }
    public String getPermission() { return this.permission; }

    public static void registerPermissions(){
        DefaultPermissions.registerPermission(USE.getPermission(), "Allows user to use configured scythe functionality", PermissionDefault.OP);
        DefaultPermissions.registerPermission(TOGGLE_COMMAND.getPermission(), "Allows user to toggle scythe functionality off or on", PermissionDefault.OP);
        DefaultPermissions.registerPermission(RELOAD_COMMAND.getPermission(), "Allows user to reload scythe plugin config", PermissionDefault.OP);
    }
}
