package adhdmc.scythe;

import adhdmc.scythe.commands.CommandHandler;
import adhdmc.scythe.commands.subcommands.HelpCommand;
import adhdmc.scythe.commands.subcommands.ReloadCommand;
import adhdmc.scythe.commands.subcommands.ToggleCommand;
import adhdmc.scythe.config.ConfigHandler;
import adhdmc.scythe.config.Defaults;
import adhdmc.scythe.listeners.InteractListener;
import adhdmc.scythe.listeners.InteractListenerDependsCoreprotect;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class Scythe extends JavaPlugin {
    private static Scythe instance;
    private static Logger logger;
    private static FileConfiguration config;
    private static final MiniMessage miniMessage = MiniMessage.miniMessage();

    @Override

    public void onEnable() {
        instance = this;
        logger = this.getLogger();
        config = this.getConfig();
        try {
            Class.forName("net.kyori.adventure.text.minimessage.MiniMessage");
            Class.forName("net.kyori.adventure.text.Component");
            Class.forName("com.destroystokyo.paper.MaterialTags");
        } catch (ClassNotFoundException e){
            this.getLogger().severe("[Scythe] This plugin depends on classes not found on your server software. This plugin depends on PaperMC, versions after 1.18.2. Disabling plugin");
            this.getServer().getPluginManager().disablePlugin(this);
        }
        if (instance.getServer().getPluginManager().isPluginEnabled("CoreProtect")){
            getServer().getPluginManager().registerEvents(new InteractListenerDependsCoreprotect(), this);
        } else {
            getServer().getPluginManager().registerEvents(new InteractListener(), this);
        }
        this.getCommand("scythe").setExecutor(new CommandHandler());
        this.saveDefaultConfig();
        Defaults.configDefaults();
        Defaults.setPerms();
        ConfigHandler.configParser();
        registerCommands();
    }

    public static Plugin getInstance(){
        return instance;
    }

    public static Logger getScytheLogger(){
        return logger;
    }

    public static FileConfiguration getScytheConfig(){
        return config;
    }

    public static MiniMessage getMiniMessage(){
        return miniMessage;
    }

    private void registerCommands() {
        CommandHandler.subcommandList.put("toggle", new ToggleCommand());
        CommandHandler.subcommandList.put("reload", new ReloadCommand());
        CommandHandler.subcommandList.put("help", new HelpCommand());
    }


}

