package adhdmc.scythe;

import adhdmc.scythe.Commands.CommandHandler;
import adhdmc.scythe.Commands.SubCommands.HelpCommand;
import adhdmc.scythe.Commands.SubCommands.ReloadCommand;
import adhdmc.scythe.Commands.SubCommands.ToggleCommand;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class Scythe extends JavaPlugin {
    public static Scythe plugin;
    public static final double version = 1.0;
    public static final String usePermission = "scythe.use";
    public static final String togglePermission = "scythe.toggle";
    public static final String reloadPermission = "scythe.reload";
    public static final String replantingEnabled = "replanting-enabled";
    public static final String replantingDisabled = "replanting-disabled";
    public static final String pluginLoggerPrefix = "[Scythe] ";

    @Override

    public void onEnable() {
        plugin = this;
        try {
            Class.forName("net.kyori.adventure.text.minimessage.MiniMessage");
            Class.forName("net.kyori.adventure.text.Component");
            Class.forName("com.destroystokyo.paper.MaterialTags");
        } catch (ClassNotFoundException e){
            this.getLogger().severe("[Scythe] This plugin depends on classes not found on your server software. This plugin depends on PaperMC, versions after 1.18.2. Disabling plugin");
            this.getServer().getPluginManager().disablePlugin(this);
        }
        if (plugin.getServer().getPluginManager().isPluginEnabled("CoreProtect")){
            getServer().getPluginManager().registerEvents(new InteractListenerDependsCoreprotect(), this);
        } else {
            getServer().getPluginManager().registerEvents(new InteractListener(), this);
        }
        this.getCommand("scythe").setExecutor(new CommandHandler());
        this.saveDefaultConfig();
        configDefaults();
        ConfigHandler.configParser();
        registerCommands();
    }

    private void registerCommands() {
        CommandHandler.subcommandList.put("toggle", new ToggleCommand());
        CommandHandler.subcommandList.put("reload", new ReloadCommand());
        CommandHandler.subcommandList.put("help", new HelpCommand());
    }

    private void configDefaults(){
        FileConfiguration config = this.getConfig();
        config.addDefault("require-hoe", false);
        config.addDefault("allow-right-click-to-harvest", true);
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

