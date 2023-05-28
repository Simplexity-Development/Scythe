package simplexity.scythe;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import simplexity.scythe.commands.CommandHandler;
import simplexity.scythe.commands.subcommands.HelpCommand;
import simplexity.scythe.commands.subcommands.ReloadCommand;
import simplexity.scythe.commands.subcommands.ToggleCommand;
import simplexity.scythe.config.ConfigHandler;
import simplexity.scythe.interactions.InteractListener;

import java.util.Objects;
import java.util.logging.Logger;

public final class Scythe extends JavaPlugin {
    private static Scythe instance;
    private static Logger logger;
    private static final MiniMessage miniMessage = MiniMessage.miniMessage();

    @Override

    public void onEnable() {
        instance = this;
        logger = this.getLogger();
        //noinspection unused - Bstats requirement
        Metrics metrics = new Metrics(this, 16540);
        try {
            Class.forName("net.kyori.adventure.text.minimessage.MiniMessage");
            Class.forName("net.kyori.adventure.text.Component");
        } catch (ClassNotFoundException e) {
            this.getLogger().severe("This plugin depends on classes not found on your server software:");
            this.getLogger().warning("net.kyori.adventure.text.minimessage.MiniMessage");
            this.getLogger().warning("net.kyori.adventure.text.Component");
            this.getServer().getPluginManager().disablePlugin(this);
        }
        Objects.requireNonNull(this.getCommand("scythe")).setExecutor(new CommandHandler());
        this.getServer().getPluginManager().registerEvents(new InteractListener(), this);
        this.saveDefaultConfig();
        ConfigHandler.getInstance().configParser();
        registerCommands();
    }

    public static Plugin getInstance() {
        return instance;
    }

    public static Logger getScytheLogger() {
        return logger;
    }

    public static MiniMessage getMiniMessage() {
        return miniMessage;
    }

    private void registerCommands() {
        CommandHandler.subcommandList.put("toggle", new ToggleCommand());
        CommandHandler.subcommandList.put("reload", new ReloadCommand());
        CommandHandler.subcommandList.put("help", new HelpCommand());
    }


}

