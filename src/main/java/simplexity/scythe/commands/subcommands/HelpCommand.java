package simplexity.scythe.commands.subcommands;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import simplexity.scythe.Scythe;
import simplexity.scythe.commands.SubCommand;
import simplexity.scythe.config.Message;
import simplexity.scythe.config.ScythePermission;

public class HelpCommand extends SubCommand {
    MiniMessage miniMessage = Scythe.getMiniMessage();

    public HelpCommand() {
        super(ScythePermission.USE.getPermission());
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender.hasPermission(ScythePermission.USE.getPermission())) {
            sender.sendMessage(miniMessage.deserialize((Message.PREFIX.getMessage() + "\n" + Message.HELP_MAIN.getMessage() + "\n" + Message.HELP_RELOAD.getMessage() + "\n" + Message.HELP_TOGGLE.getMessage())));
            return;
        }
        sender.sendMessage(miniMessage.deserialize(Message.NO_PERMISSION.getMessage()));
    }
}
