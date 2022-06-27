package adhdmc.scythe.Commands.SubCommands;

import adhdmc.scythe.MessageHandler;
import org.bukkit.command.CommandSender;

import java.util.List;

public class HelpCommand extends SubCommand{
    public HelpCommand(){
        super ("help","Villager Info help", "/vill help");
    }

    @Override
    public void doThing(CommandSender sender, String[] args) {
        if(sender.hasPermission("scythe.use")) {
            sender.sendMessage(MessageHandler.prefix + MessageHandler.helpMain);
            sender.sendMessage(MessageHandler.helpToggle);
            sender.sendMessage(MessageHandler.helpReload);
            return;
        }
        sender.sendMessage(MessageHandler.noPermission);
    }

    @Override
    public List<String> getSubcommandArguments(CommandSender sender, String[] args) {
        return null;
    }
}
