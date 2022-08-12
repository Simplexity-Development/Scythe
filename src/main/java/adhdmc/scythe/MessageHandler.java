package adhdmc.scythe;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MessageHandler {
    public static FileConfiguration config = Scythe.plugin.getConfig();
    public static boolean requireHoe;
    private static final HashMap<Message, String> messageMap = new HashMap<>();
    public enum Message {
        PREFIX, TOGGLE_ON, TOGGLE_OFF, UNKNOWN_COMMAND, CONFIG_RELOAD, NO_PERMISSION,
        NOT_A_PLAYER, HELP_MAIN, HELP_TOGGLE, HELP_RELOAD
    }
    public static void configParser(){
        FileConfiguration config = Scythe.plugin.getConfig();
        messageMap.clear();
        requireHoe = false;
        requireHoe = config.getBoolean("require-hoe", false);
        messageMap.put(Message.PREFIX,
                config.getString("prefix","<gold><bold>[</bold><yellow>Scythe</yellow><bold>]<reset>"));
        messageMap.put(Message.TOGGLE_ON,
                config.getString("toggle-on", "<green>Scythe toggled on!"));
        messageMap.put(Message.TOGGLE_OFF,
                config.getString("toggle-off", "<red>Scythe toggled off!"));
        messageMap.put(Message.UNKNOWN_COMMAND,
                config.getString("unknown-command", "<red>Unknown Command"));
        messageMap.put(Message.CONFIG_RELOAD,
                config.getString("config-reload", "<gold>Scythe Config Reloaded!"));
        messageMap.put(Message.NO_PERMISSION,
                config.getString("no-permission", "<red>You do not have the required permissions to run this command"));
        messageMap.put(Message.NOT_A_PLAYER,
                config.getString("not-a-player", "Sorry! This command can only be run by a player"));
        messageMap.put(Message.HELP_MAIN,
                config.getString("help-main", "<grey>Scythe allows players to harvest grown crops without needing to replant"));
        messageMap.put(Message.HELP_TOGGLE,
                config.getString("help-toggle", "<yellow>/scythe toggle \n<grey>• Toggle scythe on or off"));
        messageMap.put(Message.HELP_RELOAD,
                config.getString("help-reload", "<yellow>/scythe reload \n<grey>• Reloads config settings"));
    }
    public static Map<Message, String> getMessageMap() {
        return Collections.unmodifiableMap(messageMap);
    }

}
