package adhdmc.scythe.Commands.SubCommands;

import adhdmc.scythe.MessageHandler;
import adhdmc.scythe.Scythe;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.Map;

public class HelpCommand extends SubCommand{
    Map<MessageHandler.Message, String> msgs = MessageHandler.getMessageMap();
    MiniMessage mM = MiniMessage.miniMessage();
    public HelpCommand(){
        super ("help","Scythe Info", "/scythe help");
    }

    @Override
    public void doThing(CommandSender sender, String[] args) {
        if(sender.hasPermission(Scythe.usePermission)) {
            sender.sendMessage(mM.deserialize(msgs.get(
                    MessageHandler.Message.PREFIX) + "\n" + msgs.get(MessageHandler.Message.HELP_MAIN) +
                    "\n" + msgs.get(MessageHandler.Message.HELP_RELOAD) +
                    "\n" + msgs.get(MessageHandler.Message.HELP_TOGGLE)));
            return;
        }
        sender.sendMessage(mM.deserialize(msgs.get(MessageHandler.Message.NO_PERMISSION)));
    }

    @Override
    public List<String> getSubcommandArguments(CommandSender sender, String[] args) {
        return null;
    }
}
