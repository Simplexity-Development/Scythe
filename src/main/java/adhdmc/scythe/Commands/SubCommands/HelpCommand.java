package adhdmc.scythe.Commands.SubCommands;

import adhdmc.scythe.ConfigHandler;
import adhdmc.scythe.Scythe;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.Map;

public class HelpCommand extends SubCommand{
    Map<ConfigHandler.Message, String> msgs = ConfigHandler.getMessageMap();
    MiniMessage mM = MiniMessage.miniMessage();
    public HelpCommand(){
        super ("help","Scythe Info", "/scythe help");
    }

    @Override
    public void doThing(CommandSender sender, String[] args) {
        if(sender.hasPermission(Scythe.usePermission)) {
            sender.sendMessage(mM.deserialize(msgs.get(
                    ConfigHandler.Message.PREFIX) + "\n" + msgs.get(ConfigHandler.Message.HELP_MAIN) +
                    "\n" + msgs.get(ConfigHandler.Message.HELP_RELOAD) +
                    "\n" + msgs.get(ConfigHandler.Message.HELP_TOGGLE)));
            return;
        }
        sender.sendMessage(mM.deserialize(msgs.get(ConfigHandler.Message.NO_PERMISSION)));
    }

    @Override
    public List<String> getSubcommandArguments(CommandSender sender, String[] args) {
        return null;
    }
}
