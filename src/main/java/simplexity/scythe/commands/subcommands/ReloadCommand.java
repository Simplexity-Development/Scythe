package simplexity.scythe.commands.subcommands;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import simplexity.scythe.Scythe;
import simplexity.scythe.commands.SubCommand;
import simplexity.scythe.config.ConfigHandler;
import simplexity.scythe.config.LocaleHandler;
import simplexity.scythe.config.ScythePermission;

public class ReloadCommand extends SubCommand {
    MiniMessage miniMessage = Scythe.getMiniMessage();

    public ReloadCommand() {
        super(ScythePermission.RELOAD_COMMAND.getPermission());
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!sender.hasPermission(ScythePermission.RELOAD_COMMAND.getPermission())) {
            sender.sendMessage(miniMessage.deserialize(LocaleHandler.getInstance().getNoPermission()));
            return;
        }
        Scythe.getInstance().reloadConfig();
        ConfigHandler.getInstance().configParser();
        sender.sendMessage(miniMessage.deserialize(LocaleHandler.getInstance().getConfigReloaded()));
    }
}
