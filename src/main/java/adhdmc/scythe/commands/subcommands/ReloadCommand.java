package adhdmc.scythe.commands.subcommands;

import adhdmc.scythe.commands.SubCommand;
import adhdmc.scythe.config.ConfigHandler;
import adhdmc.scythe.Scythe;
import adhdmc.scythe.config.Defaults;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

public class ReloadCommand extends SubCommand {
    Map<ConfigHandler.Message, String> msgs = ConfigHandler.getMessageMap();
    MiniMessage miniMessage = Scythe.getMiniMessage();

    public ReloadCommand(){
        super("reload", "Reloads the Scythe Plugin", "/scythe reload");
    }

    @Override
    public void doThing(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)|| sender.hasPermission(ConfigHandler.getPermMap().get(ConfigHandler.Permission.RELOAD_COMMAND))) {
            Scythe.getInstance().reloadConfig();
            ConfigHandler.configParser();
            sender.sendMessage(miniMessage.deserialize(msgs.get(ConfigHandler.Message.CONFIG_RELOAD)));
            return;
        }
        sender.sendMessage(miniMessage.deserialize(msgs.get(ConfigHandler.Message.NO_PERMISSION)));
    }

    @Override
    public List<String> getSubcommandArguments(CommandSender sender, String[] args) {
        return null;
    }
}
