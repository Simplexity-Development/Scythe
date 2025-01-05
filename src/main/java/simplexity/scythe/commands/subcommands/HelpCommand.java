package simplexity.scythe.commands.subcommands;

import org.bukkit.command.CommandSender;
import simplexity.scythe.commands.SubCommand;
import simplexity.scythe.config.Message;
import simplexity.scythe.config.ScythePermission;

public class HelpCommand extends SubCommand {

    public HelpCommand() {
        super(ScythePermission.USE.getPermission());
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender.hasPermission(ScythePermission.USE.getPermission())) {
            sender.sendRichMessage((Message.PREFIX.getMessage()
               + "\n" + Message.HELP_MAIN.getMessage()
               + "\n" + Message.HELP_RELOAD.getMessage()
               + "\n" + Message.HELP_TOGGLE.getMessage()));
            return;
        }
        sender.sendRichMessage(Message.NO_PERMISSION.getMessage());
    }
}
