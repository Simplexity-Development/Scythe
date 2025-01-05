package simplexity.scythe.commands;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import simplexity.scythe.Scythe;
import simplexity.scythe.config.Message;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class CommandHandler implements CommandExecutor, TabExecutor {
    public static HashMap<String, SubCommand> subcommandList = new HashMap<>();
    MiniMessage miniMessage = Scythe.getMiniMessage();

    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        ArrayList<String> tabCompleteCommands = new ArrayList<>();
        subcommandList.forEach((string, subcommand) -> {
            if (sender.hasPermission(subcommand.getPermission())) {
                tabCompleteCommands.add(string);
            }
        });
        if (args.length == 1) {
            return tabCompleteCommands;
        }
        return null;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (args.length == 0) {
            String url = "https://github.com/Simplexity-Development/Scythe";
            @SuppressWarnings("deprecation") String version = Scythe.getInstance().getDescription().getVersion();
            sender.sendMessage(miniMessage.deserialize("<click:open_url:'<website>'><hover:show_text:'<gray>Click here to visit the GitHub!'>" + "<green>Scythe Version: <version> </hover></click><aqua>| Authors:" + "<click:open_url:'" + url + "'>" + "<hover:show_text:'<gray>Click here to visit the Scythe GitHub!'>" + "<dark_aqua> Rhythmic", Placeholder.parsed("website", url), Placeholder.parsed("version", version)));
            return true;
        }
        String command = args[0].toLowerCase();
        if (subcommandList.containsKey(command) && sender.hasPermission(subcommandList.get(command).getPermission())) {
            subcommandList.get(command).execute(sender, Arrays.copyOfRange(args, 1, args.length));
        } else {
            sender.sendRichMessage(Message.UNKNOWN_COMMAND.getMessage());
        }
        return true;
    }
}
