package adhdmc.scythe;

import adhdmc.scythe.Commands.CommandHandler;
import adhdmc.scythe.Commands.SubCommands.HelpCommand;
import adhdmc.scythe.Commands.SubCommands.ReloadCommand;
import adhdmc.scythe.Commands.SubCommands.ToggleCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class Scythe extends JavaPlugin {
    public static Scythe plugin;

    @Override

    public void onEnable() {
        plugin = this;
        getServer().getPluginManager().registerEvents(new InteractListener(), this);
        this.getCommand("scythe").setExecutor(new CommandHandler());
        configDefaults();
        MessageHandler.loadPluginMsgs();
        registerCommands();
    }
    private void configDefaults(){
        this.saveDefaultConfig();
        getConfig().addDefault("Require Hoe", false);
        getConfig().addDefault("Plugin Prefix", "&6&l[&eScythe&6&l]&r ");
        getConfig().addDefault("Toggle On", "&aScythe toggled on!");
        getConfig().addDefault("Toggle Off", "&cScythe toggled off!");
        getConfig().addDefault("No Command", "&cUnknown Command");
        getConfig().addDefault("Config Reload", "&aScythe Config Reloaded!");
        getConfig().addDefault("No Permission", "&cYou do not have the required permissions to run this command");
        getConfig().addDefault("Not A Player", "Sorry! This command can only be run by a player");
        getConfig().addDefault("Help Main", "&7Scythe allows players to harvest grown crops without needing to replant");
        getConfig().addDefault("Help Toggle", "&6/scythe toggle \n&7Toggle scythe on or off");
        getConfig().addDefault("Help Reload", "&6/scythe reload \n&7Reloads config settings");
    }
    private void registerCommands() {
        CommandHandler.subcommandList.put("toggle", new ToggleCommand());
        CommandHandler.subcommandList.put("reload", new ReloadCommand());
        CommandHandler.subcommandList.put("help", new HelpCommand());
    }
}

