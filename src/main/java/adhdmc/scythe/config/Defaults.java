package adhdmc.scythe.config;

import adhdmc.scythe.Scythe;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class Defaults {
    private static final Plugin instance = Scythe.getInstance();
    public static void configDefaults(){
        FileConfiguration config = instance.getConfig();
        config.addDefault("require-hoe", false);
        config.addDefault("allow-right-click-to-harvest", true);
        config.addDefault("crops", List.of("BEETROOTS", "CARROTS", "COCOA", "NETHER_WART", "POTATOES", "WHEAT"));
        config.addDefault("prefix","<gold><bold>[</bold><yellow>Scythe</yellow><bold>]<reset>");
        config.addDefault("toggle-on", "<green>Scythe toggled on!");
        config.addDefault("toggle-off", "<red>Scythe toggled off!");
        config.addDefault("unknown-command", "<red>Unknown Command");
        config.addDefault("config-reload", "<gold>Scythe Config Reloaded!");
        config.addDefault("no-permission", "<red>You do not have the required permissions to run this command");
        config.addDefault("not-a-player", "Sorry! This command can only be run by a player");
        config.addDefault("help-main", "<grey>Scythe allows players to harvest grown crops without needing to replant");
        config.addDefault("help-toggle", "<yellow>/scythe toggle \n<grey>• Toggle scythe on or off");
        config.addDefault("help-reload", "<yellow>/scythe reload \n<grey>• Reloads config settings");
    }
}
