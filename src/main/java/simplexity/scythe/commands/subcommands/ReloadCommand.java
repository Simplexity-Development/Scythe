package simplexity.scythe.commands.subcommands;

import org.bukkit.command.CommandSender;
import simplexity.scythe.Scythe;
import simplexity.scythe.commands.SubCommand;
import simplexity.scythe.config.ConfigHandler;
import simplexity.scythe.config.Message;
import simplexity.scythe.config.ScythePermission;

public class ReloadCommand extends SubCommand {

    public ReloadCommand() {
        super(ScythePermission.RELOAD_COMMAND.getPermission());
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!sender.hasPermission(ScythePermission.RELOAD_COMMAND.getPermission())) {
            sender.sendRichMessage(Message.ERROR_NO_PERMISSION.getMessage());
            return;
        }
        Scythe.getInstance().reloadConfig();
        ConfigHandler.getInstance().configParser();
        sender.sendRichMessage(Message.CONFIG_RELOADED.getMessage());
    }
}
