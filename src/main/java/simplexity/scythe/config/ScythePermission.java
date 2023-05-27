package simplexity.scythe.config;

public enum ScythePermission {
    USE("scythe.use"),
    TOGGLE_COMMAND("scythe.toggle"),
    RELOAD_COMMAND("scythe.reload");
    final String permission;

    ScythePermission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return this.permission;
    }
}
