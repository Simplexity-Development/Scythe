package adhdmc.scythe.Commands;

import adhdmc.scythe.Commands.SubCommands.SubCommand;
import adhdmc.scythe.MessageHandler;
import adhdmc.scythe.Scythe;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.*;

public class CommandHandler implements CommandExecutor, TabExecutor {
    public static HashMap<String, SubCommand> subcommandList = new HashMap<String, SubCommand>();
    Map<MessageHandler.Message, String> msgs = MessageHandler.getMessageMap();
    MiniMessage mM = MiniMessage.miniMessage();

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        ArrayList<String> tabbableCommands = new ArrayList<String>(Arrays.asList("toggle", "reload", "help"));
        if (args.length == 1) {
            return tabbableCommands;
        }
        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0){
            sender.sendMessage(mM.deserialize("<click:open_url:'https://github.com/RhythmicSys/Scythe'><hover:show_text:'<gray>Click here to visit the GitHub!'>" +
                    "<green>Scythe Version: <version> </hover></click><aqua>| Authors:" +
                    "<click:open_url:'https://github.com/RhythmicSys'>" +
                    "<hover:show_text:'<gray>Click here to visit Rhythmic\\'s GitHub!'>" +
                    "<dark_aqua> Rhythmic", Placeholder.parsed("version", String.valueOf(Scythe.version))));
            return true;
        }
        String command = args[0].toLowerCase();
        if (subcommandList.containsKey(command)) {
            subcommandList.get(command).doThing(sender, Arrays.copyOfRange(args, 1, args.length));
        } else {
            sender.sendMessage(mM.deserialize(msgs.get(MessageHandler.Message.UNKNOWN_COMMAND)));
        }
        return true;
    }
}
