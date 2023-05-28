package simplexity.scythe.config;

import org.bukkit.permissions.Permission;

public enum ScythePermission {
    USE(new Permission("scythe.use")),
    USE_HARVEST(new Permission("scythe.use.harvest")),
    USE_REPLANT(new Permission("scythe.use.replant")),
    TOGGLE_COMMAND(new Permission("scythe.toggle")),
    RELOAD_COMMAND(new Permission("scythe.reload"));
    final Permission permission;

    ScythePermission(Permission permission) {
        this.permission = permission;
    }

    public Permission getPermission() {
        return this.permission;
    }
}
