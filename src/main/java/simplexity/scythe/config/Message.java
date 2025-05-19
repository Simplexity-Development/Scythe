package simplexity.scythe.config;

public enum Message {
    PREFIX("prefix", "<gold><bold>[</bold><yellow>Scythe</yellow><bold>]<reset> "),
    TOGGLE_ENABLED("feedback.toggle-enabled", "<prefix><green>Scythe functionality enabled!</green>"),
    TOGGLE_DISABLED("feedback.toggle-disabled", "<prefix><grey>Scythe functionality <red>disabled</red>!</grey>"),
    CONFIG_RELOADED("feedback.config-reloaded", "<gold>Scythe Config Reloaded!</gold>"),
    SCYTHE_OBTAINED("feedback.item.obtained", "<green>You have obtained a scythe!</green>"),
    SCYTHE_GIVEN("feedback.item.given", "<green>You have given <name> a scythe</green>"),
    SCYTHE_RECEIVED("feedback.item.received", "<green><name> has given you a scythe!</green>"),
    ERROR_CUSTOM_ITEM_NOT_ENABLED("errors.custom-item-not-enabled", "<red>Sorry, custom items are not enabled in the config, and thus this command is not able to be used.</red>"),
    ERROR_UNKNOWN_COMMAND("errors.unknown-command", "<red>Unknown Command</red>"),
    ERROR_YOUR_TOOL_IS_ALMOST_BROKEN("errors.tool-nearly-broken", "<red>Your tool is nearly broken, scythe functionality disabled until tool is repaired"),
    ERROR_MUST_HOLD_TOOL("errors.must-hold-tool", "<red>You must be holding a valid tool to be able to automatically replant"),
    ERROR_NO_PERMISSION("errors.no-permission", "<red>You do not have the required Permission to run this command</red>"),
    ERROR_NOT_A_PLAYER("errors.not-a-player", "Sorry! This command can only be run by a player"),
    ERROR_COOLDOWN_NOT_EXPIRED("errors.cooldown", "<red>Sorry, That command is on cooldown for<time> longer</red>"),
    INSERT_TIME_FORMAT_GROUP("insert.time-format.group", "<hour><min><sec>"),
    INSERT_TIME_FORMAT_HOUR("insert.time-format.hour", " <yellow><count></yellow> hour"),
    INSERT_TIME_FORMAT_HOURS("insert.time-format.hours", " <yellow><count></yellow> hours"),
    INSERT_TIME_FORMAT_MINUTE("insert.time-format.minute", " <yellow><count></yellow> min"),
    INSERT_TIME_FORMAT_MINUTES("insert.time-format.minute", " <yellow><count></yellow> mins"),
    INSERT_TIME_FORMAT_SECOND("insert.time-format.second", " <yellow><count></yellow> sec"),
    INSERT_TIME_FORMAT_SECONDS("insert.time-format.second", " <yellow><count></yellow> secs"),
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
