package simplexity.scythe.config;

public enum Message {
    PREFIX("prefix", "<gold><bold>[</bold><yellow>Scythe</yellow><bold>]<reset> "),
    TOGGLE_ENABLED("feedback.toggle-enabled", "<prefix><green>Scythe functionality enabled!</green>"),
    TOGGLE_DISABLED("feedback.toggle-disabled", "<prefix><grey>Scythe functionality <red>disabled</red>!</grey>"),
    CONFIG_RELOADED("feedback.config-reloaded", "<gold>Scythe Config Reloaded!</gold>"),
    UNKNOWN_COMMAND("errors.unknown-command", "<red>Unknown Command</red>"),
    YOUR_TOOL_IS_ALMOST_BROKEN("errors.tool-nearly-broken", "<red>Your tool is nearly broken, scythe functionality disabled until tool is repaired"),
    MUST_HOLD_TOOL("errors.must-hold-tool", "<red>You must be holding a valid tool to be able to automatically replant"),
    NO_PERMISSION("errors.no-permission", "<red>You do not have the required Permission to run this command</red>"),
    NOT_A_PLAYER("errors.not-a-player", "Sorry! This command can only be run by a player"),
    HELP_MAIN("help.main", "<grey>Scythe allows players to harvest grown crops without needing to replant</grey>"),
    HELP_TOGGLE("help.toggle", "<yellow>/scythe toggle \n<grey>• Toggle scythe on or off</grey></yellow>"),
    HELP_RELOAD("help.reload", "<yellow>/scythe reload \n<grey>• Reloads config settings</grey></yellow>");

    private final String path;
    private String message;

    Message(String path, String message) {
        this.path = path;
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
