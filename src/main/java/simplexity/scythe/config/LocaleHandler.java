package simplexity.scythe.config;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import simplexity.scythe.Scythe;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public class LocaleHandler {
    private static LocaleHandler instance;
    private final String fileName = "locale.yml";
    private final File localeFile = new File(Scythe.getInstance().getDataFolder(), fileName);
    private final FileConfiguration localeConfig = new YamlConfiguration();
    private final Logger logger = Scythe.getInstance().getLogger();
    private String prefix, toggleEnabled, toggleDisabled, unknownCommand, configReloaded,
            noPermission, notAPlayer, helpMain, helpToggle, helpReload;

    private LocaleHandler() {
        if (!localeFile.exists()) {
            Scythe.getInstance().saveResource(fileName, false);
        }
    }

    public static LocaleHandler getInstance() {
        if (instance == null) instance = new LocaleHandler();
        return instance;
    }

    public FileConfiguration getLocaleConfig() {
        return localeConfig;
    }

    public void loadLocale() {
        try {
            localeConfig.load(localeFile);
        } catch (IOException | InvalidConfigurationException e) {
            logger.severe("Issue loading locale.yml");
            e.printStackTrace();
        }
        prefix = localeConfig.getString("prefix", "<gold><bold>[</bold><yellow>Scythe</yellow><bold>]<reset> ");
        toggleEnabled = localeConfig.getString("feedback.toggle-enabled", "<prefix><green>Scythe functionality enabled!</green>");
        toggleDisabled = localeConfig.getString("feedback.toggle-disabled", "<prefix><grey>Scythe functionality <red>disabled</red>!</grey>");
        configReloaded = localeConfig.getString("feedback.config-reloaded", "<gold>Scythe Config Reloaded!</gold>");
        unknownCommand = localeConfig.getString("errors.unknown-command", "<red>Unknown Command</red>");
        noPermission = localeConfig.getString("errors.no-permission", "<red>You do not have the required Permission to run this command</red>");
        notAPlayer = localeConfig.getString("errors.not-a-player", "Sorry! This command can only be run by a player");
        helpMain = localeConfig.getString("help.main", "<grey>Scythe allows players to harvest grown crops without needing to replant</grey>");
        helpToggle = localeConfig.getString("help.toggle", "<yellow>/scythe toggle \n<grey>• Toggle scythe on or off</grey></yellow>");
        helpReload = localeConfig.getString("help.reload", "<yellow>/scythe reload \n<grey>• Reloads config settings</grey></yellow>");
    }

    public String getPrefix() {
        return prefix;
    }

    public String getToggleEnabled() {
        return toggleEnabled;
    }

    public String getToggleDisabled() {
        return toggleDisabled;
    }

    public String getUnknownCommand() {
        return unknownCommand;
    }

    public String getConfigReloaded() {
        return configReloaded;
    }

    public String getNoPermission() {
        return noPermission;
    }

    public String getNotAPlayer() {
        return notAPlayer;
    }

    public String getHelpMain() {
        return helpMain;
    }

    public String getHelpToggle() {
        return helpToggle;
    }

    public String getHelpReload() {
        return helpReload;
    }
}
