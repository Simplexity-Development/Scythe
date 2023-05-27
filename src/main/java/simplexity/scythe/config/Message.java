package simplexity.scythe.config;

import org.bukkit.plugin.Plugin;
import simplexity.scythe.Scythe;

public enum Message {
    CONSOLE_PREFIX("[Scythe] "),
    PREFIX("<gold><bold>[</bold><yellow>Scythe</yellow><bold>]<reset>"),
    TOGGLE_ON("<prefix><green>Scythe toggled on!"),
    TOGGLE_OFF("<prefix><red>Scythe toggled off!"),
    UNKNOWN_COMMAND("<red>Unknown Command"),
    CONFIG_RELOAD("<gold>Scythe Config Reloaded!"),
    NO_PERMISSION("<red>You do not have the required Permission to run this command"),
    NOT_A_PLAYER("Sorry! This command can only be run by a player"),
    HELP_MAIN("<grey>Scythe allows players to harvest grown crops without needing to replant"),
    HELP_TOGGLE("<yellow>/scythe toggle \n<grey>• Toggle scythe on or off"),
    HELP_RELOAD("<yellow>/scythe reload \n<grey>• Reloads config settings");
    String message;

    Message(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    private void setMessage(String message) {
        this.message = message;
    }

    public static void reloadMessages() {
        Plugin instance = Scythe.getInstance();
        CONSOLE_PREFIX.setMessage("[Scythe] ");
        PREFIX.setMessage(instance.getConfig().getString("prefix"));
        TOGGLE_ON.setMessage(instance.getConfig().getString("toggle-on"));
        TOGGLE_OFF.setMessage(instance.getConfig().getString("toggle-off"));
        UNKNOWN_COMMAND.setMessage(instance.getConfig().getString("unknown-command"));
        CONFIG_RELOAD.setMessage(instance.getConfig().getString("config-reload"));
        NO_PERMISSION.setMessage(instance.getConfig().getString("no-permission"));
        NOT_A_PLAYER.setMessage(instance.getConfig().getString("not-a-player"));
        HELP_MAIN.setMessage(instance.getConfig().getString("help-main"));
        HELP_TOGGLE.setMessage(instance.getConfig().getString("help-toggle"));
        HELP_RELOAD.setMessage(instance.getConfig().getString("help-reload"));
    }
}
