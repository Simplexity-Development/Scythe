package adhdmc.scythe.commands.subcommands;

import adhdmc.scythe.commands.SubCommand;
import adhdmc.scythe.config.ConfigHandler;
import adhdmc.scythe.Scythe;
import adhdmc.scythe.config.Defaults;
import adhdmc.scythe.config.Message;
import adhdmc.scythe.config.ScythePermission;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

public class ReloadCommand extends SubCommand {
    MiniMessage miniMessage = Scythe.getMiniMessage();

    public ReloadCommand(){
        super("reload", "Reloads the Scythe Plugin", "/scythe reload");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)|| sender.hasPermission(ScythePermission.RELOAD_COMMAND.getPermission())) {
            Scythe.getInstance().reloadConfig();
            ConfigHandler.configParser();
            sender.sendMessage(miniMessage.deserialize(Message.CONFIG_RELOAD.getMessage()));
            return;
        }
        sender.sendMessage(miniMessage.deserialize(Message.NO_PERMISSION.getMessage()));
    }

    @Override
    public List<String> getSubcommandArguments(CommandSender sender, String[] args) {
        return null;
    }
}
