package adhdmc.scythe.Commands;

import adhdmc.scythe.Commands.SubCommands.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class CommandHandler implements CommandExecutor, TabExecutor {
    public static HashMap<String, SubCommand> subcommandList = new HashMap<String, SubCommand>();

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
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "\n&aAuthor: &6IllogicalSong\n&aVersion:&7 ALPHA"));
            return true;
        }
        String command = args[0].toLowerCase();
        if (subcommandList.containsKey(command)) {
            subcommandList.get(command).doThing(sender, Arrays.copyOfRange(args, 1, args.length));
        } else {
            sender.sendMessage("Sorry, you input the command incorrectly. Please use `/scythe` help to see proper usage");
        }
        return true;
    }
}
