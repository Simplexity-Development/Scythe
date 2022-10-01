package adhdmc.scythe.commands.subcommands;

import adhdmc.scythe.Scythe;
import adhdmc.scythe.commands.SubCommand;
import adhdmc.scythe.config.ConfigHandler;
import adhdmc.scythe.config.Defaults;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.Map;

public class HelpCommand extends SubCommand {
    Map<ConfigHandler.Message, String> msgs = ConfigHandler.getMessageMap();
    MiniMessage miniMessage = Scythe.getMiniMessage();
    public HelpCommand(){
        super ("help","Scythe Info", "/scythe help");
    }

    @Override
    public void doThing(CommandSender sender, String[] args) {
        if(sender.hasPermission(ConfigHandler.getPermMap().get(ConfigHandler.Permission.USE))) {
            sender.sendMessage(miniMessage.deserialize(msgs.get(
                    ConfigHandler.Message.PREFIX) + "\n" + msgs.get(ConfigHandler.Message.HELP_MAIN) +
                    "\n" + msgs.get(ConfigHandler.Message.HELP_RELOAD) +
                    "\n" + msgs.get(ConfigHandler.Message.HELP_TOGGLE)));
            return;
        }
        sender.sendMessage(miniMessage.deserialize(msgs.get(ConfigHandler.Message.NO_PERMISSION)));
    }

    @Override
    public List<String> getSubcommandArguments(CommandSender sender, String[] args) {
        return null;
    }
}
