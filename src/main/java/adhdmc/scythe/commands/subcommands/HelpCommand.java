package adhdmc.scythe.commands.subcommands;

import adhdmc.scythe.Scythe;
import adhdmc.scythe.commands.SubCommand;
import adhdmc.scythe.config.Message;
import adhdmc.scythe.config.ScythePermission;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;

import java.util.List;

public class HelpCommand extends SubCommand {
    MiniMessage miniMessage = Scythe.getMiniMessage();
    public HelpCommand(){
        super ("help","Scythe Info", "/scythe help");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender.hasPermission(ScythePermission.USE.getPermission())) {
            sender.sendMessage(miniMessage.deserialize((Message.PREFIX.getMessage() +
                    "\n" + Message.HELP_MAIN.getMessage() +
                    "\n" + Message.HELP_RELOAD.getMessage() +
                    "\n" + Message.HELP_TOGGLE.getMessage())));
            return;
        }
        sender.sendMessage(miniMessage.deserialize(Message.NO_PERMISSION.getMessage()));
    }

    @Override
    public List<String> getSubcommandArguments(CommandSender sender, String[] args) {
        return null;
    }
}
