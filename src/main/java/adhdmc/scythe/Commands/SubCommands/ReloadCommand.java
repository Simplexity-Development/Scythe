package adhdmc.scythe.Commands.SubCommands;

import adhdmc.scythe.MessageHandler;
import adhdmc.scythe.Scythe;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class ReloadCommand extends SubCommand {

    public ReloadCommand(){
        super("reload", "Reloads the Scythe Plugin", "/scythe reload");
    }

    @Override
    public void doThing(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)|| sender.hasPermission("scythe.reload")) {
            Scythe.plugin.reloadConfig();
            sender.sendMessage(MessageHandler.configReload);
            return;
        }
        sender.sendMessage(MessageHandler.noPermission);
    }

    @Override
    public List<String> getSubcommandArguments(CommandSender sender, String[] args) {
        return null;
    }
}
